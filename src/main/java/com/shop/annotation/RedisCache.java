package com.shop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解，结合AOP实现Redis自动缓存
 */
@Retention(RetentionPolicy.RUNTIME)//@Retention用来约束注解的生命周期，分别有三个值，源码级别（source），类文件级别（class）或者运行时级别（runtime）
//SOURCE：注解将被编译器丢弃（该类型的注解信息只会保留在源码里，源码经过编译后，注解信息会被丢弃，不会保留在编译好的class文件里）
//CLASS：注解在class文件中可用，但会被VM丢弃（该类型的注解信息会保留在源码里和class文件里，在执行的时候，不会加载到虚拟机中），请注意，当注解未定义Retention值时，默认值是CLASS，如Java内置注解，@Override、@Deprecated、@SuppressWarnning等
//RUNTIME：注解信息将在运行期(JVM)也保留，因此可以通过反射机制读取注解的信息（源码、class文件和执行的时候都有注解的信息），如SpringMvc中的@Controller、@Autowired、@RequestMapping等
@Target({ElementType.METHOD, ElementType.TYPE})//@Target 用来约束注解可以应用的地方
//TYPE 标明该注解可以用于类、接口（包括注解类型）或enum声明
//FIELD 标明该注解可以用于字段(域)声明，包括enum实例
//METHOD 标明该注解可以用于方法声明 
//PARAMETER 标明该注解可以用于参数声明 
//CONSTRUCTOR 标明注解可以用于构造函数声明
//LOCAL_VARIABLE 标明注解可以用于局部变量声明
//ANNOTATION_TYPE 标明注解可以用于注解声明(应用于另一个注解上)
//PACKAGE 标明注解可以用于包声明
//TYPE_PARAMETER 标明注解可以用于类型参数声明（1.8新加入）
//TYPE_USE 类型使用声明（1.8新加入)
@Inherited//@Inherited 可以让注解被继承，但这并不是真的继承，只是通过使用@Inherited，可以让子类Class对象使用getAnnotations()获取父类被@Inherited修饰的注解
@Documented//可以发现使用@Documented元注解定义的注解(@DocumentA)将会生成到javadoc中,而@DocumentB则没有在doc文档中出现，这就是元注解@Documented的作用
public @interface RedisCache {
	long expireTime() default 0;// 失效时间
	TimeUnit timeUnit() default TimeUnit.SECONDS;// 时间单位
	String prefix();// 前缀
	String[] params();// 需要作为key的属性
	Class<?> clazz();// 类
}
