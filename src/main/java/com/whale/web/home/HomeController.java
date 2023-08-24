package com.whale.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping(value="/")
	public String home() {
	
		return "home";
	}
	
	@GetMapping(value="/help")
	public String help() {
	
		return "help";
	}
	
	@GetMapping(value="/supportus")
	public String supportUs() {
	
		return "supportus";
	}
	
}
