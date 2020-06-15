package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.HelpIssueVo;
import com.platform.entity.HelpTypeVo;
import com.platform.entity.UserAccountVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.utils.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller
 *
 *
 */
@Api(tags = "求助信息")
@RestController
@RequestMapping("/api/helpissue")
public class ApiHelpIssueController extends ApiBaseAction {
    @Autowired
    private ApiHelpIssueService helpIssueService;
    @Autowired
    private ApiHelpTypeService helpTypeService;
    @Autowired
    private ApiUserAccountService userAccountService;
    @Autowired
    private ApiUserService userService;


    /**
     * 查看帮助类型列表
     */
    @RequestMapping("/typeList")
    @IgnoreAuth
    public Object typeList() {

        List<HelpTypeVo> list = helpTypeService.queryList(new HashMap());

        return toResponsSuccess(list);
    }
    /**
     * 查看不同种类所有问题列表
     */
    @RequestMapping("/issueListbyId")
    @IgnoreAuth
    public Object issueListbyId(Integer id) {

        Map resultObj = new HashMap();
        HelpIssueVo helpIssueList = helpIssueService.queryObject(id);

        if (null != helpIssueList ) {
            helpIssueList.setContent(Base64Util.decode(helpIssueList.getContent()));
            if(null != helpIssueList.getApproveMsg()){
            helpIssueList.setApproveMsg(Base64Util.decode(helpIssueList.getApproveMsg()));}
            Integer typeId = helpIssueList.getTypeId();
            HelpTypeVo list = helpTypeService.queryObject(typeId);
            resultObj.put("userIssueList", helpIssueList);
            resultObj.put("userIssueType", list);
        }
        return toResponsSuccess(resultObj);

    }

    /**
     * 查看不同种类所有问题列表
     */
    @ApiOperation(value = "求助信息列表")
    @RequestMapping("/issueList")
    public Object issueList(@LoginUser UserVo loginUser,Integer type_id) {
        if (null == loginUser || null == loginUser.getUserId()) {
            return toResponsFail("未登录");
        }
        Map params = new HashMap();
        params.put("typeId", type_id);
        params.put("userId", loginUser.getUserId());
        List<HelpIssueVo> helpIssueList = helpIssueService.queryList(params);
        List<Map<String, Object>> newIssueList = new ArrayList<>();
        if (null != helpIssueList && helpIssueList.size() > 0) {
            for (HelpIssueVo IssueListVo : helpIssueList) {
                IssueListVo.setContent(Base64Util.decode(IssueListVo.getContent()));
                Integer typeId = IssueListVo.getTypeId();
                HelpTypeVo list = helpTypeService.queryObject(typeId);
                Map<String, Object> newIssue = new HashMap<String, Object>();
                newIssue.put("userIssueVo",  IssueListVo);
                newIssue.put("typeName", list.getTypeName());
                newIssueList.add(newIssue);
            }
        }
        return toResponsSuccess(newIssueList);
    }
    /**
     * 查看问题列表分类，按不同区域、是否审核分类查询
     */
    @ApiOperation(value = "求助信息分类查询")
    @RequestMapping("/issueListByStatus")
    @IgnoreAuth
    public Object issueListByStatus(Long type_id,Integer approve,String provinceName,String cityName,String countyName) {
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map params = new HashMap();
        params.put("typeId", type_id);
        params.put("cityName", cityName);
        params.put("provinceName", provinceName);
        params.put("countyName", countyName);
        params.put("approveStatus", approve.toString());

        List<HelpIssueVo> helpIssueList = helpIssueService.queryList(params);
        List<Map<String, Object>> newIssueList = new ArrayList<>();
        if (null != helpIssueList && helpIssueList.size() > 0) {
            for (HelpIssueVo IssueListVo : helpIssueList) {
                IssueListVo.setContent(Base64Util.decode(IssueListVo.getContent()));
                Integer typeId = IssueListVo.getTypeId();
                HelpTypeVo list = helpTypeService.queryObject(typeId);
                Map<String, Object> newIssue = new HashMap<String, Object>();
                newIssue.put("userIssueVo",  IssueListVo);
                newIssue.put("typeName", list.getTypeName());
                newIssueList.add(newIssue);
            }
        }

        return toResponsSuccess(newIssueList);
    }



