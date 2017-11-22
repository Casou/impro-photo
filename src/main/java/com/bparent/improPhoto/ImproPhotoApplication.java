package com.bparent.improPhoto;

import com.bparent.improPhoto.util.IConstants;
import com.bparent.improPhoto.util.NetworkUtils;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.net.SocketException;

@SpringBootApplication
public class ImproPhotoApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws SocketException {
		ConfigurableApplicationContext context = SpringApplication.run(ImproPhotoApplication.class, args);

		Environment environment = context.getBean(Environment.class);

		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"Serveur démarré");
		System.out.println(NetworkUtils.getIpString(environment.getProperty("server.port")));
		System.out.println("\n\n\n\n\n\n\n");

	}

	@Bean
	public ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
		registration.addUrlMappings("/console/*");
		return registration;
	}

}
