//package me.pgthinker;
//
//import me.pgthinker.crawler.JdApiInfo;
//import me.pgthinker.crawler.JdCrawler;
//import me.pgthinker.hadoop.HdfsClient;
//import me.pgthinker.hadoop.WordHandler;
//import me.pgthinker.util.WordCloudUtil;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.QuotaUsage;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * @Project: me.pgthinker
// * @Author: pgthinker
// * @GitHub: https://github.com/ningning0111
// * @Date: 2024/5/26 19:41
// * @Description:
// */
//@SpringBootTest
//public class AppTest {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private JdCrawler jdCrawler;
//
//    @Autowired
//    private HdfsClient hdfsClient;
//
//    @Autowired
//    private FileSystem fileSystem;
//
//    @Autowired
//    private WordHandler wordHandler;
//
//    @Test
//    public void test(){
//        System.out.println("This is test ...");
//    }
//
//    @Test
//    public void commentDataTest(){
//        long productId = 100059354001L;
//        int score = 0;
//        int sortType = 5;
//        int page = 40; // 第几页
//        int pageSize = 10; // 每页数量
//        String baseUrl = "https://club.jd.com/comment/productPageComments.action";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
//                .queryParam("productId", productId)
//                .queryParam("sortType",sortType)
//                .queryParam("page",page)
//                .queryParam("pageSize",pageSize)
//                .queryParam("score", score);
//        JdApiInfo jdApiInfo = restTemplate.getForObject(builder.toUriString(), JdApiInfo.class);
//        assert jdApiInfo != null;
//
//        System.out.println(jdApiInfo.getComments().size());
//    }
//
//
//
//
//    public void hdfsTest() {
//        try {
//            hdfsClient.saveContentToDfs("这是测试内容","测试文件");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public void connectTest() throws IOException {
//        QuotaUsage quotaUsage = fileSystem.getQuotaUsage(new Path("/"));
//        System.out.println(quotaUsage);
//    }
//
//    @Test
//    public void handlerTest()  {
//        wordHandler.run();
//    }
//
//    @Test
//    public void getResultTest(){
//        String outputPath = "/output";
//        Map<String, Integer> result = hdfsClient.getResultFromDfs(outputPath);
//        System.out.println(result);
//    }
//
//    @Test
//    public void wordCloudTest(){
//        String outputPath = "/output";
//
//        Map<String, Integer> result = hdfsClient.getResultFromDfs(outputPath);
//        WordCloudUtil.createWordCloud(result,"jd_comments_word_cloud.png",150);
//    }
//}
