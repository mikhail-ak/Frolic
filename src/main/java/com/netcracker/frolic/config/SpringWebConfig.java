package com.netcracker.frolic.config;

import com.netcracker.frolic.Application;
import com.netcracker.frolic.controller.QueryParamResolver;
import com.netcracker.frolic.controller.QueryParamResolverImpl;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.validator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = Application.class)
public class SpringWebConfig implements WebApplicationInitializer {

    private final String TMP_FOLDER = "/tmp";
    private final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024;

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringWebConfig.class, DataJpaConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER,
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);
        dispatcher.setMultipartConfig(multipartConfigElement);
    }

    @Bean
    public QueryParamResolver getDefaultQueryParamResolver()
    { return new QueryParamResolverImpl(); }

    @Bean(name = "gameInfoWebValidator")
    public ValidatorImpl<GameInfo> getGameInfoWebValidator()
    { return new ValidatorImpl<>(GameInfoErrorMessageBuilder.INSTANCE, ResponseStatusThrower.BAD_REQUEST); }

    @Bean(name = "userWebValidator")
    public ValidatorImpl<User> getUserWebValidator()
    { return new ValidatorImpl<>(UserErrorMessageBuilder.INSTANCE, ResponseStatusThrower.BAD_REQUEST); }

    @Bean(name = "subscriptionWebValidator")
    public ValidatorImpl<Subscription> getSubscriptionWebValidator()
    { return new ValidatorImpl<>(SubscriptionErrorMessageBuilder.INSTANCE, ResponseStatusThrower.BAD_REQUEST); }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
