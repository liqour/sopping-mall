package com.shop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: 计算方法用时注解实现
 * @author liquor
 *
 */
@Aspect
@Component
public class ExcutionTimeAspect {

	/**
	 * @Description: 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExcutionTimeAspect.class);
	
	/**
	 * @Description: 切点
	 */
    @Pointcut("@annotation(ExcutionTime)")
    private void setJoinPoint() {
    }

    /**
     * @Description: 方法执行时对方法的操作
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            //前置通知
            result = proceedingJoinPoint.proceed();
            //返回通知
        } catch (Throwable e) {
            //异常通知
            e.printStackTrace();
        }
        //后置通知
        long end = System.currentTimeMillis();
        logger.info(String.format("==============================================>方法：%s,用时%s ms<=========================================",proceedingJoinPoint.getSignature().getName(),(end-start)));
        return result;
    }

}
