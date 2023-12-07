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
import com.wecare.dto.CoachDTO;
import com.wecare.dto.ErrorMessage;
import com.wecare.dto.LoginDTO;
import com.wecare.exception.WecareException;
import com.wecare.service.BookService;
import com.wecare.service.CoachService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@Validated
@RequestMapping("/coaches")
public class CoachRestController {
	@Autowired
	CoachService service;
	@Autowired
	BookService bookingService;
	
	@Operation(summary="Create new account for Life Coaches")
	@PostMapping
	public ResponseEntity<String> createCoach(@Valid @RequestBody CoachDTO coachDTO, Errors errors)throws MethodArgumentNotValidException{
		if (errors.hasErrors()) {
			String response = "";
			response = errors.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.collect(Collectors.joining(","));
			ErrorMessage error = new ErrorMessage();
			error.setMessage(response);
			return ResponseEntity.badRequest().body(error.getMessage());
		}	
		return ResponseEntity.ok().body(service.createCoach(coachDTO));
	}
	
	@Operation(summary="Login for life coaches")
	@PostMapping("/login")
	public ResponseEntity<Boolean> loginCoach(@Valid @RequestBody LoginDTO loginDTO)throws WecareException,MethodArgumentNotValidException{
		return ResponseEntity.ok().body(service.loginCoach(loginDTO));
	}
	
	@Operation(summary="Get Coach's Profile by its ID")
	@GetMapping("/{coachId}")
	public ResponseEntity<CoachDTO> getCoachProfile(@PathVariable  String coachId)throws ConstraintViolationException, WecareException{
		return ResponseEntity.ok().body(service.getCoachProfile(coachId));
	}
	
	@Operation(summary="Get all Life coaches' profile")
	@GetMapping("/all")
	public ResponseEntity<List<CoachDTO>> showAllCoaches()throws WecareException{
		return ResponseEntity.ok().body(service.showAllCoaches());
		
	}
	
	@Operation(summary="Get all scheduled appointments of coach by its ID")
	@GetMapping("booking/{coachId}")
	public ResponseEntity<List<BookingDTO>> showMySchedule(@PathVariable  String coachId)throws ConstraintViolationException,WecareException{
		return ResponseEntity.ok().body(bookingService.findBookingByCoachId(coachId));
	}
}
