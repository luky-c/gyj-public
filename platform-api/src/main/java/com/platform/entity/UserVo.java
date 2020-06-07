package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 * 增加了社工和社工编号 2020-05-07
 * 增加了分类总积分 2020-05-24
 */
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long userId;
    //会员名称
    private String username;
    //会员密码
    private String password;
    //性别
    private Integer gender;
    //出生日期
    private Date birthday;
    //注册时间
    private Date register_time;
    //最后登录时间
    private Date last_login_time;
    //最后登录Ip
    private String last_login_ip;
    //会员等级
    private Integer user_level_id;
    //别名
    private String nickname;
    //手机号码
    private String mobile;
    //注册Ip
    private String register_ip;
    //头像
    private String avatar;
    //微信Id
    private String weixin_openid;
     //是否社工
    private Integer is_social_worker;
     //社工号码
    private String social_number;
    // 显示的可修改的ID
    private String id_for_show;
    /**
     * 完成任务获得总数量
     */
    private BigDecimal task_integral;
    /**
     * 捐赠获得总积分
     */
    private BigDecimal donation_integral;
    /**
     * 消费获得总积分
     */
    private BigDecimal deduction_integral;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public Integer getUser_level_id() {
        return user_level_id;
    }

    public void setUser_level_id(Integer user_level_id) {
        this.user_level_id = user_level_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegister_ip() {
        return register_ip;
    }

    public void setRegister_ip(String register_ip) {
        this.register_ip = register_ip;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }

    public Integer getIsSocialworker() {
        return is_social_worker;
    }

    public void setIsSocialworker(Integer is_social_worker) {
        this.is_social_worker = is_social_worker;
    }

    public String getSocialNumber() {
        return social_number;
    }

    public void setSocialNumber(String social_number) {
        this.social_number = social_number;
    }

    public void setTaskIntegral(BigDecimal task_integral) {
        this.task_integral = task_integral;
    }

    public BigDecimal getTaskIntegral() {
        return task_integral;
    }

    public void setDeductionIntegral(BigDecimal deduction_integral) {
        this.deduction_integral = deduction_integral;
    }

    public BigDecimal getDeductionIntegral() {
        return deduction_integral;
    }

    public void setDonationIntegral(BigDecimal donation_integral) {
        this.donation_integral = donation_integral;
    }

    public BigDecimal getDonationIntegral() {
        return donation_integral;
    }

    public String getIdForShow() {
        return id_for_show;
    }

    public void setIdForShow(String id_for_show) {
        this.id_for_show = id_for_show;
    }
}
