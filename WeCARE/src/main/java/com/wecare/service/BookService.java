package com.wecare.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.wecare.dto.BookingDTO;
import com.wecare.entity.BookingEntity;
import com.wecare.entity.CoachEntity;
import com.wecare.entity.UserEntity;
import com.wecare.exception.ExceptionConstants;
import com.wecare.exception.WecareException;
import com.wecare.repository.BookRepository;
import com.wecare.repository.CoachRepository;
import com.wecare.repository.UserRepository;
import com.wecare.utility.MailUtility;

@Service
@PropertySource("classpath:messages.properties")
public class BookService {
	@Autowired 
	BookRepository bookRepository;
	@Autowired 
	CoachRepository coachRepository;
	@Autowired 
	UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private Environment environment;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MailUtility mail;
	
	public boolean bookAppointment(String userId, String coachId, LocalDate appointmentDate, String slot) throws WecareException {
		logger.info("Inside bookAppointment method of {}",this.getClass());
		BookingEntity entity = new BookingEntity();
		String userName,coachName,email,bookingSlot;
		Optional<UserEntity> opUserEntity = userRepository.findById(userId);
		if(opUserEntity.isPresent()) {
			UserEntity Uentity = opUserEntity.get();
			userName = Uentity.getName();
			email = Uentity.getEmail();
			entity.setUser(Uentity);
		}else {
			throw new WecareException(environment.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));
		}
		Optional<CoachEntity> opCoachEntity = coachRepository.findById(coachId);
		if(opCoachEntity.isPresent()) {
			CoachEntity Centity = opCoachEntity.get();
			coachName = Centity.getName();
			entity.setCoach(Centity);
		}else {
			throw new WecareException(environment.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));
		}
		entity.setAppointmentDate(appointmentDate);
		entity.setSlot(slot);
		BookingEntity bookingEntity = bookRepository.saveAndFlush(entity);
		//bookAppointment mail 
		Integer bookingId = bookingEntity.getBookingId();
		bookingSlot = slot;
		mail.sendSchedulingEmail(userName, coachName, email, bookingId, bookingSlot, appointmentDate);
		return true;
	}
	
	public boolean rescheduleAppointment(Integer bookingId, LocalDate appointmentDate, String slot) throws WecareException{
		logger.info("Inside rescheduleAppointment method of {}",this.getClass());
		String userName,coachName,email,bookingSlot;
		Optional<BookingEntity> op = bookRepository.findById(bookingId);
		if(op.isPresent()) {
			BookingEntity entity = op.get();
			entity.setAppointmentDate(appointmentDate);
			entity.setSlot(slot);
			BookingEntity bookingEntity = bookRepository.saveAndFlush(entity);
			//rescheduleAppointment mail 
			userName = bookingEntity.getUser().getName();
			coachName = bookingEntity.getCoach().getName();
			email = bookingEntity.getUser().getEmail();
			bookingSlot = slot;
			mail.sendReschedulingEmail(userName, coachName, email, bookingId, bookingSlot, appointmentDate);
			return true;
		}
		throw new WecareException(environment.getProperty(ExceptionConstants.BOOKING_NOT_FOUND.toString()));
	}
	
	public void cancelAppointment(Integer bookingId) throws WecareException {
		logger.info("Inside cancelAppointment method of {}",this.getClass());
		String userName,coachName,email,bookingSlot;
		LocalDate appointmentDate;
		Optional<BookingEntity> op = bookRepository.findById(bookingId);
		if(op.isPresent()) {
			BookingEntity entity = op.get();
			//cancelAppointment mail 
			userName = entity.getUser().getName();
			coachName = entity.getCoach().getName();
			email = entity.getUser().getEmail();
			bookingSlot = entity.getSlot();
			appointmentDate = entity.getAppointmentDate();
			mail.sendCancellingEmail(userName, coachName, email, bookingId, bookingSlot, appointmentDate);
			entity.setCoach(null);
			entity.setUser(null);
			bookRepository.delete(entity);
		}else {
			throw new WecareException(environment.getProperty(ExceptionConstants.BOOKING_NOT_FOUND.toString()));
		}
	}
	
	public BookingDTO findByBookingId(Integer bookingId) throws WecareException {
		logger.info("Inside findByBookingId method of {}",this.getClass());
		Optional<BookingEntity> op = bookRepository.findById(bookingId);
		if(op.isPresent()) {
			BookingEntity entity = op.get();
			BookingDTO dto = mapper.map(entity, BookingDTO.class);
			return dto;
		}
		throw new WecareException(environment.getProperty(ExceptionConstants.BOOKING_NOT_FOUND.toString()));
	}
	
	public List<BookingDTO> findBookingByUserId(String userId) throws WecareException{
		logger.info("Inside findBookingByUserId method of {}",this.getClass());
		Optional<UserEntity> opUserEntity = userRepository.findById(userId);
		if(opUserEntity.isPresent()) {
			UserEntity Uentity = opUserEntity.get();
			List<BookingEntity> bookingEntityLst = Uentity.getUsers().stream().collect(Collectors.toList());
			List<BookingDTO> bookingLst = bookingEntityLst.stream().map(b->mapper.map(b, BookingDTO.class)).collect(Collectors.toList());
			return bookingLst;
		}
		throw new WecareException(environment.getProperty(ExceptionConstants.USER_NOT_FOUND.toString()));
	}
	
	public List<BookingDTO> findBookingByCoachId(String coachId) throws WecareException{
		logger.info("Inside findBookingByCoachId method of {}",this.getClass());
		Optional<CoachEntity> opCoachEntity = coachRepository.findById(coachId);
		if(opCoachEntity.isPresent()) {
			CoachEntity Centity = opCoachEntity.get();
			List<BookingEntity> bookingEntityLst = Centity.getCoaches().stream().collect(Collectors.toList());
			List<BookingDTO> bookingLst = bookingEntityLst.stream().map(b->mapper.map(b, BookingDTO.class)).collect(Collectors.toList());
			return bookingLst;
		}
		throw new WecareException(environment.getProperty(ExceptionConstants.COACH_NOT_FOUND.toString()));
	}
}
