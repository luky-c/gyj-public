package com.platform.service;


import com.platform.dao.ApiUserAccountMapper;
import com.platform.entity.GoodsVo;
import com.platform.entity.RankVo;
import com.platform.entity.UserAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiUserAccountService {

    @Autowired
    private ApiUserAccountMapper userAccountDao;

    @Autowired
    private FabricService fabricService;

    public UserAccountVo queryObject(Integer id) {
        return userAccountDao.queryObject(id);
    }

    public List<UserAccountVo> queryList(Map<String, Object> map) {
        return userAccountDao.queryList(map);
    }

    public UserAccountVo queryListByUserid(Long user_id) {
        return userAccountDao.queryListByUserid(user_id);
    }

    public int queryTotal(Map<String, Object> map) {
        return userAccountDao.queryTotal(map);
    }

    public int save(UserAccountVo userAccount) {
        int result = userAccountDao.save(userAccount);
        fabricService.save(userAccount);
        return result;
    }

    public int update(UserAccountVo userAccount) {
        int result = userAccountDao.update(userAccount);
        fabricService.update(userAccount);
        return result;
    }

    public int delete(Integer id) {
        return userAccountDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return userAccountDao.deleteBatch(ids);
    }

    public List<RankVo> queryListByMonth() {
        return userAccountDao.queryListByMonth();
    }

    public List<RankVo> queryListByWeek() { return userAccountDao.queryListByWeek();
    }
}
