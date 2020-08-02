package kfu.coviddashboard.coviddashboardspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class CovidDashboardSpringApplication {
	final static Logger logger = LoggerFactory.getLogger(CovidDashboardSpringApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CovidDashboardSpringApplication.class, args);
		logger.info("hello");
	}
}
