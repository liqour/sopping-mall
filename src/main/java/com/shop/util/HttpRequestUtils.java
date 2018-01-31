//package com.shop.util;
//
//import java.io.FileOutputStream;  
//import java.io.IOException;  
//import java.io.InputStream;  
//import java.io.InputStreamReader;  
//import java.io.OutputStreamWriter;  
//import java.io.UnsupportedEncodingException;  
//import java.net.HttpURLConnection;  
//import java.net.Socket;  
//import java.net.URL;  
//import java.net.URLConnection;  
//import java.net.URLEncoder;  
//import java.util.ArrayList;  
//import java.util.HashMap;  
//import java.util.Iterator;  
//import java.util.List;  
//import java.util.Map;  
//import java.util.Map.Entry;  
//  
//import org.apache.http.HttpResponse;  
//import org.apache.http.NameValuePair;  
//import org.apache.http.client.HttpClient;  
//import org.apache.http.client.entity.UrlEncodedFormEntity;  
//import org.apache.http.client.methods.HttpGet;  
//import org.apache.http.client.methods.HttpPost;  
//import org.apache.http.impl.client.DefaultHttpClient;  
//import org.apache.http.message.BasicNameValuePair;  
//
//import com.alibaba.fastjson.JSONObject;
// 
//public class HttpRequestUtils {
//    public static String httpClientPost(String urlParam, Map params, String charset) {  
//        StringBuffer resultBuffer = null;  
//        HttpClient client = new DefaultHttpClient();  
//        HttpPost httpPost = new HttpPost(urlParam);  
//        // 构建请求参数  
//        List list = new ArrayList();  
//        Iterator iterator = params.entrySet().iterator();  
//        while (iterator.hasNext()) {  
//            Entry elem = iterator.next();  
//            list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));  
//        }  
//        BufferedReader br = null;  
//        try {  
//            if (list.size() > 0) {  
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);  
//                httpPost.setEntity(entity);  
//            }  
//            HttpResponse response = client.execute(httpPost);  
//            // 读取服务器响应数据  
//            resultBuffer = new StringBuffer();  
//            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));  
//            String temp;  
//            while ((temp = br.readLine()) != null) {  
//                resultBuffer.append(temp);  
//            }  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        } finally {  
//            if (br != null) {  
//                try {  
//                    br.close();  
//                } catch (IOException e) {  
//                    br = null;  
//                    throw new RuntimeException(e);  
//                }  
//            }  
//        }  
//        return resultBuffer.toString();  
//    }  
//}