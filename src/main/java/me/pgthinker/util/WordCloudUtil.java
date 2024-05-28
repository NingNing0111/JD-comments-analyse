package me.pgthinker.util;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.*;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/27 18:34
 * @Description:
 */
public class WordCloudUtil {
    public static void createWordCloud(Map<String,Integer> resultMap, String outputPath,int n){

        List<WordFrequency> wordFrequencies = new ArrayList<>();
        Set<String> keys = resultMap.keySet();
        for(String key: keys){
            wordFrequencies.add(new WordFrequency(key,resultMap.get(key)));
        }
        if(n != -1){
            wordFrequencies  = wordFrequencies.subList(0, n);
        }
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);

        Dimension dimension = new Dimension(1000, 1000);
        // 此处的设置采用内置常量即可，生成词云对象
        WordCloud wordCloud = getWordCloud(dimension, font);
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(outputPath);
    }

    private static WordCloud getWordCloud(Dimension dimension, Font font) {
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        //设置边界及字体
        wordCloud.setPadding(5);
        //因为我这边是生成一个圆形,这边设置圆的半径
        wordCloud.setFontScalar(new SqrtFontScalar(16, 50));
        //设置词云显示的三种颜色，越靠前设置表示词频越高的词语的颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        //因为我这边是生成一个圆形,这边设置圆的半径
        wordCloud.setBackground(new CircleBackground(500));
        return wordCloud;
    }
}
