package com.bsep.admin;

import com.bsep.admin.model.DeviceType;
import com.bsep.admin.model.Message;
import com.bsep.admin.myHouse.RulesService;
import com.bsep.admin.myHouse.dto.RuleDto;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.OIDTokenizer;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

@SpringBootTest
@Profile("test")
class AdminApplicationTests {

	@Autowired
	private RulesService rulesService;

	@Test
	public void testAddRule() {
		RuleDto ruleDto = new RuleDto();
		ruleDto.setName("TEST");
		ruleDto.setNum(3);
		ruleDto.setOperatorNum(">=");
		rulesService.addRule(ruleDto);

		UUID deviceId = UUID.randomUUID();

		Message message1 = new Message();
		UUID uuid = UUID.randomUUID();
		message1.setId(uuid);
		message1.setText("Testno pravilo");
		message1.setType("INFO");
		message1.setDeviceType(DeviceType.GATE);
		message1.setDeviceId(deviceId);
		message1.setValue(20.0);

		Message message2 = new Message();
		uuid = UUID.randomUUID();
		message2.setId(uuid);
		message2.setText("Testno pravilo");
		message2.setType("INFO");
		message2.setDeviceType(DeviceType.GATE);
		message2.setDeviceId(deviceId);
		message2.setValue(20.0);

		Message message3 = new Message();
		uuid = UUID.randomUUID();
		message3.setId(uuid);
		message3.setText("Testno pravilo");
		message3.setType("INFO");
		message3.setDeviceType(DeviceType.GATE);
		message3.setDeviceId(deviceId);
		message3.setValue(20.0);

		Message message4 = new Message();
		uuid = UUID.randomUUID();
		message4.setId(uuid);
		message4.setText("Testno pravilo");
		message4.setType("INFO");
		message4.setDeviceType(DeviceType.GATE);
		message4.setDeviceId(deviceId);
		message4.setValue(20.0);

		rulesService.addMessage(message1);
		rulesService.addMessage(message2);
		rulesService.addMessage(message3);
		rulesService.addMessage(message4);



		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


}
