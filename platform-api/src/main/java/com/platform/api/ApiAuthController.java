package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.FullUserInfo;
import com.platform.entity.UserAccountVo;
import com.platform.entity.UserInfo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserAccountService;
import com.platform.service.ApiUserService;
import com.platform.service.TokenService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiUserUtils;
import com.platform.util.CommonUtil;
import com.platform.utils.CharUtil;
import com.platform.utils.ResourceUtil;
import com.platform.validator.Assert;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * API登录授权
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * @date 2017-03-23 15:31
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApiUserAccountService userAccountService;



    /**
     * 登录APIUserAccount
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public Object login(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        //用户登录
        long userId = userService.login(mobile, password);

        UserVo userVo = userService.queryByMobile(mobile);

        //生成token
        Map<String, Object> map = tokenService.createToken(userId);

        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("nickName", userVo.getNickname());
        userInfo.put("avatarUrl", userVo.getAvatar());
        userInfo.put("gender", userVo.getGender());
        map.put("userInfo", userInfo);
        map.put("userRegTime", userVo.getRegister_time());
        map.put("userDonaIntegral", userVo.getDonationIntegral());
        map.put("userTaskIntegral", userVo.getTaskIntegral());
        map.put("userDeduIntegral", userVo.getDeductionIntegral());
        map.put("userMobile", userVo.getMobile());
        map.put("userId", userVo.getUserId());
        map.put("userlevelId", userVo.getUser_level_id());
        map.put("isSocialWorker", userVo.getIsSocialworker());
        map.put("socialNumber", userVo.getSocialNumber());
        map.put("idForShow", userVo.getIdForShow());

        return toResponsSuccess(map);
    }

    /**
     * 微信登录
     */
    @ApiOperation(value = "微信登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin() {
        JSONObject jsonParam = this.getJsonRequest();
        FullUserInfo fullUserInfo = null;
        String code = "";
        UserAccountVo useraccountentity= new UserAccountVo();
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        if (null != jsonParam.get("userInfo")) {
            fullUserInfo = jsonParam.getObject("userInfo", FullUserInfo.class);
        }
        if (null == fullUserInfo) {
            return toResponsFail("登录失败");
        }

        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        UserInfo userInfo = fullUserInfo.getUserInfo();

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》组合token为：" + requestUrl);
        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null == sessionData || StringUtils.isNullOrEmpty(sessionData.getString("openid"))) {
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性
        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
        if (!fullUserInfo.getSignature().equals(sha1)) {
            return toResponsFail("登录失败");
        }
        Date nowTime = new Date();
        UserVo userVo = userService.queryByOpenId(sessionData.getString("openid"));
        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUsername("微信用户" + CharUtil.getRandomString(12));
            userVo.setPassword(sessionData.getString("openid"));
            userVo.setRegister_time(nowTime);
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            //性别 0：未知、1：男、2：女
            userVo.setGender(userInfo.getGender());
            userVo.setNickname(userInfo.getNickName());
            userVo.setIsSocialworker(0);
            userVo.setSocialNumber("");
            userService.save(userVo);
            // 初次微信登录后就建立用户账户表  2020.5.7
            useraccountentity.setUserId(userVo.getUserId());
            useraccountentity.setIntegraltype("sign");
            useraccountentity.setTitle("初次签到");
            useraccountentity.setLinkId(0);
            useraccountentity.setAmount(new BigDecimal("1.0"));
            useraccountentity.setBalance(new BigDecimal("100.0"));
            useraccountentity.setCategory("integral");
            useraccountentity.setMark("首签获得");
            useraccountentity.setCreateTime(nowTime);
            useraccountentity.setModifyTime(nowTime);
            useraccountentity.setPm(1);
            useraccountentity.setStatus(1);
            useraccountentity.setSuccsign(1);
            userAccountService.save(useraccountentity);

        } else {
            userVo.setLast_login_ip(this.getClientIp());
            userVo.setLast_login_time(nowTime);
            userService.update(userVo);
        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (null == userInfo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        resultObj.put("token", token);
        resultObj.put("userInfo", userInfo);
        resultObj.put("userRegTime", userVo.getRegister_time());
        resultObj.put("userDonaIntegral", userVo.getDonationIntegral());
        resultObj.put("userTaskIntegral", userVo.getTaskIntegral());
        resultObj.put("userDeduIntegral", userVo.getDeductionIntegral());
        resultObj.put("userMobile", userVo.getMobile());
        resultObj.put("userId", userVo.getUserId());
        resultObj.put("userlevelId", userVo.getUser_level_id());
        resultObj.put("isSocialWorker", userVo.getIsSocialworker());
        resultObj.put("socialNumber", userVo.getSocialNumber());
        resultObj.put("idForShow", userVo.getIdForShow());
        return toResponsSuccess(resultObj);
    }

    /**
     * 支付宝登录
     */
    @ApiOperation(value = "支付宝登录")
    @IgnoreAuth
    @PostMapping("login_by_ali")
    public Object login_by_ali() {
        JSONObject jsonParam = this.getJsonRequest();
        UserAccountVo useraccountentity= new UserAccountVo();
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(ResourceUtil.getConfigByName("ali.webAccessTokenhttps"), ResourceUtil.getConfigByName("ali.appId"), ResourceUtil.getConfigByName("ali.privateKey"),
                "json", "UTF-8", ResourceUtil.getConfigByName("ali.pubKey"), "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        try {
            //code 换取token
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            String accessToken = oauthTokenResponse.getAccessToken();

            //根据token获取用户头像、昵称等信息
            AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse userInfoResponse = alipayClient.execute(userInfoShareRequest, accessToken);

            Date nowTime = new Date();
            UserVo userVo = userService.queryByOpenId(userInfoResponse.getUserId());
            if (null == userVo) {
                userVo = new UserVo();
                userVo.setUsername("支付宝用户" + CharUtil.getRandomString(12));
                userVo.setPassword(userInfoResponse.getUserId());
                userVo.setRegister_time(nowTime);
                userVo.setRegister_ip(this.getClientIp());
                userVo.setLast_login_ip(userVo.getRegister_ip());
                userVo.setLast_login_time(nowTime);
                userVo.setWeixin_openid(userInfoResponse.getUserId());
                userVo.setAvatar(userInfoResponse.getAvatar());
                //性别 0：未知、1：男、2：女
                //F：女性；M：男性
                userVo.setGender("m".equalsIgnoreCase(userInfoResponse.getGender()) ? 1 : 0);
                userVo.setNickname(userInfoResponse.getNickName());
                userVo.setIsSocialworker(0);
                userVo.setSocialNumber("");
                userService.save(userVo);
                // 初次微信登录后就建立用户账户表  2020.5.7
                useraccountentity.setUserId(userVo.getUserId());
                useraccountentity.setIntegraltype("sign");
                useraccountentity.setTitle("初次签到");
                useraccountentity.setLinkId(0);
                useraccountentity.setAmount(new BigDecimal("1.0"));
                useraccountentity.setBalance(new BigDecimal("100.0"));
                useraccountentity.setCategory("integral");
                useraccountentity.setMark("首签获得");
                useraccountentity.setCreateTime(nowTime);
                useraccountentity.setModifyTime(nowTime);
                useraccountentity.setPm(1);
                useraccountentity.setStatus(1);
                useraccountentity.setSuccsign(1);
                userAccountService.save(useraccountentity);

            } else {
                userVo.setLast_login_ip(this.getClientIp());
                userVo.setLast_login_time(nowTime);
                userService.update(userVo);
            }

            Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
            String token = MapUtils.getString(tokenMap, "token");

            if (StringUtils.isNullOrEmpty(token)) {
                return toResponsFail("登录失败");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();
            resultObj.put("token", token);
            resultObj.put("userInfo", userInfoResponse);
            resultObj.put("userId", userVo.getUserId());
            resultObj.put("userRegTime", userVo.getRegister_time());
            resultObj.put("userDonaIntegral", userVo.getDonationIntegral());
            resultObj.put("userTaskIntegral", userVo.getTaskIntegral());
            resultObj.put("userDeduIntegral", userVo.getDeductionIntegral());
            resultObj.put("userMobile", userVo.getMobile());
            resultObj.put("userlevelId", userVo.getUser_level_id());
            resultObj.put("isSocialWorker", userVo.getIsSocialworker());
            resultObj.put("socialNumber", userVo.getSocialNumber());
            resultObj.put("idForShow", userVo.getIdForShow());
            return toResponsSuccess(resultObj);
        } catch (AlipayApiException e) {
            return toResponsFail("登录失败");
        }
    }
}
