package com.test.shop.thread;

import com.shop.util.ThreadUtil;

/**
 * 
 * @Description: 线程池测试
 * @ClassName: ThreadTest
 * @author liquor
 * @date 2017年9月2日 上午11:08:49
 *
 */
public class ThreadTest {

	public static void main(String[] args) throws Exception {
		ThreadUtil.getLongTimeOutThread(new Runnable() {
			public void run() {
				System.out.println("线程1");
			}
		});
		ThreadUtil.getLongTimeOutThread(new Runnable() {
			public void run() {
				System.out.println("线程2");
			}
		});
		ThreadUtil.getLongTimeOutThread(new Runnable() {
			public void run() {
				System.out.println("线程3");
			}
		});
		ThreadUtil.getLongTimeOutThread(new Runnable() {
			public void run() {
				System.out.println("线程4");
			}
		});
		ThreadUtil.getLongTimeOutThread(new Runnable() {
			public void run() {
				System.out.println("线程状态");
				ThreadUtil.getThreadState();
			}
		});
	}

}
