package com.whale.web.homeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String homePage() {
	
		return "index";
	}
	
	@RequestMapping(value="/ajuda", method=RequestMethod.GET)
	public String help() {
	
		return "ajuda";
	}
	
	@RequestMapping(value="/apoie", method=RequestMethod.GET)
	public String apoie() {
	
		return "apoie";
	}
	
}
