package com.akash.redis.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.akash.redis.Constants;
import com.akash.redis.model.dto.BasicPutRequest;
import com.akash.redis.model.dto.GetRequest;
import com.akash.redis.model.dto.ListPutRequest;
import com.akash.redis.service.CacheClientService;

@Service
public class RedisClientService implements CacheClientService {

	// inject the actual template
	@Autowired
	private RedisTemplate<String, String> template;

	// inject the template as ListOperations
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;
	
	@Override
	public <T> void set(BasicPutRequest<T> putRequest) {
		String key = putRequest.getNamespace() + Constants.SEPARATOR + putRequest.getKey();
		String value = JsonRedisSerializer.serialize(putRequest.getValue());
		if(value != null) {
			valueOps.set(key, value);
		}
		template.expire(key, putRequest.getTtl(), TimeUnit.SECONDS);
	}
	
	@Override
	public <T> T get(GetRequest<T> request, Class<T> clazz) {
		String key = request.getNamespace() + Constants.SEPARATOR + request.getKey();
		String value = valueOps.get(key);
		if(value != null) {
			return JsonRedisSerializer.deserialize(value, clazz);
		}
		return null;
	}
	
	@Override
	public <T> void addToList(ListPutRequest<T> putRequest) {
		String key = putRequest.getNamespace() + Constants.SEPARATOR + putRequest.getKey();
		for(T object : putRequest.getValues()) {
			String value = JsonRedisSerializer.serialize(object);
			if(value != null) {
				listOps.rightPush(key, value);
			}
		}
		template.expire(key, putRequest.getTtl(), TimeUnit.SECONDS);
	}

	@Override
	public <T> List<T> getList(GetRequest<T> request, Class<T> clazz) {
		String key = request.getNamespace() + Constants.SEPARATOR + request.getKey();
		List<String> values = listOps.range(key, 0, -1);
		List<T> objects = new ArrayList<>();
		for(String value : values) {
			objects.add(JsonRedisSerializer.deserialize(value, clazz));
		}
		return objects;
	}
	
	@Override
	public void deleteKey(String key) {
		template.delete(key);
	}
}
