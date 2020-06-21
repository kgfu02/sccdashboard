package kfu.coviddashboard.coviddashboardspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CovidDashboardSpringApplication {

	public static void main(String[] args) {
		System.out.println("changed");
		SpringApplication.run(CovidDashboardSpringApplication.class, args);
	}
}
