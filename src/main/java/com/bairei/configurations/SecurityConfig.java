package com.bairei.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin()
                    .loginPage("/login")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                    .antMatchers("/*/new").authenticated()
                    .antMatchers("/*/edit/*").authenticated()
                    .antMatchers("/*/delete/*").authenticated()
                    .antMatchers(HttpMethod.POST,"/patient").authenticated()
                    .antMatchers(HttpMethod.POST,"/doctor").authenticated()
                    .antMatchers(HttpMethod.POST,"/visit").authenticated()
                    .antMatchers(HttpMethod.POST,"/*s").authenticated()
                    .anyRequest().permitAll()
                .and().csrf();
    }

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
    }


}
