package com.bsep.admin;

import com.bsep.admin.config.AppProperties;
import com.bsep.admin.myHouse.DeviceService;
import com.bsep.admin.myHouse.RulesService;
import com.bsep.admin.repository.GroceryItemRepository;
import com.bsep.admin.repository.UserRepository;
import com.bsep.admin.util.Trie;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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


	@PostConstruct
	public void startDeviceManager() {
		// start python script
		String pythonCommand = "python";
		String pythonScript = "./devices/device_manager.py";
		ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, pythonScript);
		try {
			// redirect output to log file
			processBuilder.redirectOutput(new File("./devices/device_manager.log"));

			Process process = processBuilder.start();
//			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private RulesService rulesService;

//	@PostConstruct
//	// don't run for test, only for development
//	@Profile("dev")
//	public void testDevices() {
//		deviceService.addDevice("LAMP", "", 1L);
//		RuleCreationDto ruleDto = new RuleCreationDto();
//		ruleDto.setName("Lamp rule");
//		ruleDto.setDeviceType(DeviceType.LAMP);
//		ruleDto.setNum(3);
//		ruleDto.setAlarmText("Lampa se previse ukljucuje");
//		ruleDto.setOperatorNum(">=");
//		rulesService.addRule(ruleDto);
//	}

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
