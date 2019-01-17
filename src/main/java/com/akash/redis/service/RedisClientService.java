package com.akash.redis.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisClientService {

	// inject the actual template
	@Autowired
	private RedisTemplate<String, String> template;

	// inject the template as ListOperations
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;
	
	public void addToList(String key, String value) {
		listOps.rightPush(key, value);
	}

	public void addleftToList(String key, String value) {
		listOps.leftPush(key, value);
	}

	public List<String> getList(String key) {
		long size = listOps.size(key);
		if (size == 0) {
			return Collections.emptyList();
		}
		return listOps.range(key, 0, size);
	}
	
	public void set(String key, String value) {
		valueOps.set(key, value);
	}
	
	public String get(String key) {
		return valueOps.get(key);
	}
}