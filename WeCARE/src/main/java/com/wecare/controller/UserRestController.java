package com.wecare.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wecare.dto.BookingDTO;
import com.wecare.dto.ErrorMessage;
import com.wecare.dto.LoginDTO;
import com.wecare.dto.UserDTO;
import com.wecare.exception.WecareException;
import com.wecare.service.BookService;
import com.wecare.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@Validated
@RequestMapping("/users")
public class UserRestController {
	@Autowired
	UserService service;
	@Autowired
	BookService bookingService;
	
	@Operation(summary="Create new account for User")
	@PostMapping
	public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO, Errors errors)throws MethodArgumentNotValidException{
		if (errors.hasErrors()) {
			String response = "";
			response = errors.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.collect(Collectors.joining(","));
			ErrorMessage error = new ErrorMessage();
			error.setMessage(response);
			return ResponseEntity.badRequest().body(error.getMessage());
		}
		return ResponseEntity.ok().body(service.createUser(userDTO));
	}
	
	//ResponseEntity.ok().body(service.loginUser(loginDTO)); //status success/ok : 200
	//ResponseEntity.badRequest().body(service.loginUser(loginDTO)); //status bad req : 400
	//ResponseEntity.notFound().body(service.createUser(userDTO)); // status not found : 404
	// status internal server error : 500
	
	@Operation(summary="Login for for User")
	@PostMapping("/login")
	public ResponseEntity<Boolean> loginUser(@Valid @RequestBody LoginDTO loginDTO) throws WecareException, MethodArgumentNotValidException{
		return ResponseEntity.ok().body(service.loginUser(loginDTO));
	}
	
	@Operation(summary="Get User's Profile by its ID")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUserProfile(@PathVariable  String userId) throws WecareException, ConstraintViolationException{
		return ResponseEntity.ok().body(service.getUserProfile(userId));
	}
	
	@Operation(summary="Get all scheduled appointments of user by its ID")
	@GetMapping("/booking/{userId}")
	public ResponseEntity<List<BookingDTO>> showMyAppointments(@PathVariable  String userId) throws WecareException, ConstraintViolationException{
		return ResponseEntity.ok().body(bookingService.findBookingByUserId(userId));
	}
}
