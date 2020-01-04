package com.netcracker.frolic.config;

import com.netcracker.frolic.Application;
import com.netcracker.frolic.controller.GameInfoValidator;
import com.netcracker.frolic.controller.QueryParamResolver;
import com.netcracker.frolic.controller.QueryParamResolverImpl;
import com.netcracker.frolic.controller.Validator;
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
}
