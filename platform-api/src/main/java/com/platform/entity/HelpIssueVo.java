package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HelpIssueVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 求助信息唯一序列号
     */
    private String SN;
    /**
     * 创建者用户ID，用户表的用户ID
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 市
     */
    private String cityName;
    /**
     * 区
     */
    private String countyName;
    /**
     * 详细收货地址信息
     */
    private String detailInfo;
    /**
     * 求助分类
     */
    private Integer typeId;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 募集金额
     */
    private BigDecimal collectionPrice;
    /**
     * 审批状态 ：4 未提交  0 待审批  1 审批通过 2 审批拒绝
     */
    private Integer approveStatus;
    /**
     * 审批内容
     */
    private String approveMsg;
    /**
     * 审批用户ID
     */
    private Long approveUserId;
    /**
     * 是否含有图片
     */
    private Integer hasPicture;
    /**
     * 图片地址列表，采用JSON数组格式
     */
    private String picUrls;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     *
     */
    private Integer isOnSale;
    /**
     *
     */
    private Integer isNew;
    /**
     *
     */
    private Integer isHot;
    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：序列号
     */
    public void setSN(String SN) {
        this.SN = SN;
    }

    /**
     * 获取：序列号
     */
    public String getSN() {
        return SN;
    }


    /**
     * 设置：会员Id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：会员Id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置：会员ID
     */
    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    /**
     * 获取：会员ID
     */
    public Long getApproveUserId() {
        return approveUserId;
    }


    /**
     * 设置：会员名称
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * 获取：会员名称
     */
    public String getUserName() {
        return username;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApproveMsg() {
        return approveMsg;
    }

    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    /**
     * 设置：省
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取：省
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * 设置：市
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 获取：市
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * 设置：区
     */
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    /**
     * 获取：区
     */
    public String getCountyName() {
        return countyName;
    }

    /**
     * 设置：详细收货地址信息
     */
    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    /**
     * 获取：详细收货地址信息
     */
    public String getDetailInfo() {
        return detailInfo;
    }


    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(Integer hasPicture) {
        this.hasPicture = hasPicture;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }


    public String getPicUrls() {
        return picUrls;
    }

    public void setCollectionPrice(BigDecimal collectionPrice) {
        this.collectionPrice = collectionPrice;
    }


    public BigDecimal getCollectionPrice() {
        return collectionPrice;
    }



    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Integer getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Integer isOnSale) { this.isOnSale =isOnSale; }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
