package com.wecare.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
	private String userId;
	@NotNull(message="{name.not.null}")
	@Size(min = 3,max = 50,message = "{name.size.invalid}")
	private String name;
	@NotNull(message="{gender.not.null}")
	private char gender;
	@NotNull(message="{dateOfBirth.not.null}")
	private LocalDate dateOfBirth;
	@NotNull(message="{password.not.null}")
	@Size(min = 5,max = 10,message = "{password.size.invalid}")
	private String password;
	@NotNull(message="{mobile.number.not.null}")
	@Pattern(regexp="(^$|[0-9]{10})",message="{mobile.number.invalid}")
	private String mobileNumber;
	@NotNull(message="{email.not.null}")
	@Email(message="{email.invalid}")
	private String email;
	@NotNull(message="{pincode.number.not.null}")
	@Pattern(regexp="(^$|[0-9]{6})",message="{pincode.number.invalid}")
	private String pincode;
	@NotNull(message="{city.not.null}")
	@Size(min = 3,max = 20,message = "{city.size.invalid}")
	private String city;
	@NotNull(message="{state.not.null}")
	@Size(min = 3,max = 20,message = "{state.size.invalid}")
	private String state;
	@NotNull(message="{country.not.null}")
	@Size(min = 3,max = 20,message = "{country.size.invalid}")
	private String country;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
