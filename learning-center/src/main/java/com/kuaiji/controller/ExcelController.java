package com.kuaiji.controller;

import com.kuaiji.entity.*;
import com.kuaiji.service.*;
import com.kuaiji.utils.ImportExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kuaiji.utils.CommonUtil.tryProduct;
import static com.kuaiji.utils.CommonUtil.userSync;

/**
 * Created by Administrator on 2017/9/8 0008.
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private AppAccountService appAccountService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private AppProvinceService appProvinceService;

    @Autowired
    private AppService appService;

    @RequestMapping(value = "importFile",method = RequestMethod.POST)
    public void importFile(MultipartFile file, int type, String code) {
        try {
            ImportExcel ei = new ImportExcel(file, 0, 0);
            for (int i = ei.getDataRowNum(); i <= ei.getLastDataRowNum(); i++) {
                Row row = ei.getRow(i);
                if(null != row) {
                    String val0 = "";
                    String val1 = "";
                    String val2 = "";
                    for (int k = 0; k < row.getLastCellNum(); k++)  //LastCellNum 是当前行的总列数
                    {
                        Cell cell = row.getCell(k);  //当前表格
                        if(null != cell) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            Object val = cell.getStringCellValue();
                            if(0 == k) {
                                val0 = val.toString();
                            } else if(1 == k) {
                                val1 = val.toString();
                            } else if(2 == k) {
                                val2 = val.toString();
                            }
                        }
                    }
                    if(99 == type) {
                        type99(val1,val0);
                    } else if(100 == type) {
                        type100(val1,val0);
                    } else if(101 == type) {
                        type101(val1,val0);
                    } else if(102 == type) { //广东
                        type102(val1,val0);
                    } else if(103 == type) { //上海
                        type103(val1,val0);
                    } else if(105 == type) { //全国
                        type105(val1,val0,val2);
                    } else if(106 == type) { //全国-中级题库
                        type106(val1,val0,val2);
                    } else if(107 == type) { //全国-初级题库
                        typeAll(val1,val0,val2,"A005");
                    } else if(108 == type) { //全国-CPA
                        typeAll(val1,val0,val2,"A006");
                    } else {
                        typeOther(val0,val1,type,code);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void type99(String username, String realName) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 10);
            if(userAppCount == 0) { //不存在记录
                UserApp userApp = new UserApp();
                userApp.setUserid(users.getUserId());
                userApp.setAppid(10);
                userApp.setCode("B001");
                userApp.setUsername(username);
                userApp.setUserpass("888888");
                userApp.setCreatetime(new Date());
                userApp.setDr(0);
                userAppService.addUserApp(userApp);

                //用户同步接口
                String pwd = "JDeO-1612-02D-1107-8711";
                int sex = 0;
                userSync(username, sex, realName, pwd);
                //试用产品接口
                tryProduct(username,pwd);
            }
        }
    }

    private void type100(String username, String realName) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(5);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 5);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(5);
                    userApp.setCode("A002");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("12dc964b0ff61a43ea3cdb0b65370f6a");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);
                    userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                }
            }
        }
    }

    private void type101(String username, String realName) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(8);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 8);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(8);
                    userApp.setCode("A003");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("83d8abfd46e6a0c3c16955ab69cf92d5");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);
                    userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                }
            }
        }
    }

    private void type102(String username, String realName) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(2);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 2);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(2);
                    userApp.setCode("A001");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("c32ec9d4921a85bfac6c438bf31dc047");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);
                    userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                }
            }
        }
    }

    private void type103(String username, String realName) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(3);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 3);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(3);
                    userApp.setCode("A001");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("12d38ad1b1df6dc1b909ad1c4c75ebfa");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);
                    userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                }
            }
        }
    }

    private void type105(String username, String realName, String provincename) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(11);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 11);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(11);
                    userApp.setCode("A001");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("c32ec9d4921a85bfac6c438bf31dc047");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);

                    AppProvince appProvince = appProvinceService.findByProvinceName(provincename);
                    if(null != appProvince) {
                        userApp.setSchoolCode(appProvince.getNcProvinceCode() + "010101");
                        userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                    }
                }
            }
        }
    }

    private void type106(String username, String realName, String provincename) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            AppAccount appAccount = appAccountService.findIsStudentByAppId(12);
            if(null != appAccount) {
                int userAppCount = userAppService.findByUseridAppid(users.getUserId(), 12);
                if(userAppCount == 0) { //不存在记录
                    UserApp userApp = new UserApp();
                    userApp.setUserid(users.getUserId());
                    userApp.setAppid(12);
                    userApp.setCode("A004");
                    userApp.setUsername(appAccount.getUsername());
                    userApp.setUserpass(appAccount.getUserpass());
                    userApp.setCourseid("aac528a10e57e5df3ce6995edaf0c7e5");
                    userApp.setCreatetime(new Date());
                    userApp.setDr(0);

                    AppProvince appProvince = appProvinceService.findByProvinceName(provincename);
                    if(null != appProvince) {
                        userApp.setSchoolCode(appProvince.getNcProvinceCode() + "010101");
                        userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
                    }
                }
            }
        }
    }

    private void typeAll(String username, String realName, String provincename, String code) {
        Users users = usersService.findByMobile(username);
        if (null != users) {
            List<App> appList = appService.findListByCode(code);
            if(null != appList) {
                AppAccount appAccount = appAccountService.findIsStudentByCode(code);
                if(null != appAccount) {
                    List<UserApp> list = new ArrayList<>();
                    for (App app : appList) {
                        int userAppCount = userAppService.findByUseridAppid(users.getUserId(), app.getAppid());
                        //不存在记录
                        if(userAppCount == 0) {
                            UserApp userApp = new UserApp();
                            userApp.setUserid(users.getUserId());
                            userApp.setAppid(app.getAppid());
                            userApp.setCode(app.getCode());
                            userApp.setUsername(appAccount.getUsername());
                            userApp.setUserpass(appAccount.getUserpass());
                            userApp.setCourseid(app.getCourseid());
                            userApp.setCreatetime(new Date());
                            userApp.setDr(0);

                            AppProvince appProvince = appProvinceService.findByProvinceName(provincename);
                            if(null != appProvince) {
                                userApp.setSchoolCode(appProvince.getNcProvinceCode() + "010101");
                                list.add(userApp);
                            }
                        }
                    }
                    if(list.size() > 0) {
                        userAppService.addUserAppListUpdateAccountDr(list, appAccount.getAccountid());
                    }
                }
            }
        }
    }

    private void typeOther(String username, String userpass, int type, String code) {
        AppAccount appAccount = new AppAccount();
        appAccount.setAppid(type);
        appAccount.setCode(code);
        appAccount.setUsername(username);
        appAccount.setUserpass(userpass);
        appAccount.setDr(0);
        appAccount.setCreatetime(new Date());

        AppAccount account = appAccountService.findByUsernameAppid(appAccount.getUsername(),appAccount.getAppid());
        if(null == account) {
            appAccountService.addAppAccount(appAccount);
        } else {
            appAccountService.updatePTByUsernameAppid(appAccount.getUserpass(),new Date(),appAccount.getUsername(),type);
        }
    }

}






























