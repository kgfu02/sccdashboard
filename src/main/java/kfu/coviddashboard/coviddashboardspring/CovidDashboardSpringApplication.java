package kfu.coviddashboard.coviddashboardspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class CovidDashboardSpringApplication extends SpringBootServletInitializer{
	final static Logger logger = LoggerFactory.getLogger(CovidDashboardSpringApplication.class);
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(CovidDashboardSpringApplication.class, args);
	}

	@Scheduled(cron = "0 0/1 11/1 ? * *", zone="America/Los_Angeles")
	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(CovidDashboardSpringApplication.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
		logger.info("restarted");
	}
}
