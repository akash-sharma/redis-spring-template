package com.akash.redis.service.impl;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

public class JsonRedisSerializer {

	private static final ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	public static String serialize(Object object) {
		if(object instanceof String) {
			return object.toString();
		}
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("exception occured while serialize : " + e.getMessage(), e);
		}
	}

	public static <T> T deserialize(String string, Class<T> clazz) {
		try {
			return mapper.readValue(string, clazz);
		} catch (IOException e) {
			throw new RuntimeException("exception occured while deserialize : " + e.getMessage(), e);
		}
	}
}
