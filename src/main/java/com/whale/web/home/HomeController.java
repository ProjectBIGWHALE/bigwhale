package com.whale.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home() {
	
		return "home";
	}
	
	@RequestMapping(value="/help", method=RequestMethod.GET)
	public String help() {
	
		return "help";
	}
	
	@RequestMapping(value="/supportus", method=RequestMethod.GET)
	public String supportUs() {
	
		return "supportus";
	}
	
}
