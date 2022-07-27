package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableMongock
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class Library {

	public static void main(String[] args) {
		SpringApplication.run(Library.class);
	}

}
