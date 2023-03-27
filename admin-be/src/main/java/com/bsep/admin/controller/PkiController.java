package com.bsep.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pki")
public class PkiController {

	// health endpoint
	@GetMapping("/health")
	public String health() {
		return "OK";
	}

}
