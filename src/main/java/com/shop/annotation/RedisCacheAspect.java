package com.shop.annotation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.exception.BusinessRuntimeException;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * @Description: 缓存注解方法实现
 * @author 000302
 *
 */
@Aspect
@Component
public class RedisCacheAspect {

	/**
	 * @Description: 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

	private static String[] types = { "java.lang.Integer", "java.lang.Double",  
            "java.lang.Float", "java.lang.Long", "java.lang.Short",  
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",  
            "java.lang.String", "int", "double", "long", "short", "byte",  
            "boolean", "char", "float" };
	
	/**
	 * @module: 缓存操作类
	 */
	@Autowired
	private com.shop.redis.RedisHelper redisHelper;

	
	/**
	 * @Description: 切入点
	 */
	@Pointcut("@annotation(RedisCache)")
	private void setJoinPoint() {
	}

	/**
	 * @Description: 执行方法时的操作
	 * @param proceedingJoinPoint
	 * @return
	 */
	@Around(value = "setJoinPoint()")
	public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

		Object result = null;
		// 获取缓存key
		String key = getCacheKey(proceedingJoinPoint);
		// 前置通知:调用从redis中查询的方法
//		Object obj = redisHelper.get(key);
		Object obj = redisHelper.getV(key,0);

		if (obj != null) {
			logger.debug("找到缓存,调用缓存返回值,缓存剩余生存时间：" + redisHelper.getExpireTime(key));
			return obj;
		}

		try {
			logger.debug("没有从redis中查到数据,开始从数据库查询数据");
			// 让方法继续执行
			result = proceedingJoinPoint.proceed();
			MethodSignature ms = (MethodSignature)(proceedingJoinPoint.getSignature());
			long expireTime = ms.getMethod().getAnnotation(RedisCache.class).expireTime();
			if(expireTime<=0){// 判断是否是限定时间的缓存
				// 开始写入缓存
				logger.debug("写入永久缓存");
//				redisHelper.setObject(key, result);
				redisHelper.setV(key, result, 0);
			}else{
				// 开始写入缓存
				TimeUnit timeUnit = ms.getMethod().getAnnotation(RedisCache.class).timeUnit();// 获取时间单位
				logger.info("写入带生存时间的缓存,生存时间为:" + expireTime + com.shop.common.TimeUnit.getDescription(timeUnit));
//				redisHelper.setObject(key, result, expireTime, timeUnit);
				redisHelper.setVExpire(key, result, Integer.parseInt(expireTime+""), 0);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}

//		// 返回通知
//
//		// 获取
//		Class<? extends Object> targetClass = proceedingJoinPoint.getTarget().getClass();
//		Method[] methods = targetClass.getDeclaredMethods();
//		// 遍历所有方法，找出当前连接点方法
//		for (Method method : methods) {
//			if (method.getName().equals(proceedingJoinPoint.getSignature().getName())
//					&& method.isAnnotationPresent(RedisCache.class)) {
//				// 获取当前注解
//				RedisCache redisCache = method.getAnnotation(RedisCache.class);
//				Class<? extends Object> clazz = result.getClass();
//				Field ret = clazz.getDeclaredField("ret");// 是否成功
//				Field data = clazz.getDeclaredField("data");// data
//				ret.setAccessible(true);
//				data.setAccessible(true);
//				// 切入点方法执行成功后，执行切面动作
//				if (1 == (Integer) ret.get(result)) {
//					// 缓存至Redis
//					// Map map = (Map) data.get(result);
//					logger.debug("开始保存缓存数据【redisCache.prefix()】:" + redisCache.prefix() + ",【redisCache.id()】："
//							+ redisCache.id() + ",【map.get(redisCache.key())】" + data.get(result));
//					// redisHelper.cacheSimpleBean(redisCache.prefix(),redisCache.id(),
//					// data.get(result));// map.get(redisCache.key())
//				}
//				break;
//			}
//		}
		// 后置通知
		return result;
	}
	
