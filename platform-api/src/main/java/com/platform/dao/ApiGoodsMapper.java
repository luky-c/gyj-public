package com.platform.dao;

import com.platform.entity.GoodsVo;

import java.util.List;
import java.util.Map;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * @date 2017-08-11 09:16:45
 */
public interface ApiGoodsMapper extends BaseDao<GoodsVo> {

    List<GoodsVo> queryHotGoodsList(Map<String, Object> params);

    List<GoodsVo> queryCatalogProductList(Map<String, Object> params);

    List<GoodsVo> regularQuery(Map<String, Object> params);
}
