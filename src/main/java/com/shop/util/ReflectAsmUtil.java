package com.shop.util;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.shop.entity.UserInfo;

/**
 * 性能是spring beanutis.copyProperties的三倍
 * 
 * @author FEILIX
 *
 */
public class ReflectAsmUtil {
	private static Map<Class<? extends Object>, MethodAccess> methodMap = new HashMap<Class<? extends Object>, MethodAccess>();  
    private static Map<Class<? extends Object>,Map<String, InternalPropertyDescriptor>> propertyDescriptorMap = new HashMap<Class<? extends Object>,Map<String, InternalPropertyDescriptor>>(); 

    private static class InternalPropertyDescriptor extends PropertyDescriptor{
		private Integer readMethodIndex;// 读取方法的序号
    	private Integer writeMethodIndex;// 写入方法的序号
    	
    	public InternalPropertyDescriptor(String propertyName, Class<?> beanClass) throws IntrospectionException {
			super(propertyName, beanClass);
		}
    	
		public Integer getReadMethodIndex() {
			return readMethodIndex;
		}
		public void setReadMethodIndex(Integer readMethodIndex) {
			this.readMethodIndex = readMethodIndex;
		}
		public Integer getWriteMethodIndex() {
			return writeMethodIndex;
		}
		public void setWriteMethodIndex(Integer writeMethodIndex) {
			this.writeMethodIndex = writeMethodIndex;
		}
    }
    
    /**
     * 
     * @Description: 类复制
     * @Title: copyProperties 
     * @param @param src
     * @param @param dest
     * @param @throws IntrospectionException    设定文件 
     * @return void    返回类型 
     * @throws 
     * @author liquor
     */
    public static <T, K> void copyProperties(T src, K dest) throws IntrospectionException {  
        MethodAccess descMethodAccess = methodMap.get(dest.getClass());  
        if (descMethodAccess == null) {  
            descMethodAccess = cache(dest);  
        }  
        MethodAccess orgiMethodAccess = methodMap.get(src.getClass());  
        if (orgiMethodAccess == null) {  
            orgiMethodAccess = cache(src);  
        }  
        Map<String, InternalPropertyDescriptor> destPds = propertyDescriptorMap.get(dest.getClass());
        Map<String, InternalPropertyDescriptor> srcPds = propertyDescriptorMap.get(src.getClass());
        Integer setIndex = null;
        InternalPropertyDescriptor readPd = null;
        for (Map.Entry<String, InternalPropertyDescriptor> entry : destPds.entrySet()) { 
        	setIndex = entry.getValue().getWriteMethodIndex();
        	readPd = srcPds.get(entry.getKey());
        	if(readPd != null) {
        		descMethodAccess.invoke(dest, setIndex, orgiMethodAccess.invoke(src, readPd.getReadMethodIndex()));  
        	}
        }  
    }  
  
    /**
     * 
     * @Description: 缓存 
     * @Title: cache 
     * @param @param obj
     * @param @return
     * @param @throws IntrospectionException    设定文件 
     * @return MethodAccess    返回类型 
     * @throws 
     * @author liquor
     */
    private static MethodAccess cache(Object obj) throws IntrospectionException {
    	MethodAccess methodAccess = null;
    	synchronized (obj.getClass()) {  
            methodAccess = MethodAccess.get(obj.getClass()); 
            Field[] fields = obj.getClass().getDeclaredFields(); 
            Map<String, InternalPropertyDescriptor> pds = new HashMap<String, InternalPropertyDescriptor>();
            for (Field field : fields) {
            	if (!Modifier.isStatic(field.getModifiers())) { // 排除类变量
            		InternalPropertyDescriptor pd = new InternalPropertyDescriptor(field.getName(), obj.getClass());
                 	Method writeMethod = pd.getWriteMethod();
                 	Method readMethod = pd.getReadMethod();
                 	if(writeMethod != null && readMethod != null) {
                 		pd.setReadMethodIndex(methodAccess.getIndex(readMethod.getName()));
                 		pd.setWriteMethodIndex(methodAccess.getIndex(writeMethod.getName()));
                 		pds.put(field.getName(), pd);
                 	}
            	 }
            }  
            methodMap.put(obj.getClass(), methodAccess);  
            propertyDescriptorMap.put(obj.getClass(), pds);
        }  
        return methodAccess; 
    } 

    /**
     * 
     * @Description: 测试方法 
     * @Title: main 
     * @param @param args
     * @param @throws IntrospectionException    设定文件 
     * @return void    返回类型 
     * @throws 
     * @author liquor
     */
    @SuppressWarnings("static-access")
	public static void main(String[] args) throws IntrospectionException {
    	ReflectAsmUtil r = new ReflectAsmUtil();
    	List<UserInfo> userList = new ArrayList<>();
    	for(int i=0;i<1000000;i++){
    		UserInfo user = new UserInfo();
    		user.setId(i);
    		user.setUserName("admin");
    		userList.add(user);
    	}
    	List<UserInfo> userList1 = new ArrayList<>();
    	Long start = System.currentTimeMillis();
    	for(UserInfo user:userList){
    		UserInfo user1 = new UserInfo();
    		r.copyProperties(user, user1);
    		userList1.add(user1);
    		break;
    	}
    	Long end = System.currentTimeMillis();
    	System.out.println("1百万数据用时:【"+(end - start)+"】,list1大小:"+userList1.size());
	}
}
