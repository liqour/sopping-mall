package com.test.shop.testWebSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.shop.service.IUserInfoService;

/**
 * 
 * @Description: 长连接 
 * @ClassName: LongConnect 
 * @author liquor
 * @date 2017年9月2日 下午3:47:35 
 *
 */
@SuppressWarnings("unused")
public class LongConnect {
	
	private static final Logger LOGGER = Logger.getLogger(LongConnect.class);
	
	/** 用户管理类*/
	@Autowired
	private IUserInfoService userInfoService;
	/** 端口号*/
	private static int port;
	/** 运行状态*/
	private static volatile boolean running = false;
	/** 最大连接时间*/
	private static final Long receiveTimeDelay = 30000L;
	/** 连接线程*/
	private Thread connWatchDog;
	/** 最大连接数*/
	private static final Integer MAX_CONNECT = 5;
	/** 响应消息*/
	private String msg;
	
	/** 有参构造设置连接端口号*/
	public LongConnect(int port){
		LongConnect.port = port;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	/** 启动服务端*/
	public void start(){
		// 如果客户端已经启动则不需要重新启动
		if(running){
			LOGGER.info("多次启动服务端。。。。。");
			return;
		}
		// 设置启动状态
		running = true;
		// 获取会话
		new Thread(new ConnWatchDog()).start();
		LOGGER.info("================================》服务端启动《================================");
	}
	
	/**
	 * 
	 * @Description: 内部类获取socket连接 
	 * @ClassName: ConnWatchDog 
	 * @author liquor
	 * @date 2017年9月2日 下午5:15:44 
	 *
	 */
	class ConnWatchDog implements Runnable{

		@SuppressWarnings("resource")
		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port,MAX_CONNECT);
				while(running){
					// 从服务端取出一个连接
					Socket socket = serverSocket.accept();
					// 开始会话
					new Thread(new SocketAction(socket)).start();
				}
			} catch (IOException e) {
				LOGGER.info("创建socket服务端错误");
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * 
	 * @Description: 内部类处理连接信息 
	 * @ClassName: SocketAction 
	 * @author liquor
	 * @date 2017年9月2日 下午5:22:18 
	 *
	 */
	class SocketAction implements Runnable{

		/** socket连接*/
		private Socket socket;
		/** 连接状态*/
		private Boolean run = true;
		/** 开始连接的时间*/
		private Long lastReceiveTime = System.currentTimeMillis();
		
		/**
		 * 构造方法设置连接
		 */
		public SocketAction(Socket socket) {
			this.socket = socket;
		}
		
		/**
		 * 消息接收和响应
		 */
		@Override
		public void run() {
			/** 当服务器端已启动，且当前连接未关闭时*/
			while(running && run){
				// 判断如果连接已经超时
				if(System.currentTimeMillis()-lastReceiveTime>receiveTimeDelay){
					stopConnect();
				}else{
					try {
						// 获取客户端传过来的消息
						InputStream in = socket.getInputStream();
						// 接受到客户端消息
						if(in.available()>0){
							// 重置上一次会话时间
							lastReceiveTime = System.currentTimeMillis();
							//读取客户端消息
							ObjectInputStream ois = new ObjectInputStream(in);
							Object obj = ois.readObject();
							LOGGER.info("接收到消息："+obj);
							// 响应客户端消息
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							if(StringUtils.isEmpty(userInfoService.getUserMsg(null))){
								
							}
							oos.writeObject("");
							oos.flush();
						}
					} catch (IOException e) {
						stopConnect();
						LOGGER.info("获取客户端消息失败,连接关闭");
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						stopConnect();
						LOGGER.info("读取客户端消息失败,连接关闭");
						e.printStackTrace();
					}
				}
			}
		}
		
		/**
		 * 
		 * @Description: 设置连接状态为已关闭,并关闭当前会话 
		 * @Title: stopConnect 
		 * @param     设定文件 
		 * @return void    返回类型 
		 * @throws 
		 * @author liquor
		 */
		private void stopConnect(){
			if(run){
				run = false;
			}
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					LOGGER.info("关闭会话失败");
					e.printStackTrace();
				}
			}
			LOGGER.info("连接："+socket.getRemoteSocketAddress()+"已断开");
		}
	}
}
