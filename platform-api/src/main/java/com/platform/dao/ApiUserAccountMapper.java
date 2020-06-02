package com.platform.dao;

import com.platform.entity.RankVo;
import com.platform.entity.UserAccountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ApiUserAccountMapper extends BaseDao<UserAccountVo>{
    UserAccountVo queryListByUserid(@Param("user_id") Long user_id);
    List<RankVo> queryListByMonth();

    List<RankVo> queryListByWeek();
}
