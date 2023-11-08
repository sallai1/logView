package com.sallai.logview.support.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LogViewServlet
 * @Description 日志查看servlet
 * @Author sallai
 * @Email sallai@aliyun.com
 * @Date 16:12 2023/11/5
 * @Version 1.0
 **/

public class LogViewServlet extends ResourceServlet{

    private static final Log LOG = LogFactory.getLog(LogViewServlet.class);

    private static final long serialVersionUID = 1L;

    public LogViewServlet() {
        super("static/logView");
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected String process(String url) {
        return null;
    }
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");
        // root context
        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/logView/index.html");
            } else {
                response.sendRedirect("logView/index.html");
            }
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        super.service(request, response);
    }

}
