package me.pgthinker.websocket;

import lombok.extern.slf4j.Slf4j;
import me.pgthinker.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Project: me.pgthinker.websocket
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 19:28
 * @Description:
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{id}")
public class AppWebSocket {
    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;
    private String id;

    private static AppService appService;

    @Autowired
    public void setAppService(AppService appService){
        AppWebSocket.appService = appService;
    }

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     * 注意这里的kv,设计的很巧妙，v刚好是本类 WebSocket (用来存放每个客户端对应的MyWebSocket对象)
     */
    private static ConcurrentHashMap<String,AppWebSocket> webSocketSet = new ConcurrentHashMap<>();

    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "id") String name){
        log.info("----------------------------------");
        this.session = session;
        this.id = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name,this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
        log.info("----------------------------------");
        log.info("");

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void OnClose(){
        webSocketSet.remove(this.id);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.info("发生错误");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void OnMessage(String message_str) throws IOException {
        log.info("[WebSocket] 收到消息：{}",message_str);
        // run crawler [production_id]: 执行爬虫, 爬虫产品为production_id的评论
        if(message_str.startsWith("run crawler ")){
            // 定义正则表达式模式
            String patternString = "run crawler \\[(\\d+)\\]";
            // 编译正则表达式模式
            Pattern pattern = Pattern.compile(patternString);
            // 创建匹配器对象
            Matcher matcher = pattern.matcher(message_str);
            if(matcher.find()){
                String productionIdStr = matcher.group(1);
                long productionId = Long.parseLong(productionIdStr);
                appService.runCrawler(productionId,this.session);
            }else{
                this.session.getBasicRemote().sendText("production_id匹配错误");
            }
        }else if(message_str.equals("run analysis")){ // run analysis: 启动hadoop进行分析
            appService.runAnalysis(this.session);
        }else if(message_str.equals("export wordCloud")){
            appService.exportWordCloud(this.session);
        }else{
            this.session.getBasicRemote().sendText("发送:run crawler [production_id] 即可爬取对应Id的产品评论;\n发送:run analysis 即可对爬取的结果进行分析\n发送:export wordCloud 导出词云图片");

        }
    }

}
