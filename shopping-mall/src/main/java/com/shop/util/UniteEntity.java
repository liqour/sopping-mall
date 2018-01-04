package com.shop.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.shop.entity.GoodsInfo;
import com.shop.entity.UserInfo;

/**
 * @Description: 联合实体类
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class UniteEntity {
    
    //联合实体对象集合
    List<Object> entitys = null;
    //方法名称
    String fnName = "";
    //方法对象
    Method method = null;
    
    /**
     * 构造方法
     * @param entitys 需要联合的实体对象集合
     */
    public UniteEntity(List<Object> entitys){
        this.entitys = entitys;
    }
    
    /**
     * 获取某个取值方法
     * @param fnName 方法名称
     * @return 该取值方法返回值类型
     */
	public Class getFunction(String fnName){
        //保存方法名称
        this.fnName = fnName;
        //查找方法
        Method m = findMethod();
        //判断方法是否存在
        if(m != null){
            //获取目标方法的返回值类型
            Class type = m.getReturnType();
            //保存取值方法对象
            method = m;
            //返回该取值方法返回值类型
            return type;
        }else{
            return null;
        }
    }
    
    /**
     * 获取某个赋值方法
     * @param fnName 方法名称
     * @return 该赋值方法参数类型
     */
    public Class setFunction(String fnName){
        //保存方法名称
        this.fnName = fnName;
        //查找方法
        Method m = findMethod();
        //判断方法是否存在
        if(m != null){
            //获取目标方法的参数类型
            Class type = m.getParameterTypes()[0];
            //保存赋值方法对象
            method = m;
            //返回该赋值方法参数类型
            return type;
        }else{
            return null;
        }
    }
    
    /**
     * 调用某个方法，为属性赋值
     * @param <T> 赋值方法的参数类型
     * @param c
     * @param value 值内容
     */
    public <T> void setValue(Class<T> c,T value){
        //遍历实体类集合
        for(Object o : entitys){
            //出错继续执行
            try{
                method.invoke(o, value);
                break;
            }catch(Exception ex){}
        }
    }
    
    /**
     * 调用某个方法，取得属性的值
     * @param <T> 取值方法的返回值类型
     * @param c
     * @return 取得值的内容
     */
	public <T> T getValue(Class<T> c){
        //遍历实体类集合
        for(Object o : entitys){
            //出错继续运行
            try{
                //由于invoke返回的是Object类型，因此要强制转换成T类型
                return (T)method.invoke(o);
            }catch(Exception ex){}
        }
        return null;
    }
    
    /**
     * 从实体对象集合中查找某个方法
     * @return 方法对象
     */
    private Method findMethod(){
        //遍历集合，寻找方法
        for(Object o : entitys){
            //保证出错能继续运行
            try{
                //获取对象所有公有方法
                Method[] methods = o.getClass().getMethods();
                
                //遍历方法
                for(Method m : methods){
                    //匹配是否有目标方法
                    if(fnName.equals(m.getName())){
                        //返回方法对象
                        return m;
                    }
                }
            }catch(Exception ex){}
        }
        return null;
    }
    
    public static void main(String[] args) {
    	//创建一个对象集合
    	List<Object> list = new ArrayList<Object>();

    	//将需要融合的实体类填入集合
    	list.add(new UserInfo());
    	list.add(new GoodsInfo());

    	//创建联合实体类对象
    	UniteEntity ue = new UniteEntity(list);

    	String i = "张三";

    	//调用实体类中方法名为setEnno的方法(赋值方法)，并给一个参数i
    	ue.setValue(ue.setFunction("setUserName"), i);
    	//调用实体类中方法名为getEnno的方法(取值方法)，并打印返回值
    	System.out.println(ue.getValue(ue.getFunction("getUserName")));
	}
}
