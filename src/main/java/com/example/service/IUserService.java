package com.example.service;

import java.util.List;

import com.example.ResponseEntity;

import com.example.dto.LoginDto;
import com.example.dto.UserDto;
import com.example.model.ParkingSlotModel;

public interface IUserService {

	ResponseEntity add(UserDto userDto);

	List<UserDto> getAllDetails(String role);

	public String loginUser(LoginDto loginDto);

	LoginDto logout(String token);

	ParkingSlotModel[] getAllByGuard(String token);

	ParkingSlotModel[] getAllByAdmin(String token);

}
