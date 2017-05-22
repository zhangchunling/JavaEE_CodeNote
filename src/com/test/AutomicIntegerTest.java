package com.test;

import java.util.concurrent.atomic.AtomicInteger;

public class AutomicIntegerTest {

	private AtomicInteger localAi = new AtomicInteger(0);
	private static int num=0;
	// 异步设置缓存
	public void setHomePageLocalCache() {
		System.out.println(localAi.get()+"...");
		if (localAi.getAndIncrement() == 0) {
			
			num++;
			System.out.println(localAi.get()+"===num:"+num);
			localAi.set(0);
		}
	}
	
	
	private class MyThread extends Thread{	
		private AutomicIntegerTest c;
		public MyThread(AutomicIntegerTest c){
			this.c = c;
		}
		@Override
		public void run() {
			c.setHomePageLocalCache();
		}
	}
	
	public void test(){
		 AutomicIntegerTest c = new AutomicIntegerTest();
		 for(int i=0;i<10;i++){
			 new MyThread(c).start();
		 }
		
		new MyThread(c).start();
	}
	public static void main(String[] args) {
		new AutomicIntegerTest().test();
	}
}
