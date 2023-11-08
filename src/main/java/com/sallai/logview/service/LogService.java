package com.sallai.logview.service;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RolloverFailure;
import com.sallai.logview.module.vo.LogInfoVo;
import com.sallai.logview.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName LogService
 * @Description TODO
 * @Author sallai
 * @Date 13:01 2023/10/29
 * @Version 1.0
 **/
@Service
@Slf4j
public class LogService {
//    @Value("${logging.file.name}")
//    private String logFile;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private LogFile logFile;


    public LogInfoVo readLogFile(long line) {
        // 获取日志文件
        log.info(logFile.toString());
        String logFileStr = logFile.toString();
        LogInfoVo logInfoVo = LogInfoVo.builder().build();
        if(!StringUtils.hasText(logFileStr)) {
            return logInfoVo;
        }
        //读取log文件
        File file = new File(logFileStr);
        if(!file.exists()) {
            return logInfoVo;
        }
        StringBuilder logContent = new StringBuilder();

        try {
            ReversedLinesFileReader fileReader = new ReversedLinesFileReader(file);
            List<String> list = new ArrayList<>();
            for(int i=0;i < line;i++){
                String str = fileReader.readLine();
                if(str==null) {
                    break;
                }
                list.add(str);
            }
            for (int i = list.size()-1; i >= 0 ; i--) {
                logContent.append(list.get(i)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInfoVo.setContent(logContent.toString());
        logInfoVo.setName(file.getName());
        logInfoVo.setSize(FileUtil.formatFileSize(file));
        return logInfoVo;
    }
}
