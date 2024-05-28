package me.pgthinker.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Project: me.pgthinker
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 19:33
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JdComment {
    /** 评论ID */
    private Long id;

    /** 全局唯一标识 */
    private String guid;

    /** 评论内容 */
    private String content;

    /** 创建时间 */
    private String creationTime;

}
