package com.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.platform.entity.UserAccountVo;
import com.platform.utils.RRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 为fabric区块链调用封装http方法
 */
@Service
public class FabricService {

    private ExecutorService poolExecutor = Executors.newFixedThreadPool(3);
    private final String IP = "http://localhost:8763";
//    private final String IP = "http://152.136.98.57:8763";
    private final String SAVE_URL = "/fabricSDK/saveUserAccount";
    private final String UPDATE_URL = "fabricSDK/updateUserAccount";

    @Autowired
    private RestTemplate restTemplate;

    public void save(UserAccountVo userAccountVo) {
        poolExecutor.execute(() -> {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String result = restTemplate.postForObject(IP + SAVE_URL, userAccountVo, String.class);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if((Integer)jsonObject.get("code") != 0) {
                throw new RRException("调用fabric api save出错， id为" + userAccountVo.getId());
            }
        });

    }

    public void update(UserAccountVo userAccountVo) {
        poolExecutor.execute(() -> {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String result = restTemplate.postForObject(IP  + UPDATE_URL, userAccountVo, String.class);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if((Integer)jsonObject.get("code") != 0) {
                throw new RRException("调用fabric api update出错， id为" + userAccountVo.getId());
            }
        });

    }
}
