package ro.info.uaic.movierecommendation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ro.info.uaic.movierecommendation.filters.AuthEntryPoint;
import ro.info.uaic.movierecommendation.filters.AuthenticationFilter;
import ro.info.uaic.movierecommendation.filters.CustomAuthorizationFilter;
import ro.info.uaic.movierecommendation.handlers.GoogleAuthenticationSuccessHandler;
import ro.info.uaic.movierecommendation.services.UserDetailsServiceImpl;
import ro.info.uaic.movierecommendation.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final UserService userService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, CustomAuthorizationFilter customAuthorizationFilter, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.customAuthorizationFilter = customAuthorizationFilter;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
        http.authorizeRequests().antMatchers("/health").permitAll();
        http.authorizeRequests().antMatchers("/swagger-ui/index.html").permitAll();
        http.authorizeRequests().antMatchers("/swagger-ui/*", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/login").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/reset-password", "/api/v1/reset-password/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/users").permitAll();
        http.authorizeRequests().antMatchers("/oauth2/**", "/auth/**").permitAll();

//
//        http.authorizeRequests().anyRequest().authenticated()
//                        .and().oauth2Login()
//                        .authorizationEndpoint().baseUri("/oauth2/authorize")
//                        .and().successHandler(authenticationSuccessHandler());

        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new GoogleAuthenticationSuccessHandler(userService);
    }
}
