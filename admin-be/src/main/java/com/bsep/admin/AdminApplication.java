package com.bsep.admin;

import com.bsep.admin.config.AppProperties;
import com.bsep.admin.repository.GroceryItemRepository;
import com.bsep.admin.repository.UserRepository;
import com.bsep.admin.util.Trie;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.security.Security;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableMongoRepositories
@EnableConfigurationProperties(AppProperties.class)
public class AdminApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Trie trie;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Security.addProvider(new BouncyCastleProvider());
	}


	@Bean
	public KieContainer makeContainer() {
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
//		kieFileSystem.write("src/main/resources/rules.drl", kieServices.getResources().newClassPathResource("rules.drl"));
		kieBuilder.buildAll();
		return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
	}

	// fill trie with all common passwords txt file
	@PostConstruct
	public void fillTrie() {
		try {
			File file = new File("src/main/resources/common_passwords.txt");
			java.util.Scanner scanner = new java.util.Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				trie.insert(line);
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Autowired
	private GroceryItemRepository groceryItemRepository;

//	@PostConstruct
//	public void testMongoDb() {
//		// insert few items
//		groceryItemRepository.save(new com.bsep.admin.model.GroceryItem("1", "Milk", 2, "Dairy"));
//		groceryItemRepository.save(new com.bsep.admin.model.GroceryItem("2", "Bread", 1, "Bakery"));
//		groceryItemRepository.save(new com.bsep.admin.model.GroceryItem("3", "Eggs", 12, "Dairy"));
//	}

//	@PostConstruct
//	public void testDevice() {
//		String pythonCommand = "python";
//		String pythonScript = "./devices/lamp.py";
//		ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, pythonScript, "LAMP_1");
//
//		try {
//			processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//			processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
//
//			Process process = processBuilder.start();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
