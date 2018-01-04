package com.shop.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @Description: 返回值工具类 
 * @ClassName: ResultUtils 
 * @author liquor
 * @date 2017年9月2日 上午9:54:59 
 *
 */
public abstract class ResultUtils{
  public static final String RESULT_SYMBOL = "RESULT_SYMBOL";

  public static final Map<String, Object> getFaildResultData(){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(false));
    return localHashMap;
  }

  public static final Map<String, Object> getFaildResultData(String[] errorMessages){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(false));
    localHashMap.put("errorMessages", errorMessages);
    return localHashMap;
  }

  public static final Map<String, Object> getFaildResultDataWithErrorCode(Object errorCode, String[] errorMessages){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(false));
    localHashMap.put("errorCode", errorCode);
    localHashMap.put("errorMessages", errorMessages);
    return localHashMap;
  }

  public static final Map<String, Object> getFaildResultData(Collection<String> errorMessages) {
    if (CollectionUtils.isNotEmpty(errorMessages)) {
      String[] arrayOfString = (String[])errorMessages.toArray(new String[errorMessages.size()]);
      return getFaildResultData(arrayOfString);
    }
    return getFaildResultData();
  }

  public static final Map<String, Object> getSuccessResultData(){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(true));
    return localHashMap;
  }

  public static final Map<String, Object> getSuccessResultData(Object data){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(true));
    localHashMap.put("data", data);
    return localHashMap;
  }

  public static final Map<String, Object> getSuccessResultData(String root, Object data){
    HashMap<String, Object> localHashMap = new HashMap<String, Object>();
    localHashMap.put("success", Boolean.valueOf(true));
    localHashMap.put(root, data);
    return localHashMap;
  }
}