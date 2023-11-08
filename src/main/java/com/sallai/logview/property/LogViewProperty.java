package com.sallai.logview.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName LogViewProperty
 * @Description TODO
 * @Author sallai
 * @Date 15:58 2023/11/5
 * @Version 1.0
 **/
@ConfigurationProperties("logview")
@Data
public class LogViewProperty {
    /**
     * 是否开启
     */
    private boolean enable;

    /**
     * 登录用户名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     *
     */
    private String urlPattern;
}
