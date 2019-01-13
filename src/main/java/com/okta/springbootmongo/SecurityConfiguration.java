package com.okta.springbootmongo;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
  
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange()
        // protect POST /kayaks based on Admin group membership
        .pathMatchers(HttpMethod.POST, "/kayaks/**").hasAuthority("Admin")
        .anyExchange().authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(grantedAuthoritiesExtractor());
    return http.build();
  }

  /*
    The GrantedAuthoritiesExtractor is used to read the "groups" claim from the JWT and
    map it to Spring Security authorities, that we can use in the hasAuthority() methods
    above.
   */
  static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
      Collection<String> authorities = (Collection<String>)jwt.getClaims().get("groups");
      return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
  }

  Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
    GrantedAuthoritiesExtractor extractor = new GrantedAuthoritiesExtractor();
    return new ReactiveJwtAuthenticationConverterAdapter(extractor);
  }
  
}

