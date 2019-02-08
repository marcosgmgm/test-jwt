package com.example.demo.config;

import com.example.demo.security.constraints.DenyMatcher;
import com.example.demo.security.constraints.PermitMatcher;
import com.example.demo.security.constraints.SecurityConstraintsLoader;
import com.example.demo.security.jwt.JwtConfigurer;
import com.example.demo.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${security.enabled}")
    Boolean securityEnable;

    @Autowired
    SecurityConstraintsLoader loader;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (securityEnable) {
            http.httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            for (DenyMatcher it : loader.getConstraintsMapper().getDenyMatcherList()) {
                for (String role: it.getRoleMapping().keySet()) {
                    for (String method: it.getRoleMapping().get(role)) {
                        http.authorizeRequests().antMatchers(HttpMethod.valueOf(method), it.getPattern()).hasRole(role);
                    }
                }
            }

            for (PermitMatcher it : loader.getConstraintsMapper().getPermitMatcherList()) {
                for (String method: it.getMethods()) {
                    http.authorizeRequests().antMatchers(HttpMethod.valueOf(method), it.getPattern()).permitAll();
                }
            }

            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));

        } else {
            http
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }


}

