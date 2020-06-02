package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

public class ArticleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 信息类型
     */
    private String typeid;
    /**
     * 信息标题
     */
    private String title;
    /**
     * 信息内容,富文本格式
     */
    private String content;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 更新时间
     */
    private Date updateTime;
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


    public String getTypeId() {
        return typeid;
    }

    public void setTypeId(String typeid) {
        this.typeid = typeid;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
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


    public Integer getDeleted() {
        return deleted;
    }


    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

}
