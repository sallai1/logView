package com.sallai.logview.configuration;

import com.sallai.logview.controller.LogViewController;
import com.sallai.logview.property.LogViewProperty;
import com.sallai.logview.service.LogService;
import com.sallai.logview.support.http.LogViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @className LogViewAutoConfigure
 * @description TODO
 * @author sallai
 * @date 15:38 2023/11/5
 * @version 1.0
 **/
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = {LogViewProperty.class})
@ConditionalOnProperty(name = "logview.enable", havingValue = "true")
@ConditionalOnClass(value = {LogFile.class})
@ComponentScan(basePackageClasses = {LogViewController.class, LogService.class} )
public class LogViewAutoConfigure {
    private static final String DEFAULT_ALLOW_IP = "127.0.0.1";

    @Bean
    public ServletRegistrationBean<LogViewServlet> logViewServletRegistrationBean(LogViewProperty logViewProperty) {
        ServletRegistrationBean<LogViewServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new LogViewServlet());
        registrationBean.addUrlMappings(logViewProperty.getUrlPattern() != null ? logViewProperty.getUrlPattern() : "/logView/*");

        if (logViewProperty.getLoginName() != null) {
            registrationBean.addInitParameter("loginUsername", logViewProperty.getLoginName());
        }
        if (logViewProperty.getLoginPassword() != null) {
            registrationBean.addInitParameter("loginPassword", logViewProperty.getLoginPassword());
        }
        return registrationBean;
    }
}
