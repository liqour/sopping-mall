package com.shop.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.shop.exception.BusinessRuntimeException;

/**
 * 
 * @Description: 导入工具类 
 * @ClassName: ExcelImportUtils 
 * @author liquor
 * @date 2017年11月4日 上午10:31:10 
 *
 */
public class ExcelImportUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelImportUtils.class);
	 /**
     * 导入公用方法
     *
     * @param inputStream excel流
     * @param fileName    文件名
     * @param mapIndex    导入的EXCEL映射对象
     * @param titleRowNum 标题行数
     * @param excelTemplateTitle excel模板标题
     * @return List<Map> 数据集
     * @author linjie
     */
    @SuppressWarnings({ "resource", "rawtypes", "deprecation" })
	public static List<Map> readData(MultipartFile multipartFile, String fileName, String[] mapIndex,String excelTemplateTitle) {
		List<Map> list = new ArrayList<Map>();
        Workbook workBook = null;
        try{
	        if (fileName.indexOf("xlsx") > 0) {
	            workBook = new XSSFWorkbook(multipartFile.getInputStream());
	        } else if (fileName.indexOf("xls") > 0) {
	            workBook = new HSSFWorkbook(multipartFile.getInputStream());
	        } else {
	            throw new BusinessRuntimeException("请上传Excel格式文件！");
	        }
	        Sheet sheet = workBook.getSheetAt(0);
	        int rowNum = sheet.getLastRowNum();
	        if (rowNum < 1) {
	            throw new BusinessRuntimeException("excel中无可用的数据！");
	        }
	        //判断模板标题是否正确
	        Row titleRow = sheet.getRow(0);
	        StringBuilder excelTitle = new StringBuilder("");
	        if (null != titleRow) {
	            int colNum = titleRow.getLastCellNum();
	            for (int j = 0; j < colNum; j++) {
	                Cell cell = titleRow.getCell(j);
	                excelTitle = excelTitle.append(cell.getStringCellValue()).append(",");
	            }
	        }
	        if(!excelTemplateTitle.equals(excelTitle.toString())){
	            throw new BusinessRuntimeException("excel模板错误！");
	        }
	        //读取数据
	        for (int i = 1; i <= rowNum; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) {
	                continue;
	            }
	            int colNum = row.getLastCellNum();
	            if (colNum > mapIndex.length) {
	                colNum = mapIndex.length;
	            }
	            Map<String, Object> dataMap = new HashMap<String, Object>();
	            for (int j = 0; j < colNum; j++) {
	                Cell cell = row.getCell(j);
	                if (cell == null) {
	                    dataMap.put(mapIndex[j], "");
	                    continue;
	                }
	                switch (cell.getCellType()) {
	                    case XSSFCell.CELL_TYPE_STRING:
	                        dataMap.put(mapIndex[j], cell.getStringCellValue());
	                        break;
	                    case XSSFCell.CELL_TYPE_BOOLEAN:
	                        dataMap.put(mapIndex[j], cell.getBooleanCellValue());
	                        break;
	                    case XSSFCell.CELL_TYPE_NUMERIC:
	                        String cellValue = null;
	                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                            Date date = cell.getDateCellValue();
	                            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                            cellValue = sFormat.format(date);
	                        } else {
	                            NumberFormat nf = NumberFormat.getInstance();
	                            nf.setGroupingUsed(false);//true时的格式：1,234,567,890
	                            nf.setMaximumFractionDigits(4); //设置最多小数位为4位
	                            double acno = cell.getNumericCellValue();
	                            cellValue = nf.format(acno);
	                        }
	                        dataMap.put(mapIndex[j], cellValue + "");
	                        break;
	                    case XSSFCell.CELL_TYPE_FORMULA:
	                        dataMap.put(mapIndex[j], cell.getCellFormula());
	                        break;
	                    default:
	                        break;
	                }
	            }
	            list.add(dataMap);
	        }
        }catch(Exception e){
        	logger.info("读取Excel出现异常",e);
        	throw new BusinessRuntimeException(StringUtils.isEmpty(e.getMessage()) ? "读取Excel出现异常" : e.getMessage());
        }
        return list;
    }

    /**
     * 对象的所有属性转换成Map输出，需要Obj提供get方法
     *
     * @param obj
     * @return
     * @throws Exception
     * @author linjie
     * @data 2017-04-19
     */

    public static Map<String, String> clazzToMap(Object obj) throws InvocationTargetException, IllegalAccessException {
        Map<String, String> mapValue = new HashMap<String, String>();
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getDeclaredMethods();
        /*try {*/
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.contains("get")) {
                Object value = method.invoke(obj);
                if (value != null) {
                    String name = methodName.substring(3);
                    name = name.substring(0, 1).toLowerCase()
                            + name.substring(1);
                    mapValue.put(name, String.valueOf(value));
                }
            }
        }
        /*} catch (IllegalArgumentException e) {
            LOGGER.debug("参数不对！");
        } catch (InvocationTargetException e) {
            LOGGER.error("方法调用失败！");
        } catch (IllegalAccessException e) {
            LOGGER.debug("没有访问权限！");
        }*/
        return mapValue;
    }

    /**
     * 全角转半角
     *
     * @param obj 需转换的数据
     * @return 转成半角后的数据
     * @author linjie
     * @data 2017-04-24
     */
    public static String toSBCS(Object obj) {
        String str = (String) obj;
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, n = str.length(); i < n; i++) {
            int c = str.charAt(i);
            if ((c >= 'Ａ') && (c <= 'Ｚ')) {
                c = (c + 'A') - 'Ａ';
            } else if ((c >= '０') && (c <= '９')) {
                c = (c + '0') - '０';
            } else if ((c >= 'ａ') && (c <= 'ｚ')) {
                c = (c + 'a') - 'ａ';
            } else if (c == '（') {
                c = (c + '(') - '（';
            } else if (c == '）') {
                c = (c + ')') - '）';
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

    /**
     * 把不规则的日期格式字符串，转换成正规的日期格式字符串
     *
     * @param stringValue       不规则的日期格式字符串
     * @param standerDateFormat 格式
     * @return 规则的日期格式字符串
     * @author linjie
     * @data 2017-04-26
     */
    public static String transferDateTimeString(String stringValue, String standerDateFormat) {

        if (StringUtils.isBlank(stringValue))
            return stringValue;

        // 把用户可能输入的中文全角字符转化成标准英文字符
        String str = stringValue;
        str = str.replaceAll("　", " ");
        str = str.replaceAll("－", "-");
        str = str.replace("：", ":");
        str = str.replaceAll("  ", " ");

        SimpleDateFormat standFormater = new SimpleDateFormat(standerDateFormat);
        if (str.length() == 11) {
            // 19990909100 --> 1999-09-09 01:00
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyyMMddHmm");
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                return str;
            }
        }
        if (str.length() == 12) {
            // YYYYMMddHHmm format 200808091800-->2008-08-09 18:00
            // YYYY-M-d Hmm fromat 2008-7-9 900-->2008-07-09 09:00
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyyMMddHHmm");
                if (str.indexOf("-") != -1)
                    dateFormat = new SimpleDateFormat("yyyy-M-d Hmm");
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                return str;
            }
        }
        if (str.length() == 13) {
            String str2 = str.substring(0, str.indexOf(" "));

            // try {
            // //YYYY-MM-dd Hmm 2008-09-08 600-->2008-09-08 06:00
            // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
            // HHmm");
            // Date theDate = dateFormat.parse(str);
            // return standFormater.format(theDate);
            // } catch (ParseException e) {
            // //do nothing
            // }
            if (str2.length() == 8) {
                // 这边有两种情况 2008-9-8 6:00 用户在时间前多输入空格导致的.
                if (str.indexOf(":") > 0) {
                    try {
                        // YYYY-M-d HHmm 2008-9-8 1600-->2008-09-08 16:00
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-M-d H:mm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    } catch (ParseException e) {
                        // do nothing
                    }
                } else {
                    try {
                        // YYYY-M-d HHmm 2008-9-8 1600-->2008-09-08 16:00
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-M-d HHmm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    } catch (ParseException e) {
                        // do nothing
                    }
                }

            }
            if (str2.length() == 9) {
                // 分两种情况 2008-10-8 和 2008-9-15
                String str3 = str2.substring(0, str2.lastIndexOf("-"));
                if (str3.length() == 6) {
                    try {
                        // YYYY-M-dd HHmm 2008-9-15 900-->2008-09-15 06:00
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-M-dd Hmm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    } catch (ParseException e) {
                        // do nothing
                    }
                } else {
                    try {
                        // YYYY-MM-d Hmm 2008-10-8 900-->2008-10-08 09:00
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-d Hmm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    } catch (ParseException e) {
                        // do nothing
                    }
                }
            }
        }
        if (str.length() == 14) {
            // try {
            // //YYYY-MM-dd HHmm 2008-09-08 600-->2008-09-08 06:00
            // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
            // HHmm");
            // Date theDate = dateFormat.parse(str);
            // return standFormater.format(theDate);
            // } catch (ParseException e) {
            // //do nothing
            // }
            String str2 = str.substring(0, str.indexOf(" "));
            if (str2.length() == 10) {

                try {
                    // YYYY-MM-dd Hmm 2008-09-08 600-->2008-09-08 06:00
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd Hmm");
                    Date theDate = dateFormat.parse(str);
                    return standFormater.format(theDate);
                } catch (ParseException e) {
                    // do nothing
                }
            }
            if (str2.length() == 9) {
                String str3 = str2.substring(0, str2.lastIndexOf("-"));
                if (str3.length() == 7) {
                    // 这里有两种情况 2008-09-8 6:00
                    if (str.indexOf(":") > 0) {
                        try {
                            // 2008-09-8 6:00-->2008-09-08 06:00 用户在时间前多输入空格导致的.
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-MM-d H:mm");
                            Date theDate = dateFormat.parse(str);
                            return standFormater.format(theDate);
                        } catch (ParseException e) {
                            // do nothing
                        }
                    } else {

                        try {
                            // YYYY-MM-d HHmm 2008-09-8 1600-->2008-09-08 16:00
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-MM-d HHmm");
                            Date theDate = dateFormat.parse(str);
                            return standFormater.format(theDate);
                        } catch (ParseException e) {
                            // do nothing
                        }
                    }
                } else {
                    // 这里也有两种情况 2008-9-08 6:00
                    if (str.indexOf(":") > 0) {
                        try {
                            // 2008-9-08 6:00-->2008-09-08 06:00 用户在时间前多输入空格导致的.
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-M-dd H:mm");
                            Date theDate = dateFormat.parse(str);
                            return standFormater.format(theDate);
                        } catch (ParseException e) {
                            // do nothing
                        }
                    } else {
                        try {
                            // YYYY-M-dd HHmm 2008-9-08 1600-->2008-09-08 16:00
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-M-dd HHmm");
                            Date theDate = dateFormat.parse(str);
                            return standFormater.format(theDate);
                        } catch (ParseException e) {
                            // do nothing
                        }
                    }

                }
            }
            if (str2.length() == 8) {
                try {
                    // YYYY-MM-d HHmm 2008-9-8 16:00-->2008-09-08 16:00
                    // 用户在时间前多输入空格导致的.
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-M-d HH:mm");
                    Date theDate = dateFormat.parse(str);
                    return standFormater.format(theDate);
                } catch (ParseException e) {
                    // do nothing
                }
            }

        }
        if (str.length() == 15) {
            try {
                // 有两种情况
                // 一种是: 2008-9-08 16:00 这种情况是用户输入的时候在时间前面加入空格导致的.
                String str2 = str.substring(0, str.indexOf(" "));
                if (str2.length() == 9) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-M-dd HH:mm");
                    Date theDate = dateFormat.parse(str);
                    return standFormater.format(theDate);
                } else {
                    // 这里头又有两种情况
                    // 一种是 2008-09-09 6:00 这种情况是用户输入的时候在时间前面加入空格导致的.
                    if (str.indexOf(":") > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd H:mm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    } else {
                        // YYYY-MM-dd HHmm 2008-09-88 1600-->2008-09-08 16:00
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd HHmm");
                        Date theDate = dateFormat.parse(str);
                        return standFormater.format(theDate);
                    }

                }
            } catch (ParseException e) {
                // do nothing
            }
        }
        if (str.length() == 16) {
            try {
                // YYYY-MM-dd HH:mm 2008-09-88 16:00-->2008-09-08 16:00
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm");
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                // do nothing
            }
        }
        if (str.length() == 17) {
            try {
                // YYYY-M-dd H:mm:ss 2008-9-18 6:00:00-->2008-09-18 06:00:00
                // 用户在时间前多输入空格导致的.
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-M-dd H:mm:ss");
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                // do nothing
            }
        }
        if (str.length() == 18) {
            String str2 = str.substring(0, str.indexOf(" "));
            if (str2.length() == 9) {

                try {
                    // YYYY-M-dd H:mm:ss 2008-9-18 16:00:00-->2008-09-18
                    // 16:00:00 用户在时间前多输入空格导致的.
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-M-dd HH:mm:ss");
                    Date theDate = dateFormat.parse(str);
                    return standFormater.format(theDate);
                } catch (ParseException e) {
                    // do nothing
                }
            } else {
                try {
                    // YYYY-MM-dd H:mm:ss 2008-09-18 6:00:00-->2008-09-18
                    // 06:00:00 用户在时间前多输入空格导致的.
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd H:mm:ss");
                    Date theDate = dateFormat.parse(str);
                    return standFormater.format(theDate);
                } catch (ParseException e) {
                    // do nothing
                }
            }

        }
        if (str.length() == 19) {
            try {
                // YYYY-MM-dd HH:mm:ss 2008-09-18 16:00:00-->2008-09-18 16:00:00
                // 用户在时间前多输入空格导致的.
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                // do nothing
            }
        }
        if (str.length() == 28) {
            try {
                // EEE MMM dd HH:mm:ss zzz yyyy : Wed Apr 26 22:18:00 CST 2017-->2008-09-18 16:00:00
                // 用户在时间前多输入空格导致的.
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                Date theDate = dateFormat.parse(str);
                return standFormater.format(theDate);
            } catch (ParseException e) {
                // do nothing
            }
        }
        return str;
    }
}
