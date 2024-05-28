package me.pgthinker.hadoop;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 15:35
 * @Description:
 */
@Component
@RequiredArgsConstructor
public class WordHandler {

    @Value("${hadoop.hdfs-url}")
    private String hdfsUrl;

    private final FileSystem fs;

    private Job init() throws IOException {
        Job job = Job.getInstance();
        job.setJobName("JD comments M-R");
        job.setJarByClass(WordHandler.class);
        job.setReducerClass(WordReducer.class);
        job.setMapperClass(WordMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 输入路径
        Path input_dir = new Path(hdfsUrl+"/input");
        Path output_dir = new Path(hdfsUrl+"/output");
        if(fs.exists(output_dir)){// 如果输出目录存在，则递归删除
            fs.delete(output_dir,true);
        }
        FileInputFormat.addInputPath(job,input_dir);
        FileOutputFormat.setOutputPath(job,output_dir);
        return job;
    }

    public boolean run() {
        try {
            Job job = init();
            return job.waitForCompletion(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}




