package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class showIndexController {
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model){
		model.addAttribute("redirect",redirect);
		return "login";
	}
}
