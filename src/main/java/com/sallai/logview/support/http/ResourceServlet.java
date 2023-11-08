package com.sallai.logview.support.http;

/**
 * @ClassName ResourceServlet
 * @Description TODO
 * @Author sallai
 * @Email sallai@aliyun.com
 * @Date 16:24 2023/11/5
 * @Version 1.0
 **/
import com.sallai.logview.utils.logView.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@SuppressWarnings("serial")
public abstract class ResourceServlet extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(ResourceServlet.class);

    public static final String SESSION_USER_KEY = "druid-user";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static final String PARAM_NAME_ALLOW = "allow";
    public static final String PARAM_NAME_DENY = "deny";
    public static final String PARAM_REMOTE_ADDR = "remoteAddress";

    protected final ResourceHandler handler;

    public ResourceServlet(String resourcePath) {
        handler = new ResourceHandler(resourcePath);
    }

    @Override
    public void init() throws ServletException {
        initAuthEnv();
    }

    private void initAuthEnv() {
        String paramUserName = getInitParameter(PARAM_NAME_USERNAME);
        if (StringUtils.hasText(paramUserName)) {
            handler.username = paramUserName;
        }

        String paramPassword = getInitParameter(PARAM_NAME_PASSWORD);
        if (StringUtils.hasText(paramPassword)) {
            handler.password = paramPassword;
        }

        String paramRemoteAddressHeader = getInitParameter(PARAM_REMOTE_ADDR);
        if (StringUtils.hasText(paramRemoteAddressHeader)) {
            handler.remoteAddressHeader = paramRemoteAddressHeader;
        }
    }

    public boolean isPermittedRequest(String remoteAddress) {
        return handler.isPermittedRequest(remoteAddress);
    }

    protected String getFilePath(String fileName) {
        return handler.resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException,
            IOException {
        handler.returnResourceFile(fileName, uri, response);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        handler.service(request, response, servletPath, new ProcessCallback() {
            @Override
            public String process(String url) {
                return ResourceServlet.this.process(url);
            }
        });
    }

    public boolean ContainsUser(HttpServletRequest request) {
        return handler.containsUser(request);
    }

    public boolean checkLoginParam(HttpServletRequest request) {
        return handler.checkLoginParam(request);
    }

    public boolean isRequireAuth() {
        return handler.isRequireAuth();
    }

    public boolean isPermittedRequest(HttpServletRequest request) {
        return handler.isPermittedRequest(request);
    }

    protected String getRemoteAddress(HttpServletRequest request) {
        return handler.getRemoteAddress(request);
    }

    protected abstract String process(String url);

    public static interface ProcessCallback {
        String process(String url);
    }

    public static class ResourceHandler {
        protected String username;
        protected String password;

        protected String resourcePath;

        protected String remoteAddressHeader;

        public ResourceHandler(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
                throws ServletException,
                IOException {
            String filePath = getFilePath(fileName);

            if (filePath.endsWith(".html")) {
                response.setContentType("text/html; charset=utf-8");
            }
            if (fileName.endsWith(".jpg")) {
                byte[] bytes = Utils.readByteArrayFromResource(filePath);
                if (bytes != null) {
                    response.getOutputStream().write(bytes);
                }
                return;
            }

            String text = Utils.readFromResource(filePath);
            if (text == null) {
                return;
            }

            if (fileName.endsWith(".css")) {
                response.setContentType("text/css;charset=utf-8");
            } else if (fileName.endsWith(".js")) {
                response.setContentType("text/javascript;charset=utf-8");
            }
            response.getWriter().write(text);
        }

        protected String getFilePath(String fileName) {
            return resourcePath + fileName;
        }

        public boolean checkLoginParam(HttpServletRequest request) {
            String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
            String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
            if (null == username || null == password) {
                return false;
            } else if (username.equals(usernameParam) && password.equals(passwordParam)) {
                return true;
            }
            return false;
        }

        protected String getRemoteAddress(HttpServletRequest request) {
            String remoteAddress = null;

            if (remoteAddressHeader != null) {
                remoteAddress = request.getHeader(remoteAddressHeader);
            }

            if (remoteAddress == null) {
                remoteAddress = request.getRemoteAddr();
            }

            return remoteAddress;
        }

        public boolean containsUser(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute(SESSION_USER_KEY) != null;
        }

        public boolean isRequireAuth() {
            return username != null;
        }

        public boolean isPermittedRequest(HttpServletRequest request) {
            String remoteAddress = getRemoteAddress(request);
            return isPermittedRequest(remoteAddress);
        }

        public boolean isPermittedRequest(String remoteAddress) {
            return true;
//            boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(':') != -1;
//
//            if (ipV6) {
//                return "0:0:0:0:0:0:0:1".equals(remoteAddress) || (denyList.isEmpty() && allowList.isEmpty());
//            }
//
//            IPAddress ipAddress = new IPAddress(remoteAddress);
//
//            for (IPRange range : denyList) {
//                if (range.isIPAddressInRange(ipAddress)) {
//                    return false;
//                }
//            }
//
//            if (allowList.size() > 0) {
//                for (IPRange range : allowList) {
//                    if (range.isIPAddressInRange(ipAddress)) {
//                        return true;
//                    }
//                }
//
//                return false;
//            }
//
//            return true;
        }

        public void service(
                HttpServletRequest request,
                HttpServletResponse response,
                String servletPath,
                ProcessCallback processCallback
        ) throws ServletException, IOException {
            String contextPath = request.getContextPath();
            String requestURI = request.getRequestURI();

            response.setCharacterEncoding("utf-8");

            if (contextPath == null) { // root context
                contextPath = "";
            }
            String uri = contextPath + servletPath;
            String path = requestURI.substring(contextPath.length() + servletPath.length());

            if (!isPermittedRequest(request)) {
                path = "/nopermit.html";
                returnResourceFile(path, uri, response);
                return;
            }

            if ("/user/login".equals(path)) {
                String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
                String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
                if (username.equals(usernameParam) && password.equals(passwordParam)) {
                    request.getSession().setAttribute(SESSION_USER_KEY, username);
                    response.getWriter().print("success");
                } else {
                    response.getWriter().print("error");
                }
                return;
            }

            if (
                    isRequireAuth() //
                    && !containsUser(request)//
                    && !checkLoginParam(request)//
                    && !("/index.html".equals(path) //
                    || path.startsWith("/static/css")//
                    || path.startsWith("/static/js") //
                    || path.startsWith("/static/img"))) {
                if (contextPath.equals("") || contextPath.equals("/")) {
                    response.sendRedirect("/logView/index.html");
                } else {
                    if ("".equals(path)) {
                        response.sendRedirect("logView/index.html");
                    } else {
                        response.sendRedirect("index.html");
                    }
                }
                return;
            }

            if ("".equals(path) || "/".equals(path)) {
                returnResourceFile("/index.html", uri, response);
                return;
            }

            if (path.contains(".json")) {
                String fullUrl = path;
                if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                    fullUrl += "?" + request.getQueryString();
                }
                response.getWriter().print(processCallback.process(fullUrl));
                return;
            }

            // find file in resources path

            returnResourceFile(path, uri, response);
        }

    }
}