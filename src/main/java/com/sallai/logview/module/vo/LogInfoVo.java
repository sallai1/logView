package com.sallai.logview.module.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className LogInfo
 * @description TODO
 * @author sallai
 * @date 21:10 2023/10/29
 * @version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInfoVo {
    /**
     * 日志文件名称
     */
    private String name;
    /**
     * 日志文件大小
     */
    private String size;
    /**
     * 日志文件内容
     */
    private String content;
}
