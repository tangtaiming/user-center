package com.mingligu.utils;

import com.fasterxml.jackson.databind.JsonSerializable;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求返回结果处理
 * @author tangtaiming
 * @version 1.0
 * @date 2025年03月21日
 */
public class ResponseUtil {

    public static Map<String, Object> success(Object obj) {
        Map<String, Object> successMap = new HashMap<>();
        successMap.put("status", "ok");
        successMap.put("obj", obj);

        return successMap;
    }

}
