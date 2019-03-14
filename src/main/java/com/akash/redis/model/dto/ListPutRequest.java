package com.akash.redis.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListPutRequest<T> {

	private String namespace;
	private String key;
	
	private List<T> values = new ArrayList<>();
	private long ttl;
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	public List<T> getValues() {
		return values;
	}
	public void setValues(List<T> values) {
		this.values = values;
	}
}