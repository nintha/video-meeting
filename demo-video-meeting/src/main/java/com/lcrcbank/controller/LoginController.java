package com.lcrcbank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lcrcbank.entity.User;
import com.lcrcbank.mapper.UserMapper;
import com.lcrcbank.meeting.RoomManager;

@RestController
public class LoginController {
	
	private final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	RoomManager roomManager;
	
	private Gson gson = new Gson();

	@RequestMapping("/user")
	public String getUserAuthList() {
		List<User> userList = userMapper.findAll();
		String json = gson.toJson(userList); 
		log.info(json);
		return json;
	}
	
	@RequestMapping("/login")
	public String login(HttpSession httpSession,String username,String password) {
		User user = userMapper.findByUsername(username);
		Map<String, Object> map = new HashMap<String,Object>();
		if(user == null){
			map.put("error", 1);
		}else if(user.getPassword().equals(password)){
			map.put("error", 0);
			map.put("data", user);
			httpSession.setAttribute("currentUser", user);
		}else{
			map.put("error", 2);
		}
		String json = gson.toJson(map); 
		log.info("login: "+json);
		return json;
	}
	
	@RequestMapping("/logout")
	public String login(HttpSession httpSession) {
		Map<String, Object> map = new HashMap<String,Object>();
		User user = (User) httpSession.getAttribute("currentUser");
		if(user == null){
			map.put("error", 1);
		}else{
			map.put("error", 0);
			map.put("data", user);
			httpSession.removeAttribute("currentUser");
		}
		String json = gson.toJson(map); 
		log.info("login: "+json);
		return json;
	}
	
	@RequestMapping("/currentUser")
	public String getCurrentUser(HttpSession httpSession) {
		Map<String, Object> map = new HashMap<String,Object>();
		User user = (User) httpSession.getAttribute("currentUser");
		if(user == null){
			map.put("error", 1);
		}else{
			map.put("error", 0);
			map.put("data", user);
		}

		String json = gson.toJson(map); 
		log.info("currentUser:"+json);
		return json; 
	}
	
	@RequestMapping("/newRoom")
	public String randomRoom() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("data", roomManager.createRoom());

		String json = gson.toJson(map); 
		log.info("newRoom: "+json);
		return json;
	}
	
	@RequestMapping("/allRoom")
	public String getAllRoom() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("data", roomManager.getAll());

		String json = gson.toJson(map); 
		log.info("allRoom: "+json);
		return json;
	}
	
	
}
