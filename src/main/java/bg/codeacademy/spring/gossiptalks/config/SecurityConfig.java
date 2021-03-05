package bg.codeacademy.spring.gossiptalks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(
      HttpSecurity http) throws Exception {
    http
        .csrf()
        /**/.disable()
        .cors()
        /**/.disable()
        .headers()
        /**/.frameOptions().sameOrigin().and()
        .formLogin()
        /**/.and()
        .httpBasic()
        /**/.and()
        .logout()
        /**/.and();
//        .authorizeRequests()
//        /**/.antMatchers("/h2-console/**").permitAll()
//        /**/.antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
//        /**/.antMatchers("/**").authenticated();
  }

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
