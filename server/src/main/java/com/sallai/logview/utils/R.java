package com.sallai.logview.utils;

import com.alibaba.fastjson2.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * @className R
 * @description TODO
 * @author sallai
 * @date 22:06 2023/10/29
 * @version 1.0
 **/
@Data
@Builder
public class R {

    private Object data;
    private int code;
    private String msg;

    public R setCode(int code) {
        this.code = code;
        return this;
    }

    public R setData(Object data) {
        this.data = data;
        return this;
    }

    public R setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public static R ok(Object data){
        return R.builder().code(200).msg("请求成功").data(data).build();
    }

    public static R fail(String msg){
        return R.builder().code(202).msg(StringUtils.hasText(msg)?msg:"请求失败").build();
    }

    public String toJson() {
       return JSON.toJSONString(this);
    }
}
