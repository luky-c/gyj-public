package com.platform.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户表实体
 *
 * @date 2020-05-05 18:58:27
 */

public class UserAccountVo implements Serializable {
    private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
        private Integer id;
        /**
         * 积分所得事由
         */
        private String title;
        /**
         * 用户表的用户ID
         */
        private Long userId;
        /**
         * 相关事件ID
         */
        private Integer linkId;
        /**
         * 分类：积分和现金
         */
        private String category;
        /**
         * 类型：签到、佣金和执行任务
         */
        private String integraltype;
        /**
         * 获得数量
         */
        private BigDecimal amount;
        /**
         * 总额
         */
        private BigDecimal balance;
        /**
         * 创建时间
         */
        private Date createTime;
        /**
         * 修改时间
         */
        private Date modifyTime;
        /**
         * 结算利率：5 表示5%或0.05
         */
        private Integer settlementRate;
        /**
         * 账户状态
         */
        private Integer status;
        /**
         * 备注
         */
        private String mark;
        /**
         * 0:消费 1：获得
         */
        private Integer pm;
        /**
         * 连续签到天数
         */
        private Integer succsign;
        



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIntegraltype() { return integraltype; }

    public void setIntegraltype(String integraltype) {
        this.integraltype = integraltype;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Integer getSettlementRate() {
        return settlementRate;
    }

    public void setSettlementRate(Integer settlementRate) {
        this.settlementRate = settlementRate;
    }

    public String getMark() { return mark; }

    public void setMark(String mark) { this.mark = mark; }

     public Integer getPm() {
        return pm;
    }

    public void setPm(Integer pm) {
        this.pm = pm;
    }

     public Integer getSuccsign() {
        return succsign;
    }

    public void setSuccsign(Integer succsign) {
        this.succsign = succsign;
    }
}
