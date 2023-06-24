package com.real.practice;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateParser {
	public static void main(String[] args) {

        String dateString = "1/9/2005 12:00:00 AM";
//        String dateFormat = "MM/dd/yyyy hh:mm:ss a";

        try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a");
    		LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            System.out.println(dateTime.getYear());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
