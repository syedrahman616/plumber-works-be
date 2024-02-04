package com.plumber.entity;

import java.util.Map;

import org.springframework.core.io.InputStreamSource;
import org.springframework.ui.ModelMap;

import lombok.Data;
@Data
public class MailRequest {
	private String[] emailTo;
	private String subject;
	private String message;
	private Map<String, Object> model;
	private InputStreamSource fileResource;

	public String[] getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String[] emailTo) {
		this.emailTo = emailTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public InputStreamSource getFileResource() {
		return fileResource;
	}

	public void setFileResource(InputStreamSource fileResource) {
		this.fileResource = fileResource;
	}
}
