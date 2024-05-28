package me.pgthinker.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.hadoop.HdfsClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 20:09
 * @Description:
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JdCrawler {

    private final RestTemplate restTemplate;

    private final int pageSize = 10;
    private final String baseUrl = "https://club.jd.com/comment/productPageComments.action";

    private final HdfsClient hdfsClient;

    // page自动递增，一直爬取，直到没有评论就停止
    public void run(long productId, Session session) {
        int currPage = 0;
        AtomicBoolean shouldContinue = new AtomicBoolean(true);
        while(shouldContinue.get()){
            int finalCurrPage = currPage;
            List<JdComment> comments = crawler(productId, finalCurrPage);
            if(comments.isEmpty()){
                shouldContinue.set(false);
            }
            for(JdComment comment: comments){
                try {
                    session.getBasicRemote().sendText("正在写入评论... Id:"+comment.getId());
                    hdfsClient.saveContentToDfs(comment.getContent(),comment.getId().toString());
                    session.getBasicRemote().sendText("写入评论完成！Id:"+comment.getId());
                } catch (IOException e) {
                    log.info("评论写入出现异常 Id:{} message:{}",comment.getId(),e.getMessage());
                }
            }
            currPage++;
        }
    }

    /**
     *
     * @param productId 产品ID
     * @param page 第几页
     * @return
     */
    private List<JdComment> crawler(long productId, int page){

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("productId", productId)
                .queryParam("sortType",5)
                .queryParam("page",page)
                .queryParam("pageSize",pageSize)
                .queryParam("score", 0);
        JdApiInfo jdApiInfo = restTemplate.getForObject(builder.toUriString(), JdApiInfo.class);
        assert jdApiInfo != null;
        List<JdComment> comments = jdApiInfo.getComments();
        // 过滤信息
        int size = comments.size();
        ArrayList<JdComment> results = new ArrayList<>();
        for(int i=0;i<size;i++){
            JdComment jdComment = comments.get(i);
            if(!jdComment.getContent().contains("此用户未填写评价内容")){
                results.add(jdComment);
            }
        }
        return results;
    }


}
