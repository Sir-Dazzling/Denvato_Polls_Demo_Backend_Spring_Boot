package com.example.denvato_polls_demo.denvato_polls_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {DenvatoPollsDemoApplication.class, Jsr310JpaConverters.class})
public class DenvatoPollsDemoApplication
{
	//Setting a default time zone
	@PostConstruct
	void init()
	{
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	public static void main(String[] args) {
		SpringApplication.run(DenvatoPollsDemoApplication.class, args);
	}

}