	/**
	 * @Description: 测试方法
	 * @param joinPoint
	 */
	public void test(ProceedingJoinPoint joinPoint){
		try {
			// joinPoint.getTarget().getClass().getName() 获取类路径【com.shop.service.impl.UserInfoServiceImpl】
			Class<?> clazz = Class.forName(joinPoint.getTarget().getClass().getName());// JVM查找并加载指定的类
			String clazzName = clazz.getName();// 类全路径
//	        String clazzSimpleName = clazz.getSimpleName();// 类名
	        String methodName = joinPoint.getSignature().getName();// 方法名
	        try {
	        	// 获取参数名称数组
				String[] paramNames = getFieldsName(this.getClass(), clazzName, methodName);
				String logContent = writeLogInfo(paramNames, joinPoint);
				System.out.println("值："+logContent);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	          
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 获取方法参数的值（基本数据类型）
	 * @param paramNames
	 * @param joinPoint
	 * @return
	 */
	private static String writeLogInfo(String[] paramNames, JoinPoint joinPoint){  
        Object[] args = joinPoint.getArgs();// 所有参数的值
        StringBuilder sb = new StringBuilder();  
        for(int k=0; k<args.length; k++){  
            Object arg = args[k];
            sb.append(paramNames[k]+" ");  
            // 获取对象类型  
            String typeName = arg.getClass().getTypeName();// 获取参数的参数类型
            for (String t : types) {  
                if (t.equals(typeName)) {  
                    sb.append("=" + arg+"; ");  
                }  
            }  
            sb.append(getFieldsValue(arg));  
        }
        return sb.toString();  
    }  
	
	 /** 
     * @Description: 得到方法参数的名称 
     * @param cls 
     * @param clazzName 
     * @param methodName 
     * @return 
     * @throws NotFoundException 
     */  
    private static String[] getFieldsName(Class<? extends RedisCacheAspect> cls, String clazzName, String methodName) throws NotFoundException{  
        ClassPool pool = ClassPool.getDefault();  
        ClassClassPath classPath = new ClassClassPath(cls);  
        pool.insertClassPath(classPath);  
          
        CtClass cc = pool.get(clazzName);  
        CtMethod cm = cc.getDeclaredMethod(methodName);  
        MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
        if (attr == null) {  
            // exception  
        }  
        String[] paramNames = new String[cm.getParameterTypes().length];  
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
        for (int i = 0; i < paramNames.length; i++){  
            paramNames[i] = attr.variableName(i + pos); //paramNames即参数名  
        }  
        return paramNames;  
    }  
	
    /** 
     * @Description: 得到参数的值 （引用数据类型）
     * @param obj 
     */  
    public static String getFieldsValue(Object obj) {  
        Field[] fields = obj.getClass().getDeclaredFields();  
        String typeName = obj.getClass().getTypeName();  
        for (String t : types) {  
            if(t.equals(typeName))  
                return "";  
        }  
        StringBuilder sb = new StringBuilder();  
        sb.append("【");
        for (Field f : fields) {  
            f.setAccessible(true);  
            try {
                for (String str : types) {  
                    if (f.getType().getName().equals(str)){  
                        sb.append(f.getName() + " = " + f.get(obj)+"; ");  
                    }  
                }  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            }  
        }  
        sb.append("】");  
        return sb.toString();  
    }
    
	/**
	 * @Description: 获取缓存key key值规则 返回类型.包名.类名.方法名.参数类型.参数值
	 * @param joinPoint
	 * @return
	 */
	private String getCacheKey(ProceedingJoinPoint joinPoint) {
		Map<String, Object> map = new HashMap<>();// 参数map
		String className = joinPoint.getTarget().getClass().getName();// 获取类路径【com.shop.service.impl.UserInfoServiceImpl】
		String clazzSimpleName = "";
		try {
			Class<?> clazz = Class.forName(joinPoint.getTarget().getClass().getName());// JVM查找并加载指定的类
			clazzSimpleName = clazz.getSimpleName();// 类名
			String clazzName = clazz.getName();// 类全路径
			MethodSignature method = (MethodSignature)joinPoint.getSignature();// 方法
			String[] paramNames = getFieldsName(this.getClass(), clazzName, method.getName());// 获取参数名称数组
			Object[] args = joinPoint.getArgs();// 所有参数的值
			for(int i=0; i<args.length; i++){
				map.put(paramNames[i], args[i]);// 添加到map
			}
			if(map!=null && !map.isEmpty()){
				String prefix = method.getMethod().getAnnotation(RedisCache.class).prefix();// 获取前缀
				String[] params = method.getMethod().getAnnotation(RedisCache.class).params();// 获取参数
				StringBuffer key = new StringBuffer(StringUtils.isBlank(prefix) ? "" : prefix+"_");// 键名 拼接前缀
				key.append(clazzSimpleName+".").append(method.getName());// 拼接类名.方法名
				for(String str : params){// 循环判断方法参数
					if(map.containsKey(str)){
						key.append("_" + map.get(str));
					}
				}
				return key.toString();// 返回键
			}
		} catch (ClassNotFoundException e) {
			logger.error(className + "类没找到");
			e.printStackTrace();
		} catch (NotFoundException e) {
			logger.error("获取参数名称数据错误");
			e.printStackTrace();
		}
		throw new BusinessRuntimeException("缓存错误,没有获取到方法注解");
	}

	
	/**
	 * @Description: 将对象转化为map
	 * @param t
	 *            对象泛型
	 * @return
	 */
	public <T> Map<String, Object> getObjectMap(T t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取传过来的类对象
			Class<? extends Object> clazz = t.getClass();
			// 获取类属性
			Field[] fields = clazz.getDeclaredFields();
			// 遍历属性并将其添加到map中
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(t).toString());
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @Description: 获取指定字段的值
	 * @param key
	 *            字段名
	 * @param t
	 *            对象
	 * @return 字段对应的值
	 */
	public <T> String getObjectKey(String key, T t) {
		try {
			// 获取类对象
			Class<? extends Object> clazz = t.getClass();
			// 类的全部属性
			Field[] fields = clazz.getDeclaredFields();
			// 遍历属性
			for (Field field : fields) {
				// setAccessible 如果类中的成员变量为private时可以将此属性设置为true 表示可以获取该属性的值
				field.setAccessible(true);
				// 找到指定字段
				if (key.equals(field.getName())) {
					// 返回指定字段的值
					return field.get(t).toString();
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}