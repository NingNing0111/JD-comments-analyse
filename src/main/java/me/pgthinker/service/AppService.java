package me.pgthinker.service;

import lombok.RequiredArgsConstructor;
import me.pgthinker.crawler.JdCrawler;
import me.pgthinker.hadoop.HdfsClient;
import me.pgthinker.hadoop.WordHandler;
import me.pgthinker.util.WordCloudUtil;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * @Project: me.pgthinker.service
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 19:46
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class AppService {

    private final JdCrawler jdCrawler;
    private final WordHandler wordHandler;
    private final HdfsClient hdfsClient;
    public void runCrawler(Long productionId, Session session) throws IOException {
       try {
           jdCrawler.run(productionId,session);
           session.getBasicRemote().sendText("评论爬取完成");
       }catch (Exception e){
           session.getBasicRemote().sendText("爬取出现异常: "+e.getMessage());
       }
    }

    public void runAnalysis(Session session) throws IOException {
        session.getBasicRemote().sendText("开始执行Hadoop 统计分析...");
        boolean runResult = wordHandler.run();
        if(runResult){
            session.getBasicRemote().sendText("统计分析完成,开始获取统计结果...");
            Map<String, Integer> resultFromDfs = hdfsClient.getResultFromDfs("/output");
            session.getBasicRemote().sendText("统计结果如下：");
            session.getBasicRemote().sendText("-------------------------");
            for(String key: resultFromDfs.keySet()){
                session.getBasicRemote().sendText(key + " " + resultFromDfs.get(key));
            }
            session.getBasicRemote().sendText("-------------------------");
        }else{
            session.getBasicRemote().sendText("执行Hadoop 统计分析过程出现异常");
        }
    }

    public void exportWordCloud(Session session) throws IOException {
        session.getBasicRemote().sendText("正在导出词云....");
        String outputPath = "/output";
        String fileName = "jd_comments_word_cloud.png";
        Map<String, Integer> result = hdfsClient.getResultFromDfs(outputPath);
        WordCloudUtil.createWordCloud(result,fileName,150);
        File file = new File(fileName);
        session.getBasicRemote().sendText("词云导出成功。");
        session.getBasicRemote().sendText(file.getAbsolutePath());
    }
}
