package appConfig;


import com.google.common.collect.ImmutableSet;
import com.yslt.doulao.dulao.interceptor.AuthInterceptor;
import common.entity.Response;
import common.ext.JacksonObjectMapper;
import common.ext.JacksonObjectMapperHttpMessageConvert;
import common.ext.ServerExceptionResolver;
import common.util.conversion.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

//boot:2
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.yslt.doulao", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})})
public class MvcConfigurer extends WebMvcConfigurationSupport {

    /**
     * RequestMappingHandlerMapping需要显示声明
     */
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return super.requestMappingHandlerMapping();
    }

    //====================================所有自定义拦截器在此声明  start==========================================


    /**
     * 描述 : <数据验证拦截器>. <br>
     * <p>
     * <使用方法说明>
     * </p>
     *
     * @return
     */
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

//====================================所有自定义拦截器在此声明 end==========================================


    //==========================注册自定义拦截器 start=============================================


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor());
        interceptorRegistration.addPathPatterns("/X20151118/*");
    }
    //==========================注册自定义拦截器 end=============================================

    @Bean
    public Response response() {
        return new Response();
    }

    @Bean
    public JacksonObjectMapper jacksonObjectMapper() {
        return new JacksonObjectMapper();
    }
    @Bean
    public JacksonObjectMapperHttpMessageConvert jacksonObjectMapperHttpMessageConvert(){
        JacksonObjectMapperHttpMessageConvert jacksonObjectMapperHttpMessageConvert = new JacksonObjectMapperHttpMessageConvert();
        jacksonObjectMapperHttpMessageConvert.setObjectMapper(jacksonObjectMapper());
        return  jacksonObjectMapperHttpMessageConvert;
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        return new SimpleMappingExceptionResolver();
    }

    @Bean
    public FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean() {

        FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean = new FormattingConversionServiceFactoryBean();
        ImmutableSet of = ImmutableSet.of(new StringTrimmerConversion(), new StringToDateConversion(),
                new StringToDoubleConversion(), new StringToIntegerConversion(), new StringToLongConversion());

        formattingConversionServiceFactoryBean.setConverters(of);
        return formattingConversionServiceFactoryBean;
    }

    @Bean
    public ServerExceptionResolver serverExceptionResolver() {

        ServerExceptionResolver serverExceptionResolver = new ServerExceptionResolver();
        serverExceptionResolver.setContentType("application/json;charset=UTF-8");
        serverExceptionResolver.setObjectMapper(jacksonObjectMapper());
        return serverExceptionResolver;
    }

    /**
     * 描述 : <文件上传处理器>. <br>
     * @return
     */
    @Bean(name="multipartResolver")

    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(10485760);
        return commonsMultipartResolver;
    }

}
