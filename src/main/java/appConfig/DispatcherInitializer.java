package appConfig;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;


//boot:3
public class DispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		// boot:4
		return new Class[] { RootConfigurer.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return null;
	}


//===============================================注册所有过滤器 start==================================================
	/*
	  * 注册过滤器，映射路径与DispatcherServlet一致，路径不一致的过滤器需要注册到另外的WebApplicationInitializer中
	  */
	@Override
	protected Filter[] getServletFilters() {
		/**字符过滤器**/
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);


		return new Filter[] {characterEncodingFilter};
	}

//===============================================注册所有过滤器 start==================================================
}
