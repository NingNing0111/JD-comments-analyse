package me.pgthinker.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 15:37
 * @Description:
 */
public class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private final IntWritable res = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values,
                          Context context) throws IOException, InterruptedException {
        int result = 0;
        for (IntWritable value : values) {
            result += value.get();
        }
        res.set(result);
        context.write(key, res);
    }
}