    /**
     * 发表求助信息
     */
    @ApiOperation(value = "发表求助信息")
    @PostMapping("add")
    public Object add(@LoginUser UserVo loginUser) {
        Map resultObj = new HashMap();
        //
        HelpIssueVo helpissueEntity = new HelpIssueVo();
        JSONObject jsonParam = this.getJsonRequest();
        // 生成求助信息序列号
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String issueno = dateFormat.format(new Date());
        String str = "000000" + (int) (Math.random() * 1000000);
        issueno = issueno + str.substring(str.length() - 6);

        if (null != jsonParam) {
            Integer typeId = jsonParam.getIntValue("typeId");
            String userName = jsonParam.getString("userName");
            String mobile = jsonParam.getString("mobile");
            String provinceName = jsonParam.getString("provinceName");
            String cityName = jsonParam.getString("cityName");
            String countyName = jsonParam.getString("countyName");
            String detailInfo = jsonParam.getString("detailInfo");
            String content = jsonParam.getString("content");
            BigDecimal collectionPrice = jsonParam.getBigDecimal("collectionPrice");

            //
            helpissueEntity.setTypeId(typeId);
            helpissueEntity.setUserName(userName);
            helpissueEntity.setMobile(mobile);
            helpissueEntity.setProvinceName(provinceName);
            helpissueEntity.setCityName(cityName);
            helpissueEntity.setCountyName(countyName);
            helpissueEntity.setDetailInfo(detailInfo);
            helpissueEntity.setCollectionPrice(collectionPrice);
            helpissueEntity.setContent(content);

            helpissueEntity.setContent(Base64Util.encode(helpissueEntity.getContent()));
            helpissueEntity.setApproveStatus(0);
            helpissueEntity.setIsOnSale(0);
            //
            helpissueEntity.setAddTime(new Date());
            helpissueEntity.setUserId(loginUser.getUserId());
            helpissueEntity.setSN(issueno);
            helpIssueService.save(helpissueEntity);
            return toResponsObject(0, "求助添加成功", resultObj);
        } else {

            return toResponsFail("求助保存失败");
        }
        //如果有图片进行下面的处理
            /*if (insertId > 0 && null != imagesList && imagesList.size() > 0) {
                int i = 0;
                for (Object imgLink : imagesList) {
                    i++;
                    CommentPictureVo pictureVo = new CommentPictureVo();
                    pictureVo.setComment_id(insertId);
                    pictureVo.setPic_url(imgLink.toString());
                    pictureVo.setSort_order(i);
                    commentPictureService.save(pictureVo);
                }
            } */

    }


    /**
     * 求助信息审核
     *
     */
    @ApiOperation(value = "求助信息审核")
    @PostMapping("check")
    public Object check(Integer id, @LoginUser UserVo loginUser,Integer approveStatus,String approveMsg) {
            Map resultObj = new HashMap();
            Date nowTime = new Date();
            //
            UserVo userEntity = new UserVo();
            UserVo userVo = userService.queryObject(loginUser.getUserId());
            BigDecimal taskintegral = userVo.getTaskIntegral();

            HelpIssueVo helpissueEntity = new HelpIssueVo();

            helpissueEntity.setId(id);
            helpissueEntity.setApproveMsg(approveMsg);
            helpissueEntity.setApproveStatus(approveStatus);
            if (approveStatus == 1) {
                helpissueEntity.setIsOnSale(1);
                helpissueEntity.setIsNew(1);
            }
            //
            helpissueEntity.setUpdateTime(nowTime);
            helpissueEntity.setApproveUserId(loginUser.getUserId());
            helpissueEntity.setApproveMsg(Base64Util.encode(helpissueEntity.getApproveMsg()));

            Integer insertId = helpIssueService.update(helpissueEntity);


        if (insertId > 0) {
            /*成功完成审核任务后获得任务积分*/
            UserAccountVo useraccountentity = new UserAccountVo();
            useraccountentity.setUserId(loginUser.getUserId());
            useraccountentity.setIntegraltype("task");
            useraccountentity.setTitle("完成审核");
            useraccountentity.setLinkId(0);
            useraccountentity.setAmount(new BigDecimal("1.0"));
            useraccountentity.setBalance(new BigDecimal("50.0"));
            useraccountentity.setCategory("integral");
            useraccountentity.setMark("审核获得");
            useraccountentity.setCreateTime(nowTime);
            useraccountentity.setModifyTime(nowTime);
            useraccountentity.setPm(1);
            useraccountentity.setStatus(1);
            useraccountentity.setSuccsign(1);
            userAccountService.save(useraccountentity);
            //update user task integral
            userEntity.setUserId(loginUser.getUserId());
            BigDecimal integral = taskintegral.add(new BigDecimal("50.0"));
            userEntity.setTaskIntegral(integral);
            userService.update(userEntity);
            return toResponsObject(0, "求助审核成功", resultObj);
        } else {
            return toResponsFail("求助审核失败");
        }
    }


}

