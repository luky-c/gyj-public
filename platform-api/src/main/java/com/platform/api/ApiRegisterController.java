package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.UserAccountVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserAccountService;
import com.platform.service.ApiUserService;
import com.platform.service.FabricService;
import com.platform.util.ApiBaseAction;
import com.platform.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 注册
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * @date 2017-03-26 17:27
 */
@Api(tags = "注册")
@RestController
@RequestMapping("/api/register")
public class ApiRegisterController  extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiUserAccountService userAccountService;
    @Autowired
    private FabricService fabricService;

    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("register")
    public Object register(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        userService.save(mobile, password);

        UserVo userVo = userService.queryByMobile(mobile);

        Date nowTime = new Date();

        UserAccountVo useraccountentity= new UserAccountVo();
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
        fabricService.save(useraccountentity);

        return toResponsSuccess("注册成功");
    }
}
