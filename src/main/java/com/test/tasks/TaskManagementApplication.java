package com.test.tasks;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableEncryptableProperties
public class TaskManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(false);
		loggingFilter.setMaxPayloadLength(64000);
		return loggingFilter;
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TaskManagementApplication.class);
	}
}
