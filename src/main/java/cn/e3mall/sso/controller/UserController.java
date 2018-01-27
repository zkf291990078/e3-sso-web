package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param, @PathVariable int type) {
		E3Result e3Result = userService.checkUser(param, type);
		return e3Result;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser tbUser) {
		E3Result e3Result = userService.createUser(tbUser);
		return e3Result;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		E3Result result = userService.loginUser(username, password);
		// 3、从返回结果中取token，写入cookie。Cookie要跨域。
		String token = result.getData().toString();
		CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		return result;
	}

	@RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_VALUE + ";chatset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		E3Result result = userService.getUserByToken(token);
		if (StringUtils.isNoneBlank(callback)) {
			return callback + "(" + JsonUtils.objectToJson(result) + ")";
		}
		return JsonUtils.objectToJson(result);
	}
	@RequestMapping(value="/user/logout/{token}", produces = MediaType.APPLICATION_JSON_VALUE + ";chatset=utf-8")
	@ResponseBody
	public E3Result logout(@PathVariable String token){
		E3Result result=userService.logout(token);
		return result;
	}
}
