package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.example.ResponseEntity;

import com.example.dto.LoginDto;
import com.example.dto.ParkingSlotModel;
import com.example.dto.UserDto;
import com.example.service.IUserService;

@RestController
public class UserController {

	@Autowired
	IUserService userService;
	
	@PostMapping("/addUser")
	public ResponseEntity AddUser(@RequestBody UserDto userDto) {
		ResponseEntity user = userService.add(userDto);
		return new ResponseEntity(user, "User added succesfully");
	}
	
	@GetMapping("/listAll")
	public List<UserDto> getAllDetails(String role) {
		return this.userService.getAllDetails(role);
	}
	
	@GetMapping("/login")
	public ResponseEntity loginUser(@RequestBody LoginDto loginDto) {
		String login = userService.loginUser(loginDto);
		return new ResponseEntity(login, "Login successfully");
	}
	
	@GetMapping("/logout")
	public ResponseEntity logout(@RequestHeader String token) {
		LoginDto logout = userService.logout(token);
		return new ResponseEntity(logout, "Logout successfully");
	}
	
	@GetMapping("/listAllUser")
	public ParkingSlotModel[] getAll(@RequestHeader String token) {
		return this.userService.getAllByGuard(token);
	}
	
	@GetMapping("/listAllByAdmin")
	public ParkingSlotModel[] getAllByAdmin(@RequestHeader String token) {
		return this.userService.getAllByAdmin(token);
	}
}
