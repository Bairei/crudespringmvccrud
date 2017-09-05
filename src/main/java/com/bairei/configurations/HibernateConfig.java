package com.bairei.configurations;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.bairei.configurations")
@PropertySource("classpath:hibernate.properties")
@EnableJpaRepositories(basePackages = {"com.bairei.repositories"})
public class HibernateConfig {

    @Autowired
    private Environment env;

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.bairei");
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    DataSource dataSource(Environment env){
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        ds.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
        ds.setUsername(env.getRequiredProperty("jdbc.user"));
        ds.setPassword(env.getRequiredProperty("jdbc.password"));
        return ds;
    }

    Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect",
                env.getRequiredProperty("hibernate.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto",
                env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.show_sql",
                env.getRequiredProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.format_sql",
                env.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }

}
