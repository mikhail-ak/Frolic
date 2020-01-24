package com.netcracker.frolic.config;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Subscription;
import com.netcracker.frolic.entity.User;
import com.netcracker.frolic.repository.Repository;
import com.netcracker.frolic.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Repository.class)
public class DataJpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataJpaConfig.class);

    @Bean
    public DataSource dataSource() {
        try {
            EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2).build();
            //.addScripts("classpath:db/schema.sql")
        } catch (Exception e) {
            logger.error("Embedded DataSource bean cannot be created!", e);
            return null;
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        hibernateProp.put("hibernate.max_fetch_depth", 3);
        hibernateProp.put("hibernate.jdbc.batch_size", 10);
        hibernateProp.put("hibernate.jdbc.fetch_size", 50);
        hibernateProp.put("hibernate.hbm2ddl.auto", "update");
        return hibernateProp;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.netcracker.frolic.entity");
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.afterPropertiesSet();
        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean(name = "gameInfoJpaValidator")
    public ValidatorImpl<GameInfo> getGameInfoJpaValidator()
    { return new ValidatorImpl<>(GameInfoErrorMessageBuilder.INSTANCE, IllegalArgumentThrower.INSTANCE); }

    @Bean(name = "userJpaValidator")
    public ValidatorImpl<User> getUserJpaValidator()
    { return new ValidatorImpl<>(UserErrorMessageBuilder.INSTANCE, IllegalArgumentThrower.INSTANCE); }

    @Bean(name = "subscriptionJpaValidator")
    public ValidatorImpl<Subscription> getSubscriptionJpaValidator()
    { return new ValidatorImpl<>(SubscriptionErrorMessageBuilder.INSTANCE, IllegalArgumentThrower.INSTANCE); }
}
