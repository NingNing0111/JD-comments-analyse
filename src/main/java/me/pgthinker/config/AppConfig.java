package me.pgthinker.config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 19:44
 * @Description:
 */
@Configuration
public class AppConfig {

    @Value("${hadoop.hdfs-url}")
    private String hdfsUrl;

    @Value("${hadoop.user}")
    private String user;
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public FileSystem fileSystem() throws IOException, InterruptedException {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        return FileSystem.get(URI.create(hdfsUrl), conf, user);
    }


}
