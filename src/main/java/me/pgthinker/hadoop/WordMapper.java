package me.pgthinker.hadoop;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.File;
import java.util.List;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 15:38
 * @Description:
 */

public class WordMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final IntWritable one = new IntWritable(1);
    private final Text word = new Text();
    private final JiebaSegmenter jieba = new JiebaSegmenter();

    @Override
    protected void map(Object key, Text value, Context context) {
        String line = value.toString().trim().replace(" ","");
        List<String> words = jieba.sentenceProcess(line);
        File file = new File("src/main/resources/stopword.dic");
        try {
            List<String >stopWords = FileUtils.readLines(file,"utf8");
            words.removeAll(stopWords);
            for(String w: words){
                word.set(w);
                context.write(word,one);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
