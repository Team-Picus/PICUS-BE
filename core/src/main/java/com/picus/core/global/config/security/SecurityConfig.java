package com.picus.core.global.config.security;

import com.picus.core.global.config.properties.AppProperties;
import com.picus.core.global.config.properties.CorsProperties;
import com.picus.core.global.oauth.entity.Role;
import com.picus.core.global.oauth.exception.RestAuthenticationEntryPoint;
import com.picus.core.global.oauth.filter.TokenAuthenticationFilter;
import com.picus.core.global.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.picus.core.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.picus.core.global.oauth.handler.TokenAccessDeniedHandler;
import com.picus.core.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.picus.core.global.oauth.repository.RefreshTokenRepository;
import com.picus.core.global.oauth.service.CustomOAuth2UserService;
import com.picus.core.global.oauth.service.CustomUserDetailsService;
import com.picus.core.global.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // If you want method-level security (e.g. @PreAuthorize)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 1) Define the PasswordEncoder bean.
     */
    @Bean
    public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    /**
     * 2) (Optional) Create a DaoAuthenticationProvider if you want custom auth handling.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 3) If you need the AuthenticationManager elsewhere (like in a service),
     *    expose it as a bean. Spring will build it from all providers,
     *    including the DaoAuthenticationProvider you declared.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 4) Main Security Filter Chain configuration replacing WebSecurityConfigurerAdapter.configure(HttpSecurity).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Manually add the provider if needed:
        http.authenticationProvider(authenticationProvider());

        http
                // if you already have a CorsConfigurationSource bean, just call .cors(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Exception handling
                .exceptionHandling(except -> except
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(tokenAccessDeniedHandler)
                )
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Let pre-flight requests through
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        // Example role-based checks
                        .requestMatchers("/api/**").hasAnyAuthority(Role.USER.getCode())
                        .requestMatchers("/api/admin/**").hasAnyAuthority(Role.ADMIN.getCode())
                        // Everything else
                        .anyRequest().authenticated()
                )
                // OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                    .baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                        )
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                        .failureHandler(oAuth2AuthenticationFailureHandler())
                );

        // Add custom token filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build the SecurityFilterChain
        return http.build();
    }

    /**
     * 5) Custom TokenAuthenticationFilter.
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /**
     * 6) OAuth2 authorization request repository bean
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * 7) OAuth2 success/failure handlers
     */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /**
     * 8) CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        // If you need a max age, set it explicitly:
        corsConfig.setMaxAge(3600L);

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}
