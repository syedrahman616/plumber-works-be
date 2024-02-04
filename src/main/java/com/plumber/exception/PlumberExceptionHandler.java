package com.plumber.exception;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plumber.entity.MailRequest;
import com.plumber.response.APIError;
import com.plumber.response.Error;

@ControllerAdvice
public class PlumberExceptionHandler {

	@Autowired
	MessageSource message;

	@ExceptionHandler(APIException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody APIError handleAPIException(APIException exception) {
		return exception.getApiError();
	}
	
	@ExceptionHandler(ServletException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody APIError handleServletException(ServletException exception) {
		APIError apiError = new APIError();
		Error error = new Error();
		error.setCode("000");
		error.setDescription(exception.getMessage());
		apiError.setError(error);
		return apiError;
	}

	@ExceptionHandler(InvalidTokenException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody APIError handleTokenException(InvalidTokenException exception) {
		return exception.getApiError();
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody APIError handleException(Exception exception) {
		APIException ex = new APIException("001", "Something went wrong. please try again");
		MailRequest mailObject = new MailRequest();
		mailObject.setMessage("Check SQL error!");
		mailObject.setEmailTo(new String[] {"krishnas81200@gmail.com"});
		mailObject.setSubject("Sql ERROR");
		return ex.getApiError();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody APIError handleMethodArugmentException(MethodArgumentNotValidException exception) {
		List<FieldError> errors = exception.getBindingResult().getFieldErrors();
		List<ObjectError> objErrors = exception.getBindingResult().getGlobalErrors();
		List<String> errorArry = new ArrayList<>();
		for(FieldError error : errors) {
			errorArry.add(error.getField() + " - " + message.getMessage(error, Locale.ENGLISH));
		}
		for(ObjectError error : objErrors) {
			errorArry.add(error.getObjectName() + " - " + message.getMessage(error, Locale.ENGLISH));
		}
		APIError apiError = new APIError();
		Error error = new Error();
		error.setCode(HttpStatus.BAD_REQUEST.toString());
		error.setDescription(errorArry.toString());
		apiError.setError(error);
		return apiError;
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody APIError handleBindException(MethodArgumentNotValidException exception) {
		List<FieldError> errors = exception.getBindingResult().getFieldErrors();
		List<ObjectError> objErrors = exception.getBindingResult().getGlobalErrors();
		List<String> errorArry = new ArrayList<>();
		for(FieldError error : errors) {
			errorArry.add(error.getField() + " - " + message.getMessage(error, Locale.ENGLISH));
		}
		for(ObjectError error : objErrors) {
			errorArry.add(error.getObjectName() + " - " + message.getMessage(error, Locale.ENGLISH));
		}
		APIError apiError = new APIError();
		Error error = new Error();
		error.setCode(HttpStatus.BAD_REQUEST.toString());
		error.setDescription(errorArry.toString());
		apiError.setError(error);
		return apiError;
	}
	
}
