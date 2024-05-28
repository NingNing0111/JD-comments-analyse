package me.pgthinker.hadoop;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 21:36
 * @Description:
 */
@Component
@RequiredArgsConstructor
public class HdfsClient {
    private final FileSystem fs;

    // 写入内容到文件中
    public void saveContentToDfs(String content, String fileName) throws IOException {

        Path inFile =new Path("/input/" + fileName);
        try (FSDataOutputStream outputStream = fs.create(inFile)) {
            byte[] contentBytes = content.getBytes();
            outputStream.write(contentBytes, 0, contentBytes.length);
        }
    }

    // 结果为：key value，并以value 从大到小排序
    public Map<String,Integer> getResultFromDfs(String output) {
        Path path = new Path(output + "/part-r-00000");
        Map<String,Integer> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split("\t");
                result.put(s[0],Integer.parseInt(s[1]));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 根据value 从大到小排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(result.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
