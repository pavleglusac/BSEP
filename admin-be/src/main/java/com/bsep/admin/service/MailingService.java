package com.bsep.admin.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailingService {

	@Autowired
	private JavaMailSender mailSender;

	private final Path templatesLocation;

	@Autowired
	public MailingService() {
		templatesLocation = Paths.get("src", "main", "resources", "templates");
	}

	@Async
	public void sendTestMail() {
		String content = renderTemplate("test.html", Map.of("name", "John"));
		sendMail("bsepml23@gmail.com", "Test", content);
	}

	private void sendMailWithAttachment(String to, String subject, String body, File attachment) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setText(body, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.addAttachment(attachment.getName(), attachment);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private void sendMail(String to, String subject, String body) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setText(body, true);
			helper.setTo(to);
			helper.setSubject(subject);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String renderTemplate(String templateName, String... variables) {
		Map<String, String> variableMap = new HashMap<>();

		List<String> keyValueList = Arrays.stream(variables).collect(Collectors.toList());

		if (keyValueList.size() % 2 != 0)
			throw new IllegalArgumentException();

		for (int i = 0; i < keyValueList.size(); i += 2) {
			variableMap.put(keyValueList.get(i), keyValueList.get(i + 1));
		}

		return renderTemplate(templateName, variableMap);
	}

	private String renderTemplate(String templateName, Map<String, String> variables) {
		File file = templatesLocation.resolve(templateName).toFile();
		String message = null;
		try {
			message = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String target, renderedValue;
		for (var entry : variables.entrySet()) {
			target = "\\{\\{ " + entry.getKey() + " \\}\\}";
			renderedValue = entry.getValue();

			message = message.replaceAll(target, renderedValue);
		}

		return message;
	}


}