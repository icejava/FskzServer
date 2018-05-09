package com.pica.fskz;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存
 * 
 */
public class LocalCache {

	private static final LocalCache LOCAL_CACHE = new LocalCache();

	private ConcurrentHashMap<String, Integer> cache = new ConcurrentHashMap<String, Integer>();

	private LocalCache() {
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public Integer get(String key) {
		return cache.get(key);
	}

	public Set<String> keySet() {
		return cache.keySet();
	}

	public void put(String key, Integer value) {
		cache.put(key, value);
	}

	public void remove(String key) {
		cache.remove(key);
	}

	public int size() {
		return cache.size();
	}

	public static LocalCache getInstance() {
		return LOCAL_CACHE;
	}

	/*
	 * 请求次数加1
	 */
	public void timesPlus(String key){
		if(containsKey(key)){
			put(key, get(key)+1);
		}else{
			put(key, 1);
		}
	}
}
