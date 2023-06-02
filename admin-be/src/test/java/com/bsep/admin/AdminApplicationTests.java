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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

@SpringBootTest
class AdminApplicationTests {

	@Autowired
	private RulesService rulesService;

	@Test
	public void testAddRule() {
		RuleDto ruleDto = new RuleDto();
		ruleDto.setName("TEST");
		ruleDto.setNum(1);
		ruleDto.setOperatorNum(">=");
		rulesService.addRule(ruleDto);


		Message message = new Message();
		UUID uuid = UUID.randomUUID();
		message.setId(uuid);
		message.setText("Testno pravilo");
		message.setType("INFO");
		message.setDeviceType(DeviceType.GATE);
		message.setValue(20.0);

		rulesService.addMessage(message);

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


}
