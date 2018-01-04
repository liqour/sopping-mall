package com.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.shop.tool.DynamicBean;
import com.shop.util.ExcelImportUtils;
import com.shop.util.ResultUtils;
import com.shop.vo.UploadVo;

@Controller
@RequestMapping("/excel")
public class ExcelImportController {
	
	//导入的列
	public static final String[] batchImportColumn = new String[]{"aa"};
	public static final String excelTitle = "aa,";

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> excelImport(UploadVo upload, HttpServletRequest request) throws ClassNotFoundException {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("status", "SUCCESS");
		//读取
		 List<Map> dataList = ExcelImportUtils.readData(upload.getMultipartFile(), upload.getMultipartFile().getOriginalFilename(), upload.getBatchImportColumn(), upload.getExcelTitle());
//		 //校验 
//		 excelImportService.validateExcelSpecial(reMap, dataList);
		 System.out.println(JSONArray.toJSON(dataList));
		 StringBuffer str = new StringBuffer();
		 for(Map map:dataList){
			 StringBuffer column = new StringBuffer();
			 for(int i=0;i<upload.getBatchImportColumn().length;i++){
				 column.append("'"+upload.getBatchImportColumn()[i]+"'");
				 if(i!=upload.getBatchImportColumn().length-1){
					 column.append(",");
				 }
			 }
			 str.append("insert into "+upload.getTableName()+" ("+column+") values(");
			 for(int i=0;i<upload.getBatchImportColumn().length;i++){
				 if(map.get(upload.getBatchImportColumn()[i])==null || "".equals(map.get(upload.getBatchImportColumn()[i]))){
					 str.append("null");
				 }else{
					 Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d{0,})?$"); 
					 Matcher isNum = pattern.matcher(map.get(upload.getBatchImportColumn()[i]).toString());
					 if(isNum.matches()){
						 str.append(map.get(upload.getBatchImportColumn()[i]));
					 }else{
						 str.append("'"+map.get(upload.getBatchImportColumn()[i])+"'");
					 }
				 }
				 if(i!=upload.getBatchImportColumn().length-1){
					 str.append(",");
				 }
			 }
			 str.substring(0, str.length()-1);
			 str.append(");\n");
		 }
		 Map<Object, Object> map = new HashMap<>();
		 map.put("dataList", Class.forName("java.util.ArrayList"));
		 map.put("sql",Class.forName("java.lang.String"));
		 DynamicBean data = new DynamicBean(map);
		 data.setValue("dataList", dataList);
		 data.setValue("sql", str.toString());
		return ResultUtils.getSuccessResultData(data);
	}
}
