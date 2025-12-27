package com.pdas.priCart.shop.auth.conf;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private RSAKey rsaKey;
    // 1. to change to Oauth2 resource server
    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateKeyPair();
        return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();

    }

    private static KeyPair generateKeyPair(){
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    // define encoder(used by TokenService to sign tokens)
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        // JwtEncoder : It uses the Private Key to sign our JWTs.
        return new NimbusJwtEncoder(jwkSource);
    }

    // define decoder (used by Resource Server to verify Tokens)
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        // JwtDecoder: This uses the Public Key to ensure that any token coming into our API was actually created by us.
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Provider (Our validator)
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // 3. Authentication Manager (The BOss that coordinates provider)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 4. The security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/all/**", "/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/users/a/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/u/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                // configuring app to be a resource server
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // keep basic auth only for the initial login endpoint
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }


    /*
    By default, Spring Security’s JwtAuthenticationConverter looks at the scope or scp claim in our JWT and prefixes it with SCOPE_.
    This is the OAuth2 standard.
    * Because Oauth2 has by default JwtAuthenticationToken with authority as SCOPE_ROLE_USER
    * */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix(""); // Removing SCOPE_
        authoritiesConverter.setAuthoritiesClaimName("scope"); // use "scope" claim

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;

    }


}
