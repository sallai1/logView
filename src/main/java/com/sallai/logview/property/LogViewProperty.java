package com.sallai.logview.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @className LogViewProperty
 * @description TODO
 * @author sallai
 * @date 15:58 2023/11/5
 * @version 1.0
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
    private String loginName = "admin";

    /**
     * 登录密码
     */
    private String loginPassword = "logview";

    /**
     *
     */
    private String urlPattern;
}
