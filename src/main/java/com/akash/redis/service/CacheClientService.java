package com.akash.redis.service;

import java.util.List;

import com.akash.redis.model.dto.BasicPutRequest;
import com.akash.redis.model.dto.GetRequest;
import com.akash.redis.model.dto.ListPutRequest;

public interface CacheClientService {

	<T> void addToList(ListPutRequest<T> putRequest);
	
	<T> List<T> getList(GetRequest<T> request, Class<T> clazz);
	
	<T> void set(BasicPutRequest<T> putRequest);
	
	<T> T get(GetRequest<T> request, Class<T> clazz);
	
	void deleteKey(String key);
}