package pt.com.springboot.api.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import pt.com.springboot.api.service.impl.CustomUserDetailServiceImpl;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailServiceImpl customUserDetailServiceImpl = null;

    public SecurityConfig(CustomUserDetailServiceImpl customUserDetailServiceImpl) {
        this.customUserDetailServiceImpl = customUserDetailServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN") // Allow access to POST requests for ADMIN role
                .antMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN") // Allow access to PUT requests for ADMIN role
                .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN") // Allow access to DELETE requests for ADMIN role
                .antMatchers("/api/v1/**").authenticated() // Allow access to authenticated users for other methods
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailServiceImpl));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
    }
}