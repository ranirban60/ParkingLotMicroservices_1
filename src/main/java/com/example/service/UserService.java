package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ResponseEntity;

import com.example.dto.LoginDto;
import com.example.dto.ParkingSlotModel;
import com.example.dto.UserDto;
import com.example.exception.UserException;
import com.example.model.UserModel;
import com.example.repository.IUserRepo;
import com.example.utility.JwtTokenUtil;

@Service
public class UserService implements IUserService {

	@Autowired
	IUserRepo userRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public ResponseEntity add(UserDto userDto) {
		Optional<UserModel> userModel = userRepo.findByEmail(userDto.getEmail());
		if (userModel.isPresent()) {
			throw new UserException("Username already exists!!");
		}
		UserModel user = modelMapper.map(userDto, UserModel.class);
		userRepo.save(user);
		return new ResponseEntity(userDto, "User is registered");
	}

	@Override
	public List<UserDto> getAllDetails(String role) {
		if (role.equals("Admin")) {
			return userRepo.findAll().stream().map(user -> modelMapper.map(user, UserDto.class))
					.collect(Collectors.toList());
		} else {
			throw new UserException("Not admin");
		}
	}

	@Override
	public String loginUser(LoginDto loginDto) {
		Optional<UserModel> user = userRepo.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
		if (user.isEmpty()) {
			Optional<UserModel> userEmail = userRepo.findByEmail(loginDto.getEmail());
			Optional<UserModel> userPassword = userRepo.findByPassword(loginDto.getPassword());
			if (userEmail.isEmpty()) {
				throw new UserException("Email is incorrect,Give correct Email");
			} else if (userPassword.isEmpty()) {
				throw new UserException("Password is incorrect,Give correct password");
			}
		}
		String token = jwtTokenUtil.generateToken(loginDto);
		System.out.println(token);
		user.get().setIsLogin(true);
		userRepo.save(user.get());
		return token;
	}

	@Override
	public LoginDto logout(String token) {
		LoginDto loginDto = jwtTokenUtil.decode(token);
		Optional<UserModel> checkUserDetails = userRepo.findByEmailAndPassword(loginDto.getEmail(),
				loginDto.getPassword());
		LoginDto logout = modelMapper.map(checkUserDetails, LoginDto.class);
		checkUserDetails.get().setIsLogin(false);
		userRepo.save(checkUserDetails.get());
		return logout;
	}

	@Override
	public ParkingSlotModel[] getAllByGuard(String token) {
		LoginDto loginDto = jwtTokenUtil.decode(token);
		Optional<UserModel> user = userRepo.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
		if (user.get().getRole().equals("Guard")) {
			ParkingSlotModel[] parkingData = restTemplate.getForObject("http://localhost:8081/info",
					ParkingSlotModel[].class);
			return parkingData;
		} else {
			throw new UserException("Denied");
		}

	}

	@Override
	public ParkingSlotModel[] getAllByAdmin(String token) {
		LoginDto loginDto = jwtTokenUtil.decode(token);
		Optional<UserModel> user = userRepo.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
		if (user.get().getRole().equals("Admin")) {
			ParkingSlotModel[] parkingData = restTemplate.getForObject("http://localhost:8081/info",
					ParkingSlotModel[].class);
			return parkingData;
		} else {
			throw new UserException("Denied");
		}
	}

}
