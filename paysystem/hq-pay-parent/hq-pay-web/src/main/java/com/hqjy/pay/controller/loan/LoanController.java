package com.hqjy.pay.controller.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hqjy.pay.LoanEntity;
import com.hqjy.pay.LoanService;
import com.hqjy.pay.utils.CustomBigDecimalEditor;
import com.hqjy.pay.utils.MD5Util;
import com.hqjy.pay.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

@RequestMapping
@Controller
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        CustomBigDecimalEditor customBigDecimalEditor = new CustomBigDecimalEditor();
        binder.registerCustomEditor(BigDecimal.class, customBigDecimalEditor);
    }

    @PostMapping("/loan")
    @ResponseBody
    public Object loan(HttpServletRequest request, @RequestBody LoanEntity loanEntity) {
        String cipher = MD5Util.string2MD5(loanEntity.getOrderNo() + loanEntity.getOrderTimestamp() + loanEntity.getTradeMoney() + "hengqijypay");
        if(cipher == null || !cipher.equals(loanEntity.getCiphertext())){
            return R.error("请求参数校验错误");
        }
        Boolean aBoolean = false;
        try {
            loanEntity.setOtherDataStr(objectMapper.writeValueAsString(loanEntity.getOtherData()));
            aBoolean = loanService.saveLoan(loanEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!aBoolean){
            return R.error("插入数据失败");
        }
        return R.ok().put(getHost(request) + "/loan?id=" + loanEntity.getId());
    }

    @GetMapping("/loan")
    public String loan(Model model, String id) {
        LoanEntity loanEntity = loanService.getLoan(id);
        model.addAttribute(loanEntity);
        try {
            HashMap hashMap = objectMapper.readValue(loanEntity.getOtherDataStr(), HashMap.class);
            model.addAllAttributes(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "daikuan";
    }

    private String getHost(HttpServletRequest request){
        String serverName = request.getServerName();
        Integer port = request.getServerPort();
        String po = port.toString();
        if (po == null) { // 区分测试线和正式线端口设置
            po = "";
        } else {
            po = ":" + po;
        }
        return "http://" + serverName + po;
    }

}
