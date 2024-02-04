package com.plumber.config;

import org.apache.catalina.core.StandardContext;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GracefulShutdown {

	@Bean
	public ServletWebServerFactory servletWebServerFactory() {
		TomcatServletWebServerFactory appServer = new TomcatServletWebServerFactory();
		appServer.addContextCustomizers(context -> {
			if(context instanceof StandardContext) {
				((StandardContext) context).setUnloadDelay(10000);
			}
		});
		return appServer;
	}
}
