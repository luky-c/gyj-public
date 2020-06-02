package com.platform.api;


import com.platform.annotation.IgnoreAuth;
import com.platform.entity.ArticleVo;
import com.platform.service.ApiArticleService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Ariticle
 *2020.5.5 by jun
 *
 */



@Api(tags = "公告信息")
@RestController
@RequestMapping("/api/article")
public class ApiArticleController extends ApiBaseAction {
    @Autowired
    private ApiArticleService articleService;

    /**
     * 查看按类型的列表
     *
     * @param  typeid
     * @return R
     */
    @RequestMapping("/list")
    @IgnoreAuth
    public Object list(String typeid) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        Map params = new HashMap();
        params.put("typeid", typeid);
        List<ArticleVo> articleList = articleService.queryList(params);
        resultObj.put("articleList", articleList);

        return toResponsSuccess(resultObj);
    }
    /**
     * 查看所有文章列表，
     */
    @RequestMapping("/listById")
    @IgnoreAuth
    public Object listById(Integer id) {
        Map<String, Object> resultObj = new HashMap<String, Object>();

        ArticleVo articleList = articleService.queryObject(id);
        resultObj.put("articleList", articleList);

        return toResponsSuccess(resultObj);
    }

}
