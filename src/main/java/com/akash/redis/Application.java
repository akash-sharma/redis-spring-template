package com.akash.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import com.akash.redis.model.entity.User;
import com.akash.redis.repo.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = { "com.akash.redis.*" })
@EnableCaching
public class Application implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// Populating embedded database here
		User rahul1 = new User("rahul", 21000l);
		User pankaj = new User("Pankaj", 29100l);
		User rahul2 = new User("rahul", 5501l);
		User ram = new User("ram", 5501l);
		userRepository.save(rahul1);
		userRepository.save(pankaj);
		userRepository.save(rahul2);
		userRepository.save(ram);
	}
}