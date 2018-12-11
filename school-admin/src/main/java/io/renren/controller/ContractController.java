package io.renren.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.renren.entity.ContractRecord;
import io.renren.service.ContractRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.renren.service.ContractService;
import io.renren.utils.R;
 
@Controller
@RequestMapping("/contract")
public class ContractController extends AbstractController{
	
	@Autowired
	private ContractService contractService;
	 
	 
	
	
	
	@Autowired
	private ContractRecordService contractRecordService;
	
	@ResponseBody
	@RequestMapping(value ="/getToken" )
    public R getToken(HttpServletRequest request, HttpServletResponse response,@RequestBody ContractRecord record) {
        return R.ok(contractService.getToken(true,(long)record.getCompanyId()));
    }
	
	@ResponseBody
	@RequestMapping(value = "/updateByContract", method = RequestMethod.POST)
    public R updateByContract(@RequestBody ContractRecord record) {
		contractRecordService.updateByContract(record);
        return R.ok();

    }
}
