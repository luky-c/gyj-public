package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiUserLevelMapper;
import com.platform.entity.*;
import com.platform.service.ApiUserAccountService;
import com.platform.service.ApiUserService;
import com.platform.service.SysConfigService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * 描述: ApiIndexController <br>
 */
@Api(tags = "会员验证")
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ApiUserAccountService userAccountService;
    @Autowired
    private ApiUserLevelMapper userLevelDao;
    /**
     * 发送短信
     */
    @ApiOperation(value = "发送短信")
    @PostMapping("smscode")
    public Object smscode(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        String phone = jsonParams.getString("phone");
        // 一分钟之内不能重复发送短信
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());
        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLog_date()) < 1 * 60) {
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            return toResponsFail("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getAppid())) {
            return toResponsFail("请先配置短信平台APPID");
        }
        if (StringUtils.isNullOrEmpty(config.getAppkey())) {
            return toResponsFail("请先配置短信平台KEY");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
            return toResponsFail("请先配置短信平台签名");
        }
        // 发送短信
        SmsSingleSenderResult result;
        int templateId = 612976;
        try {
            result = SmsUtil.crSendSms(config.getAppid(), config.getAppkey(), phone, templateId, new String[]{sms_code}, "");
        } catch (Exception e) {
            return toResponsFail("短信发送失败");
        }

        if (result.result == 0) {
            smsLogVo = new SmsLogVo();
            smsLogVo.setLog_date(System.currentTimeMillis() / 1000);
            smsLogVo.setUser_id(loginUser.getUserId());
            smsLogVo.setPhone(phone);
            smsLogVo.setSms_code(templateId);
            smsLogVo.setSms_text(sms_code);
            userService.saveSmsCodeLog(smsLogVo);
            return toResponsSuccess("短信发送成功");
        } else {
            return toResponsFail("短信发送失败");
        }
    }

    /**
     * 获取月排名
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取月排名")
    @PostMapping("getRankbyMonth")
    public Object getRankbyMonth(@LoginUser UserVo loginUser,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        List<RankVo>  rankVoList= userAccountService.queryListByMonth();
        for (int i=0;i <rankVoList.size();i++){
            if(loginUser.getUserId() == rankVoList.get(i).getUserId()){
               int position=i+1;
               resultObj.put("position", position);
            }
        }
        int len = rankVoList.size();
        if ((page-1) * size>= len){
            rankVoList = new ArrayList<>();
        }else {
            rankVoList = rankVoList.subList(page*size,page*size+10);
        }
        resultObj.put("total",len);
        resultObj.put("userRankbyMonth", rankVoList);
        return toResponsSuccess(resultObj);
    }

    /**
     * 获取周排名
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取周排名")
    @PostMapping("getRankbyWeek")
    public Object getRankbyWeek(@LoginUser UserVo loginUser,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        List<RankVo>  rankVoList= userAccountService.queryListByWeek();
        for (int i=0;i <rankVoList.size();i++){
            if(loginUser.getUserId()==rankVoList.get(i).getUserId()){
                int position=i+1;
                resultObj.put("position", position);
            }
        }
        int len = rankVoList.size();
        if ((page-1) * size>= len){
            rankVoList = new ArrayList<>();
        }else {
            rankVoList = rankVoList.subList(page*size,page*size+10);
        }
        resultObj.put("total",len);
        resultObj.put("userRankbyWeek", rankVoList);
        return toResponsSuccess(resultObj);
    }

    /**
     * 获取当前会员等级
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "获取当前会员等级")
    @PostMapping("getUserLevel")
    public Object getUserLevel(@LoginUser UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        String userLevel = userService.getUserLevel(loginUser);
        resultObj.put("userLevel", userLevel);
        return toResponsSuccess(resultObj);
    }
    /**
     * 获取会员等级列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取会员等级列表")
    @PostMapping("getUserLevelList")
    public Object getUserLevelList() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        List<UserLevelVo> userLevelList = userLevelDao.queryList();
        resultObj.put("userLevelList", userLevelList);
        return toResponsSuccess(resultObj);
    }


    /**
     * 获取当前会员积分    
     * 2020.05.05
     * @param loginUser,type_id
     * @return
     */
    @ApiOperation(value = "获取当前会员不同类型所获积分")
	@RequestMapping(value="/getIntegratebyTypeId")
	@ResponseBody
	public Object getIntegratebyTypeId(@LoginUser UserVo loginUser, String type_id) throws Exception {
        Date nowTime = new Date();
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UserAccountVo useraccountentity= new UserAccountVo();
		Map params = new HashMap();
        params.put("userId", loginUser.getUserId());
        params.put("integraltype", type_id);


        List<UserAccountVo> userAccountList = userAccountService.queryList(params);
        if(null==userAccountList){
            useraccountentity.setUserId(loginUser.getUserId());
            useraccountentity.setIntegralType("sign");
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
            useraccountentity.setSuccSign(1);
            userAccountService.save(useraccountentity);
            List<UserAccountVo> userAccountList1 = userAccountService.queryList(params);
            for (UserAccountVo userAccountVo : userAccountList1) {
                BigDecimal amount1= userAccountVo.getAmount();
                Integer sum_sign_day = amount1.intValue();
                resultObj.put("userAccountList", userAccountList1);
                resultObj.put("sign_info", sum_sign_day);
            }
            return toResponsSuccess(resultObj);
        }
            for (UserAccountVo userAccountVo : userAccountList) {
            BigDecimal amount1= userAccountVo.getAmount();
            Integer sum_sign_day = amount1.intValue();
            resultObj.put("userAccountList", userAccountList);
            resultObj.put("sign_info", sum_sign_day);
            }
            return toResponsSuccess(resultObj);
	}

    /**
     * 捐赠
     * 2020.05.05
     * @param loginUser,balance
     * @return
     */
    @ApiOperation(value = "捐赠")
    @RequestMapping(value="/donation")
    @ResponseBody
    public Object donation(@LoginUser UserVo loginUser, String balance) throws Exception {
            Date nowTime = new Date();
            UserAccountVo useraccountentity= new UserAccountVo();
            UserVo userentity = new UserVo();
            BigDecimal balan = new BigDecimal(balance);

            Map params = new HashMap();
            params.put("userId", loginUser.getUserId());
            params.put("integraltype", "donation");
            params.put("sidx", "id");
            params.put("order", "desc");
            List<UserAccountVo> userAccountDonation = userAccountService.queryList(params);
            if (null!=userAccountDonation){
                BigDecimal num1 =userAccountDonation.get(0).getAmount();
                BigDecimal balance1 = balan.add(num1);
                useraccountentity.setUserId(loginUser.getUserId());
                useraccountentity.setIntegralType("donation");
                useraccountentity.setTitle("爱心捐赠");
                useraccountentity.setLinkId(0);
                useraccountentity.setAmount(balance1);
                useraccountentity.setBalance(balan);
                useraccountentity.setCategory("integral");
                useraccountentity.setMark("捐赠获得");
                useraccountentity.setCreateTime(nowTime);
                useraccountentity.setModifyTime(nowTime);
                useraccountentity.setPm(1);
                useraccountentity.setStatus(1);
                useraccountentity.setSuccSign(1);
                userAccountService.save(useraccountentity);
                //update donation_integral for user
                userentity.setUserId(loginUser.getUserId());
                userentity.setDonationIntegral(balance1);
                userService.update(userentity);
            } else {
                BigDecimal balance1 = balan;
                useraccountentity.setUserId(loginUser.getUserId());
                useraccountentity.setIntegralType("donation");
                useraccountentity.setTitle("爱心捐赠");
                useraccountentity.setLinkId(0);
                useraccountentity.setAmount(balance1);
                useraccountentity.setBalance(balan);
                useraccountentity.setCategory("integral");
                useraccountentity.setMark("捐赠获得");
                useraccountentity.setCreateTime(nowTime);
                useraccountentity.setModifyTime(nowTime);
                useraccountentity.setPm(1);
                useraccountentity.setStatus(1);
                useraccountentity.setSuccSign(1);
                userAccountService.save(useraccountentity);
                //update donation_integral for user
                userentity.setUserId(loginUser.getUserId());
                userentity.setDonationIntegral(balance1);
                userService.update(userentity);
            }
            return toResponsSuccess("捐赠记录发送成功");
    }
	/**
     * 获取当前会员积分详情       
     * 2020.05.05
     * @param loginUser
     * @return
     */
	@ApiOperation(value = "获取当前会员积分详情")
	@RequestMapping(value="/getIntegrateDetail")
	@ResponseBody
	public Object getIntegrateDetail(@LoginUser UserVo loginUser) throws Exception {
        Map<String, Object> resultObj = new HashMap<String, Object>();
		Map params = new HashMap();
        params.put("userId", loginUser.getUserId());
        List<UserAccountVo> userAccountList = userAccountService.queryList(params);
        UserVo userEn = userService.queryObject(loginUser.getUserId());

        if (null!=userAccountList){
            resultObj.put("userAccountList", userAccountList);}

        Map params1 = new HashMap();
        params1.put("userId", loginUser.getUserId());
        params1.put("integraltype", "sign");
        params1.put("sidx", "id");
        params1.put("order", "desc");
        List<UserAccountVo> userAccountsign = userAccountService.queryList(params1);
        if (null!=userAccountsign){
        BigDecimal balance = userAccountsign.get(0).getBalance();
        resultObj.put("sign_integral", balance);}

        resultObj.put("task_integral", userEn.getTaskIntegral());
        resultObj.put("deduction_integral", userEn.getDeductionIntegral());
        resultObj.put("donation_integral", userEn.getDonationIntegral());

        /* 直接从积分详情提取积分数据
        Map params2 = new HashMap();
        params2.put("userId", loginUser.getUserId());
        params2.put("integraltype", "task");
        params2.put("sidx", "id");
        params2.put("order", "desc");
        List<UserAccountVo> userAccountTask = userAccountService.queryList(params2);
        if (null!=userAccountTask){
        BigDecimal num =userAccountTask.get(0).getAmount();
        resultObj.put("task_integral", num);}

        Map params3 = new HashMap();
        params3.put("userId", loginUser.getUserId());
        params3.put("integraltype", "donation");
        params3.put("sidx", "id");
        params3.put("order", "desc");
        List<UserAccountVo> userAccountDonation = userAccountService.queryList(params3);
        if (null!=userAccountDonation){
        BigDecimal num1 =userAccountDonation.get(0).getAmount();
        resultObj.put("donation_integral", num1);}

        Map params4 = new HashMap();
        params4.put("userId", loginUser.getUserId());
        params4.put("integraltype", "occ");
        params4.put("sidx", "id");
        params4.put("order", "desc");
        List<UserAccountVo> userAccountDedu = userAccountService.queryList(params4);
        if (userAccountDedu.size()>0){
        BigDecimal num2 =userAccountDedu.get(0).getAmount();
        resultObj.put("deduction_integral", num2);} */
        return toResponsSuccess(resultObj);
	}
	
	/**
	 * 今天签到
     * 
	 */
    @ApiOperation(value = "当天签到")
	@RequestMapping(value="/signToday")
	@ResponseBody
	public Object signToday(@LoginUser UserVo loginUser,String integral,Integer succ_record) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map resultObj = new HashMap();
        Map params = new HashMap();
        BigDecimal balan = new BigDecimal(integral);
        UserAccountVo useraccountentity= new UserAccountVo();
        params.put("userId", loginUser.getUserId());
        params.put("integraltype", "sign");
        params.put("sidx", "id");
        params.put("order", "asc");

		try {
			//查询当前用户最后签到时间和连续签到多少天
			List<UserAccountVo> list = userAccountService.queryList(params);

            if(list.size()>0){
				Date today = new Date();
                BigDecimal num = list.get(0).getAmount();
                BigDecimal balance = list.get(0).getBalance();
                BigDecimal num1=num.add(new BigDecimal("1.0"));
                BigDecimal balance1 = balance.add(balan);
                useraccountentity.setSuccSign(succ_record);
                useraccountentity.setAmount(num1);
                useraccountentity.setBalance(balance1);
                useraccountentity.setModifyTime(today);
                useraccountentity.setId(list.get(0).getId());
			}
			    userAccountService.update(useraccountentity);
			    return toResponsObject(0, "今天签到成功", resultObj);
		} catch (Exception e) {
			    return toResponsObject(0, "今天签到失败", resultObj);
		}
	}
	

    /**
     * 绑定手机
     */
    @ApiOperation(value = "绑定手机")
    @PostMapping("bindMobile")
    public Object bindMobile(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");

        if (!mobile_code.equals(smsLogVo.getSms_code())) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = userService.queryObject(loginUser.getUserId());
        userVo.setMobile(mobile);
        userService.update(userVo);
        return toResponsSuccess("手机绑定成功");
    }
}
