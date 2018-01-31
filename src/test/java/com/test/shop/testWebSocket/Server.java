package com.test.shop.testWebSocket;


import java.io.IOException;  
import java.io.InputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.net.ServerSocket;  
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

	/** 连接端口*/
	private int port;
	/** 是否已经运行*/
	private volatile boolean running=false;
	/** 超时最大时间*/
	private long receiveTimeDelay=30000;
	private Thread connWatchDog;
	private ConcurrentHashMap<Class<?>, ObjectAction> actionMapping = new ConcurrentHashMap<Class<?>,ObjectAction>();
	
	/**
	 * 有参构造
	 */
	public Server(int port) {
		this.port = port;
	}
	
	/**
	 * 要处理客户端发来的对象，并返回一个对象，可实现该接口。
	 */
	public interface ObjectAction{
		Object doAction(Object rev);
	}
	
	public static final class DefaultObjectAction implements ObjectAction{
		public Object doAction(Object rev) {
			System.out.println("处理并返回："+rev);
			return rev;
		}
	}
	
	/**
	 * 启动服务端
	 */
	public void start(){
		// 如果已经运行则直接返回
		if(running){
			return;
		}
		// 运行状态为已经运行
		running=true;
		// 获取线程
		connWatchDog = new Thread(new ConnWatchDog());
		// 线程开始
		connWatchDog.start();
	}
	
	/**
	 * 
	 * @Description: 关闭连接 
	 * @Title: stop 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@SuppressWarnings("deprecation")
	public void stop(){
		if(running)running=false;
		if(connWatchDog!=null)connWatchDog.stop();
	}
	
	public void addActionMap(Class<Object> cls,ObjectAction action){
		actionMapping.put(cls, action);
	}
	
	public static void main(String[] args) {
		int port = 65432;
		Server server = new Server(port);
		server.start();
	}
	
	/**
	 * 
	 * @Description: 线程处理类 
	 * @ClassName: ConnWatchDog 
	 * @author liquor
	 * @date 2017年9月2日 下午3:01:47 
	 *
	 */
	class ConnWatchDog implements Runnable{
		@SuppressWarnings("resource")
		public void run(){
			try {
				ServerSocket ss = new ServerSocket(port,5);
				while(running){
					Socket s = ss.accept();// 取出连接
					new Thread(new SocketAction(s)).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Server.this.stop();
			}
			
		}
	}
	
	/**
	 * 
	 * @Description: 连接处理类 
	 * @ClassName: SocketAction 
	 * @author liquor
	 * @date 2017年9月2日 下午3:03:37 
	 *
	 */
	class SocketAction implements Runnable{
		Socket s;
		boolean run=true;
		// 记录开始连接时间
		long lastReceiveTime = System.currentTimeMillis();
		public SocketAction(Socket s) {
			this.s = s;
		}
		@SuppressWarnings("resource")
		public void run() {
			while(running && run){
				if(System.currentTimeMillis()-lastReceiveTime>receiveTimeDelay){
					overThis();
				}else{
					try {
						InputStream in = s.getInputStream();
						if(in.available()>0){
							ObjectInputStream ois = new ObjectInputStream(in);
							Object obj = ois.readObject();
							lastReceiveTime = System.currentTimeMillis();
							System.out.println("接收：\t"+obj);
							ObjectAction oa = actionMapping.get(obj.getClass());
							oa = oa==null?new DefaultObjectAction():oa;
							Scanner sc = new Scanner(System.in);
							String msg = sc.next();
							Object out = oa.doAction(msg);
							if(out!=null){
								ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
								oos.writeObject(out);
								oos.flush();
							}
						}else{
							Thread.sleep(10);
						}
					} catch (Exception e) {
						e.printStackTrace();
						overThis();
					} 
				}
			}
		}
		
		/**
		 * 
		 * @Description: 关闭连接 
		 * @Title: overThis 
		 * @param     设定文件 
		 * @return void    返回类型 
		 * @throws 
		 * @author liquor
		 */
		private void overThis() {
			if(run){
				run=false;
			}
			if(s!=null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("关闭："+s.getRemoteSocketAddress());
		}
		
	}
	
}
