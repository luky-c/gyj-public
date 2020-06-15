package com.platform.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.util.ApiBaseAction;
import com.platform.util.CommonUtil;
import com.platform.utils.ResourceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Api(tags = "二维码")
@RestController
@RequestMapping("/api/code")
public class ApiCodeController extends ApiBaseAction {

    @Autowired
    private RestTemplate restTemplate;

    private String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ ResourceUtil.getConfigByName("wx.appId")+"&secret="+ResourceUtil.getConfigByName("wx.secret");

    @ApiOperation(value = "生成二维码图片")
    @PostMapping("erweima")
    @IgnoreAuth
    public Object erweima(@LoginUser UserVo loginUser, @RequestParam(defaultValue = "") String urlToCreate){
        JSONObject sessionData = CommonUtil.httpsRequest(url, "GET", null);
        String token = sessionData.getString("access_token");
        String newUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HashMap<String, String> map= new HashMap<>();
        map.put("scene","gyj");
        if (urlToCreate.length() != 0) {
            map.put("page", urlToCreate);
        }
        String json = JSON.toJSONString(map);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( newUrl, request , String.class );
        return response;
    }
}
