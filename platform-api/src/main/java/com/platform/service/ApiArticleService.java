package com.platform.service;


import com.platform.dao.ApiArticleMapper;
import com.platform.entity.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiArticleService {

    @Autowired
    private ApiArticleMapper articleDao;

    public ArticleVo queryObject(Integer id) {
        return articleDao.queryObject(id);
    }

    public List<ArticleVo> queryList(Map<String, Object> map) {
        return articleDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return articleDao.queryTotal(map);
    }

    public int save(ArticleVo article) {
        return articleDao.save(article);
    }

    public int update(ArticleVo article) {
        return articleDao.update(article);
    }

    public int delete(Integer id) {
        return articleDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return articleDao.deleteBatch(ids);
    }
}
