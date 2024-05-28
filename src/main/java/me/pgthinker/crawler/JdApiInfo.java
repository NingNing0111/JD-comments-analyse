package me.pgthinker.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 19:52
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JdApiInfo {
    private List<JdComment> comments;
    private int maxPage;
}
