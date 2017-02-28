package com.study.网络编程.TCP协议网络编程.Demo.CS系统.私聊;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 保存聊天室所有用户和对应的Socket之间的映射关系：这样服务器就可根据用户名找到对应的Socket。
 * 但实际上这里保存的是用户名和对应输出流之间的映射关系，因为服务器只需获取输出流即可。
 * 《该类既可通过用户名找到对应的输出流，也可根据输出流找到对应的用户名》
 * @author Peter
 *
 */
public class MyMap<K,V> extends HashMap<K, V>{

	/**
	 * 重写HashMap的put方法，该方法不允许value重复
	 * HashMap中key不能重复，值可以重复，所以我们得自己重写
	 */
	public V put(K key,V value){
		//遍历所有value组成的集合,如果新加入的value与原有的val集合中的一个相同，则抛出异常：RuntimeException
		for(V val : this.getValueSet()){
			if(val.equals(value)&&val.hashCode()==value.hashCode()){
				throw new RuntimeException("您添加的value值重复！");
			}
		}
		return super.put(key, value);//HaspMap中的put方法返回的也是V
	}
	/**
	 * 获取所有value组成的Set集合
	 * @return
	 */
	public Set<V> getValueSet(){
		Set<V> result = new HashSet<V>();
		//遍历key组成的集合，然后将每个key对应的value添加到result中
		for(K key: super.keySet()){
			result.add(super.get(key));
		}
		return result;
	}
	
	/**
	 * 根据value来删除指定项
	 * @param val
	 */
	public void removeByValue(Object val){
		for(Object key : keySet()){
			//==用于比较两个变量的值是否相等，对于引用类型比较的是两个引用变量存的堆的地是否相等
			if(super.get(key)==val){
				super.remove(key);
				break;
			}
		}
	}
	
	/**
	 * 根据value查找key
	 * @param val
	 * @return
	 */
	public K getKeyByValue(V val){
		for(K key : super.keySet()){
			//==用于比较两个变量的值是否相等，对于引用类型比较的是两个引用变量存的堆的地是否相等
			//equals比较的是堆中两个对象的内容是否相等。
			//map判断值是否相等，必须要两个条件同时满足。
			if(super.get(key)==val&&super.get(key).equals(val)){
				return key;
			}
		}
		return null;
	}
}
