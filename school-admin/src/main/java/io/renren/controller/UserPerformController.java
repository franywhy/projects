package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.SysUserEntity;
import io.renren.entity.UserPerformEntity;
import io.renren.pojo.UserPerformPOJO;
import io.renren.service.UserPerformService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import org.apache.commons.lang.StringUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping(value = "userPerform")
public class UserPerformController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserPerformService userPerformService;

    /**
     * 获取学员表现信息
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("userPerform:info")
    public R info(@PathVariable("id") Long id) {
        UserPerformPOJO userPerform = userPerformService.queryObject1(id);
        return R.ok().put("userPerform", userPerform);
    }

    /**
     * 查询列表
     *
     * @param request
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("userPerform:list")
    public R queryList(HttpServletRequest request) {
        Map<String, Object> map = getMapPage(request);
        stringQueryDecodeUTF8(map, request, "userNickname");
        stringQueryDecodeUTF8(map, request, "teacher");
        //查询列表数据
        List<UserPerformEntity> userPerformEntities = userPerformService.queryList(map);
        int total = userPerformService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(userPerformEntities, total, request);
        return R.ok().put(pageUtil);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("userPerform:delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        map.put("ids", ids);
        userPerformService.deleteBatch(map);
        return R.ok();
    }

    /**
     * 修改
     *
     * @param userPerform
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @RequiresPermissions("userPerform:update")
    public R update(@RequestBody UserPerformPOJO userPerform) {
        SysUserEntity user = getUser();
        Long creatorId = user.getUserId();

        if (StringUtils.isBlank(userPerform.getCommodityName())) {
            return R.error("商品名称必填！！！");
        }
        if (StringUtils.isBlank(userPerform.getUserNickname())) {
            return R.error("学员名称必填！！！");
        }
        String nickName = userPerform.getUserNickname();
        String commodityName = userPerform.getCommodityName();
        Map<String, Object> map = userPerformService.queryOrderAndUser(nickName, commodityName);
        if (map == null) {
            return R.error("不存在学员的商品订单号，请校验学员名称和商品名称是否对应！");
        }
        Long orderId = (Long) map.get("orderId");
        Long userId = (Long) map.get("userId");
        if (!StringUtils.isBlank(userPerform.getPerform()) && userPerform.getPerform().length() > 400) {
            return R.error("评价信息字符不能超过400");
        }
        if (StringUtils.isBlank(userPerform.getScholarshipName())) {
            return R.error("奖学金名称必填！！！");
        }
        if (userPerform.getScholarship() <= 0.0) {
            return R.error("奖学金金额必填！！！");
        }
        if (null == userPerform.getCreationTime()
                || StringUtils.isBlank(userPerform.getCreationTime().toString())) {
            return R.error("奖学金获取时间必填！！！");
        }

        UserPerformEntity userPerformEntity = new UserPerformEntity();
        userPerformEntity.setPerform(userPerform.getPerform());
        userPerformEntity.setOrderId(orderId);
        userPerformEntity.setScholarshipName(userPerform.getScholarshipName());
        userPerformEntity.setCreatorId(creatorId);
        userPerformEntity.setScholarship(userPerform.getScholarship());
        userPerformEntity.setUserId(userId);
        userPerformEntity.setCreationTime(userPerform.getCreationTime());
        userPerformEntity.setUserNickname(userPerform.getUserNickname());
        userPerformEntity.setDr(0);
        userPerformEntity.setId(userPerform.getId());

        userPerformService.update(userPerformEntity);
        return R.ok();
    }

    /**
     * 新增
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions("userPerform:save")
    public R save(@RequestBody UserPerformPOJO userPerform) {
        //记录当前用户的ID
        SysUserEntity user = getUser();
        Long creatorId = user.getUserId();

        if (StringUtils.isBlank(userPerform.getCommodityName())) {
            return R.error("商品名称必填！！！");
        }
        if (StringUtils.isBlank(userPerform.getUserNickname())) {
            return R.error("学员名称必填！！！");
        }
        String nickName = userPerform.getUserNickname();
        String commodityName = userPerform.getCommodityName();
        Map<String, Object> map = userPerformService.queryOrderAndUser(nickName, commodityName);
        if (map == null) {
            return R.error("不存在该学员的订单号，请校验学员名称和商品名称是否正确！");
        }
        Long orderId = (Long) map.get("orderId");
        Long userId = (Long) map.get("userId");
        if (!StringUtils.isBlank(userPerform.getPerform()) && userPerform.getPerform().length() > 400) {
            return R.error("评价信息字符不能超过400");
        }
        if (StringUtils.isBlank(userPerform.getScholarshipName())) {
            return R.error("奖学金名称必填！！！");
        }
        if (userPerform.getScholarship() <= 0.0) {
            return R.error("奖学金金额必填！！！");
        }
        if (null == userPerform.getCreationTime()
                || StringUtils.isBlank(userPerform.getCreationTime().toString())) {
            return R.error("奖学金获取时间必填！！！");
        }

        UserPerformEntity userPerformEntity = new UserPerformEntity();
        userPerformEntity.setPerform(userPerform.getPerform());
        userPerformEntity.setOrderId(orderId);
        userPerformEntity.setScholarshipName(userPerform.getScholarshipName());
        userPerformEntity.setCreatorId(creatorId);
        userPerformEntity.setScholarship(userPerform.getScholarship());
        userPerformEntity.setUserId(userId);
        userPerformEntity.setCreationTime(userPerform.getCreationTime());
        userPerformEntity.setUserNickname(userPerform.getUserNickname());
        userPerformEntity.setDr(0);
        userPerformService.save(userPerformEntity);
        return R.ok();
    }

    /**
     * 下载模版
     *
     * @param request
     * @param res
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/downTemplate", method = RequestMethod.GET)
    public void downTemplate(HttpServletRequest request, HttpServletResponse res)
            throws IOException {

        String arrStr = "0,0,0,0,商品&0,1,0,0,学员ID&0,2,0,0,姓名&0,3,0,0,时间(例:2018/08/08)&0,4,0,0,奖学金名称&0,5,0,0,奖学金金额(例:2000.0)&0,6,0,0,学习评价&0,7,0,0,班主任";
        String title = "表现信息";
        String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
        String[] cells = cellsStr.split("&");
        String filename = title + "模板导出-" + Calendar.getInstance().getTimeInMillis() + ".xls";
        filename = new String(filename.getBytes("GBK"), "iso-8859-1");
        res.setContentType("text/html; charset=UTF-8");
        res.addHeader("Content-Disposition",
                (new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
        OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
        ExcelReaderJXL.exportToJxlExcel(filename, title + "下载/导出模板", cells, toClient);
        toClient.flush();
        toClient.close();

    }

    /**
     * EXCEL批量导入
     *
     * @throws Exception
     */
    @SysLog("EXCEL批量导入")
    @ResponseBody
    @RequestMapping("/batchImport")
    public R batchImport(HttpServletRequest request) throws Exception {
        //记录当前用户的ID
        SysUserEntity user = getUser();
        Long creatorId = user.getUserId();

        String commodityName;
        String userId;
        String userName;
        String creationTime;
        String schoolshipName;
        String schoolship;
        String perform;
        String teacher;

        MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
        //获取上传文件
        MultipartFile file = mpReq.getFile("file_data");
        List<UserPerformEntity> detailList = new LinkedList<UserPerformEntity>();
        UserPerformEntity userPerform = null;
        //把文件以流的形式输入
        FileInputStream fio = (FileInputStream) file.getInputStream();

        String exceptMsg = "";
        
        List<String[]> dataList = null;
        try{
        	dataList = ExcelReaderJXL.readExcelSDF(fio, "yyyy-MM-dd HH:mm:ss");
        }catch (Exception e) {
			e.printStackTrace();
			exceptMsg = "文件解析错误";
		}
        
        if(dataList == null || dataList.size() == 0){
        	return R.error(1,exceptMsg);
        }
        
        //获取文件表头栏
        String[] header = dataList.get(0);
        ArrayList<String> exceptList = new ArrayList<>();

        if (header.length < 8) {
            exceptMsg = "总列数不正确，请核对一下列数;";
            exceptList.add(exceptMsg);
        } else if (!"商品".equals(header[0].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【商品】匹配;";
            exceptList.add(exceptMsg);
        } else if (!"学员ID".equals(header[1].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【学员ID】匹配;";
            exceptList.add(exceptMsg);
        } else if (!"姓名".equals(header[2].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【姓名】匹配;";
            exceptList.add(exceptMsg);
        } else if (!"时间(例:2018/08/08)".equals(header[3].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【时间(例:2018/08/08)】匹配;";
            exceptList.add(exceptMsg);
        } else if (!"奖学金名称".equals(header[4].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【奖学金名称】匹配;";
            exceptList.add(exceptMsg);

        } else if (!"奖学金金额(例:2000.0)".equals(header[5].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【奖学金金额(例:2000.0)】匹配;";
            exceptList.add(exceptMsg);
        } else if (!"学习评价".equals(header[6].trim())) {
            exceptMsg = "对应的列名不对，请核对一下是否与【学习评价】匹配;";
            exceptList.add(exceptMsg);
        } else if(!"班主任".equals(header[7].trim())){
            exceptMsg = "对应的列名不对，请核对一下是否与【班主任】匹配;";
            exceptList.add(exceptMsg);
        } else if (header.length >= 8 && "商品".equals(header[0].trim()) && "学员ID".equals(header[1].trim())
                && "姓名".equals(header[2].trim()) && "时间(例:2018/08/08)".equals(header[3].trim())
                && "奖学金名称".equals(header[4].trim()) && "奖学金金额(例:2000.0)".equals(header[5].trim())
                && "学习评价".equals(header[6].trim()) && "班主任".equals(header[7].trim())) {
            dataList.remove(0);
            for (int i = 0; i < dataList.size(); i++) {

                String[] dataArr = dataList.get(i);
                if (dataArr.length == 8) {

                    Map<String, Object> map = getMapPage(request);
                    boolean flag = false;
                    exceptMsg = "第" + new Integer(i + 2).toString() + "行数据不能导入原因是：";

                    commodityName = dataArr[0];
                    userId = dataArr[1];
                    userName = dataArr[2];
                    creationTime = dataArr[3];
                    schoolshipName = dataArr[4];
                    schoolship = dataArr[5];
                    perform = dataArr[6];
                    teacher = dataArr[7];


                    if (commodityName == null || "".equals(commodityName)) {
                        exceptMsg += "商品名称不能为空";
                        flag = true;
                    }

                    if (userId == null || "".equals(userId)) {
                        exceptMsg += "学员ID不能为空";
                        flag = true;
                    }

                    Long orderId = userPerformService.queryOrderId(Long.parseLong(userId), commodityName);
                    if (orderId == null) {
                        exceptMsg += "不存在该学员的本商品订单号";
                        flag = true;
                    }

                    if (userName == null || "".equals(userName)) {
                        exceptMsg += "学员名字不能为空";
                        flag = true;
                    }

                    Date creationDate = null;
                    if (creationTime == null || "".equals(creationTime)) {
                        if (flag == true) {
                            exceptMsg += "时间不能为空";
                        } else {
                            exceptMsg += "时间不能为空";
                            flag = true;
                        }
                    } else {
                        creationDate = DateUtils.parse(creationTime, "yyyy-MM-dd HH:mm:ss");
                        if (null == creationDate) {
                            if (flag == true) {
                                exceptMsg += "时间格式不正确";
                            } else {
                                exceptMsg += "时间格式不正确";
                                flag = true;
                            }
                        }
                    }

                    if (schoolshipName == null || "".equals(schoolshipName)) {
                        exceptMsg += "奖学金名称不能为空";
                        flag = true;
                    }

                    if (schoolship == null || "".equals(schoolship)) {
                        exceptMsg += "奖学金金额不能为空";
                        flag = true;
                    }

                    if (perform == null || "".equals(perform)) {
                        exceptMsg += "学员评价不能为空";
                        flag = true;
                    } else {
                        if (StringUtils.length(perform) > 400) {
                            if (flag == true) {
                                exceptMsg += "评价长度不能大于400个字符";
                            } else {
                                exceptMsg += "评价长度不能大于400个字符";
                                flag = true;
                            }
                        }
                    }
                    if ( teacher== null || "".equals(teacher)) {
                        exceptMsg += "班主任不能为空";
                        flag = true;
                    }
                    if (flag) {
                        exceptList.add(exceptMsg);
                        continue;
                    }

                    userPerform = new UserPerformEntity();
                    userPerform.setUserId(Long.parseLong(userId));
                    userPerform.setUserNickname(userName);
                    userPerform.setCreationTime(creationDate);
                    userPerform.setCreatorId(creatorId);
                    userPerform.setOrderId(orderId);
                    userPerform.setScholarship((float) (Math.round(Float.parseFloat(schoolship) * 10)) / 10);
                    userPerform.setScholarshipName(schoolshipName);
                    userPerform.setPerform(perform);
                    userPerform.setDr(0);
                    detailList.add(userPerform);
                }
            }
        }

        String errMsgErr = "";
        for (int i = 0; i < exceptList.size(); i++) {
            errMsgErr += exceptList.get(i) + "<br>";
        }

        if (errMsgErr.equals("")) {
            // 保存
            if (!detailList.isEmpty()) {
                this.userPerformService.saveBatch(detailList);
            }
            return R.ok("");
        } else {
            return R.error(1, errMsgErr);
        }
    }


}
