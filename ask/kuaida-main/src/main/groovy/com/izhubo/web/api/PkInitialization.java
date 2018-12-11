package com.izhubo.web.api;

import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import jxl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author hq
 */
@Controller
public class PkInitialization {

	@Autowired
    private QQUserRepositery qqUserRepositery;

	@ResponseBody
	@RequestMapping(value = "pkImport", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public List<String> pkImport(HttpServletRequest request) throws Exception {
		List<String> result = new ArrayList<>();
		String filePath = request.getSession().getServletContext().getRealPath("/")+"/teacherPK.xls";
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		List<String[]> dataList = readExcel(fis);
		if(null != dataList && dataList.size() > 0){
			for(int i = 0; i < dataList.size(); i++){
				int line = i+1;
				String[] dataItem = dataList.get(i);
				String pk = dataItem[0];
				String mobile = dataItem[1];
				List<QQUser> qqUserList = qqUserRepositery.findByUsername(mobile);
				if(qqUserList.size() > 0){
					QQUser qqUser = qqUserList.get(0);
					qqUser.setPk(pk);
					qqUserRepositery.save(qqUser);
					result.add("第"+line+"行保存成功---"+Arrays.toString(dataList.get(i)));
				}
			}
		}
		return result;
	}
	
	public static List<String[]> readExcel(InputStream is) throws Exception
	{
		List<String[]> dataList = new ArrayList<String[]>();
		try
		{
			Workbook book = Workbook.getWorkbook(is);
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			// 得到单元格
			for (int j = 0; j < sheet.getRows(); j++)
			{
				String[] singleRow = new String[sheet.getColumns()];
				for (int i = 0; i < sheet.getColumns(); i++)
				{
					Cell cell = sheet.getCell(i, j);
					singleRow[i] = cell.getContents() ;
					if (cell.getType() == CellType.DATE) {
				        DateCell dateCell = (DateCell) cell;
				        Date date = dateCell.getDate();
				        singleRow[i]= new SimpleDateFormat("yyyy/MM/dd").format(date);
				        
				 } else{
					 singleRow[i] = cell.getContents() ;
				 }
				}
				dataList.add(singleRow);
			}
			book.close();
		}
		catch (Exception e)
		{
			throw new Exception("文件解析失败");
		}
		return dataList;
	}
}
