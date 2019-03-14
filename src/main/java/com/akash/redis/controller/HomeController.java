package com.akash.redis.controller;

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

import com.akash.redis.Constants;
import com.akash.redis.model.dto.BasicPutRequest;
import com.akash.redis.model.dto.GetRequest;
import com.akash.redis.model.dto.ListPutRequest;
import com.akash.redis.model.entity.User;
import com.akash.redis.repo.UserRepository;
import com.akash.redis.service.CacheClientService;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CacheClientService cacheClientService;

	// <------------------------------->
	// Cache via Spring redis template
	// <------------------------------->

	@ResponseBody
	@RequestMapping(value = "/findAllByName/{userName}", method = RequestMethod.GET)
	public String findAllByName(@PathVariable String userName) {
		
		GetRequest<User> getRequest = new GetRequest<>();
		getRequest.setKey(userName);
		getRequest.setNamespace(Constants.USERS_BY_NAME_NAMESPACE);
		List<User> cachedUsers = cacheClientService.getList(getRequest, User.class);
		if(cachedUsers.size() > 0) {
			return cachedUsers.toString();
		}
		
		List<User> users = userRepository.findAllByName(userName);
		
		ListPutRequest<User> putRequest = new ListPutRequest<>();
		putRequest.setNamespace(Constants.USERS_BY_NAME_NAMESPACE);
		putRequest.setKey(userName);
		putRequest.setTtl(Constants.USERS_BY_NAME_TTL);
		putRequest.setValues(users);
		cacheClientService.addToList(putRequest);
		if (users.size() == 0) {
			return "[]";
		}
		return users.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/set", method = RequestMethod.POST)
	public String setKeyValue(HttpServletRequest request) {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		if (key == null || value == null) {
			return "either key or value not available";
		}
		
		BasicPutRequest<String> putRequest = new BasicPutRequest<>();
		putRequest.setKey(key);
		putRequest.setValue(value);
		putRequest.setNamespace(Constants.KEY_VALUE_NAMESPACE);
		putRequest.setTtl(Constants.KEY_VALUE_TTL);
		cacheClientService.set(putRequest);
		return key + " added";
	}

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getkeyValue(HttpServletRequest request) {
		String key = request.getParameter("key");
		if (key != null) {
			
			GetRequest<String> getRequest = new GetRequest<>();
			getRequest.setKey(key);
			getRequest.setNamespace(Constants.KEY_VALUE_NAMESPACE); 
			String value = cacheClientService.get(getRequest, String.class);
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

	@Cacheable(value = "userDataA", key = "#userId"/*, unless = "#result.followers < 12000"*/)
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

	@CachePut(value = "userDataA", key = "#user.id")
	@PutMapping("/update")
	@ResponseBody
	public String updatePersonByID(@RequestBody User user) {
		userRepository.save(user);
		return "added";
	}

	@CacheEvict(value = "userDataA", allEntries = true)
	@DeleteMapping("users/{id}")
	public void deleteUserByID(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}