package com.shop.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
 * 
 * @Description: 动态生成实体 
 * @ClassName: DynamicBean 
 * @author liquor
 * @date 2017年11月4日 上午11:19:19 
 *
 */
public class DynamicBean {

	private Object object = null;// 动态生成的类
	private BeanMap beanMap = null;// 存放属性名称以及属性的类型

	public DynamicBean() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public DynamicBean(Map propertyMap) {
		this.object = generateBean(propertyMap);
		this.beanMap = BeanMap.create(this.object);
	}

	/**
	 * 给bean属性赋值
	 * @param property 属性名
	 * @param value 值
	 */
	public void setValue(Object property, Object value) {
		beanMap.put(property, value);
	}

	/**
	 * 通过属性名得到属性值
	 * @param property 属性名
	 * @return 值
	 */
	public Object getValue(String property) {
		return beanMap.get(property);
	}

	/**
	 * 得到该实体bean对象
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Object generateBean(Map propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		Set keySet = propertyMap.keySet();
		for (Iterator i = keySet.iterator(); i.hasNext();) {
			String key = (String) i.next();
			generator.addProperty(key, (Class) propertyMap.get(key));
		}
		return generator.create();
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Map<Object, Object> map = new HashMap<>();
		map.put("name", Class.forName("java.lang.String"));
		DynamicBean d = new DynamicBean(map);
		d.setValue("name", "张珊");
		System.out.println(d.getValue("name"));
	}
	
}