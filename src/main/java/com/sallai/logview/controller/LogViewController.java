package com.sallai.logview.controller;

import com.sallai.logview.module.vo.LogInfoVo;
import com.sallai.logview.service.LogService;
import com.sallai.logview.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName LogViewController
 * @Description TODO
 * @Author sallai
 * @Date 13:55 2023/10/29
 * @Version 1.0
 **/
@RestController
@RequestMapping("/_api/logView")
@CrossOrigin(origins = "*")
public class LogViewController {

    @Autowired
    LogService logService;

    @GetMapping("/read/{line}")
    public R getLog(@PathVariable(value = "line") long line) {
        LogInfoVo logInfoVo = logService.readLogFile(line);
        return R.ok(logInfoVo);
    }
}
