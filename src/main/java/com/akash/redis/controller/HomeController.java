package com.akash.redisEsDemo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akash.redisEsDemo.data.User;
import com.akash.redisEsDemo.repo.UserRepository;
import com.akash.redisEsDemo.service.RedisClientService;

@Controller
public class HomeController {

	// <------------------------------->
	// Redis cache for key value pair arguments via Spring redis template
	// <------------------------------->

	private final String cacheName = "nameList";

	@Autowired
	private RedisClientService redisClientService;

	@RequestMapping(value = "/addToList", method = RequestMethod.POST)
	@ResponseBody
	public String saveName(HttpServletRequest request) {
		String name = request.getParameter("name");
		if (name != null) {
			redisClientService.addToList(cacheName, name);
		} else {
			return "name param not available";
		}
		return name + " added";
	}

	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public String getName() {
		List<String> list = redisClientService.getList(cacheName);
		if (list.size() == 0) {
			return "[]";
		}
		return list.toString();
	}

	@RequestMapping(value = "/set", method = RequestMethod.POST)
	@ResponseBody
	public String setKeyValue(HttpServletRequest request) {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		if (key == null || value == null) {
			return "either key or value not available";
		}
		redisClientService.set(key, value);
		return key + " added";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public String getkeyValue(HttpServletRequest request) {
		String key = request.getParameter("key");
		if (key != null) {
			String value = redisClientService.get(key);
			if (value == null) {
				return "no value available";
			}
			return value;
		}
		return "key not available";
	}

	// <------------------------------->
	// redis cache at method level arguments
	// <------------------------------->

	@Autowired
	private UserRepository userRepository;

	@Cacheable(value = "userData", key = "#userId")
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public String getUser(@PathVariable String userId) {
		System.out.println("hello");
		User user = userRepository.findById(Long.valueOf(userId)).get();
		if(user == null) {
			return "user not found";
		}
		System.out.println(user);
		return user.toString();
	}

	@CachePut(value = "userData", key = "#user.id")
	@PutMapping("/update")
	@ResponseBody
	public String updatePersonByID(@RequestBody User user) {
		userRepository.save(user);
		return "added";
	}

	@CacheEvict(value = "userData", allEntries = true)
	@DeleteMapping("users/{id}")
	public void deleteUserByID(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}