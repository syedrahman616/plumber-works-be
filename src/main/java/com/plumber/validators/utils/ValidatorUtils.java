package com.plumber.validators.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
	
	public static boolean emailValidator(String eMail) {
		String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(eMail);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	public static boolean passcodeValidator(String value) {
		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[*@#$%^&+=])" + "(?=\\S+$).{8,13}$";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(value);
		if (!mat.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean fullnameValidator(String value)
	{
		if (value.matches(".*\\d.*") == true) {
			return false;
		}
		return true;
		}
	public static boolean mobileValidator(String value)
	{
		String regex = "\\d{10}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()) {
			return false;
		}
		return true;
		}

}

