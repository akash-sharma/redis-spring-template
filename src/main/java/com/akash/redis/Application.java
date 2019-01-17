package com.akash.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import com.akash.redis.data.User;
import com.akash.redis.repo.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = { "com.akash.redis.*" })
// @PropertySource("classpath:/config/${java_api_env_name}/config.properties")
@EnableCaching
public class Application implements CommandLineRunner {

	private static Properties configProperties = null;
	private static final String configFolderVariableName = "java_api_env_name";
	private static final String configFolderName = "config";
	private static final String configFileName = "config.properties";
	private static final String dev = "development";

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {

		// loadConfig();
		SpringApplication.run(Application.class, args);
	}

	private static void loadConfig() {
		InputStream configInputStream = null;
		try {
			configProperties = new Properties();
			String environment = dev;
			if (Objects.nonNull(System.getProperty(configFolderVariableName))) {
				environment = String.valueOf(System.getProperty(configFolderVariableName));
			} else if (Objects.nonNull(System.getenv(configFolderVariableName))) {
				environment = String.valueOf(System.getenv(configFolderVariableName));
			}
			String configFileFullName = configFolderName + "/" + environment + "/" + configFileName;
			configInputStream = Application.class.getClassLoader().getResourceAsStream(configFileFullName);
			if (Objects.isNull(configInputStream)) {
				return;
			}
			// load a properties file from class path, inside static method
			configProperties.load(configInputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (configInputStream != null) {
				try {
					configInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// Populating embedded database here
		User shubham = new User("Shubham1", 21000l);
		User pankaj = new User("Pankaj1", 29100l);
		User lewis = new User("Lewis1", 5501l);

		userRepository.save(shubham);
		userRepository.save(pankaj);
		userRepository.save(lewis);
	}
}