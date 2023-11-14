package com.sallai.logview.controller;

import com.sallai.logview.module.vo.LogInfoVo;
import com.sallai.logview.service.LogService;
import com.sallai.logview.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.sallai.logview.support.http.ResourceServlet.SESSION_USER_KEY;

/**
 * @className LogViewController
 * @description TODO
 * @author sallai
 * @date 13:55 2023/10/29
 * @version 1.0
 **/
@RestController
@RequestMapping("/_api/logView")
@CrossOrigin(origins = "*")
public class LogViewController {

    @Autowired
    LogService logService;

    @GetMapping("/read/{line}")
    public R getLog(@PathVariable(value = "line") long line, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER_KEY) != null) {
            LogInfoVo logInfoVo = logService.readLogFile(line);
            return R.ok(logInfoVo);
        }
        return R.fail("鉴权失败！").setCode(201);
    }
}
