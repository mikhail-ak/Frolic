package com.netcracker.frolic.config;

import com.netcracker.frolic.Application;
import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.repository.GameInfoRepo;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.service.GameInfoServiceImpl;
import com.netcracker.frolic.validator.GameInfoValidator;
import com.netcracker.frolic.controller.QueryParamResolver;
import com.netcracker.frolic.controller.QueryParamResolverImpl;
import com.netcracker.frolic.validator.SubscriptionValidator;
import com.netcracker.frolic.validator.UserValidator;
import com.netcracker.frolic.validator.Validator;
import com.netcracker.frolic.entity.GameInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = Application.class)
@PropertySource("classpath:cache.properties")
public class SpringWebConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringWebConfig.class, DataJpaConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    @Bean
    public QueryParamResolver getDefaultQueryParamResolver()
    { return new QueryParamResolverImpl(); }

    @Bean(name = "gameInfoValidator")
    public Validator<GameInfo> getGameInfoValidator()
    { return new GameInfoValidator(); }

    @Bean(name = "userValidator")
    public Validator<User> getUserValidator()
    { return new UserValidator(); }

    @Bean(name = "subscriptionValidator")
    public Validator<Subscription> getSubscriptionValidator()
    { return new SubscriptionValidator(); }
}
