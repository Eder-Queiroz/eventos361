package web.eventos361;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Comentado temporariamente para evitar conflito com Docker Adminer na porta
	// 8080
	/*
	 * @Bean
	 * public ServletWebServerFactory servletContainer() {
	 * TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	 * 
	 * @Override
	 * protected void postProcessContext(Context context) {
	 * SecurityConstraint securityConstraint = new SecurityConstraint();
	 * securityConstraint.setUserConstraint("CONFIDENTIAL");
	 * SecurityCollection collection = new SecurityCollection();
	 * collection.addPattern("/*");
	 * securityConstraint.addCollection(collection);
	 * context.addConstraint(securityConstraint);
	 * }
	 * };
	 * tomcat.addAdditionalTomcatConnectors(redirectConnector());
	 * return tomcat;
	 * }
	 * 
	 * private Connector redirectConnector() {
	 * Connector connector = new
	 * Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
	 * connector.setScheme("http");
	 * connector.setPort(8080);
	 * connector.setSecure(false);
	 * connector.setRedirectPort(8443);
	 * return connector;
	 * }
	 */

}