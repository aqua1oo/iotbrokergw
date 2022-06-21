package com.umc.thingseye2.iotgw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	    	.authorizeRequests()
	        .antMatchers(HttpMethod.POST, "/command/req").permitAll()
	        .anyRequest().authenticated()
	        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);   
	  }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // implements PasswordEncoder and overide encode method with the MD5 protocol
        return new BCryptPasswordEncoder(10);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
    	// To allow Pre-flight [OPTIONS] request from browser
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
    
}