package bg.codeacademy.spring.gossiptalks.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
        // to make h2-console happy
        .csrf()
        /**/.disable()
        .cors()
        /**/.disable()
        .headers()
        /**/.frameOptions().sameOrigin().and()
        // http basic-authentication
        .httpBasic()
        /**/.and()
        .logout()
        /**/.and()
        // apply security permissions to endpoints
        .authorizeRequests()
        /**/.antMatchers("/h2-console/**").permitAll()
        /**/.antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
        /**/.antMatchers("/api/v1/**").authenticated();

  }

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
