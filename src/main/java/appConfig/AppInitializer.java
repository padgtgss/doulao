package appConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//boot:1
public class AppInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
		// boot:2
		webApplicationContext.register(MvcConfigurer.class);
		// boot:3
		DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
		ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
		dynamic.addMapping("/");
	}
}
