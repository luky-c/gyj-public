package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class RankVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户表的用户ID
     */
    private Long userId;
    /**
     * 总额
     */
    private BigDecimal sumBalance;

    private String nickname;

    //头像
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setSumBalance(BigDecimal sumBalance) {
        this.sumBalance = sumBalance;
    }

    public BigDecimal getSumBalance() {
        return sumBalance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
