package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.core.convert.converter.Converter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kalil.peixoto
 * @date 7/30/24 17:44
 * @email kalilmvp@gmail.com
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private static final String CATALOGO_ADMIN = "CATALOGO_ADMIN";
    private static final String CATALOGO_CAST_MEMBERS = "CATALOGO_CAST_MEMBERS";
    private static final String CATALOGO_GENRES = "CATALOGO_GENRES";
    private static final String CATALOGO_CATEGORIES = "CATALOGO_CATEGORIES";
    private static final String CATALOGO_VIDEOS = "CATALOGO_VIDEOS";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/cast_members*").hasAnyRole(CATALOGO_ADMIN, CATALOGO_CAST_MEMBERS)
                        .antMatchers("/genres*").hasAnyRole(CATALOGO_ADMIN, CATALOGO_GENRES)
                        .antMatchers("/categories*").hasAnyRole(CATALOGO_ADMIN, CATALOGO_CATEGORIES)
                        .antMatchers("/videos*").hasAnyRole(CATALOGO_ADMIN, CATALOGO_VIDEOS)
                        .anyRequest().hasAnyRole(CATALOGO_ADMIN))
                .oauth2ResourceServer(oauth -> oauth
                        .jwt().jwtAuthenticationConverter(new KeyCloakJwtConverter()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions().sameOrigin())
                .build();
    }

    static class KeyCloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final KeyCloakAuthoritiesConverter authoritiesConverter;

        public KeyCloakJwtConverter() {
            this.authoritiesConverter = new KeyCloakAuthoritiesConverter();
        }

        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {
            return new JwtAuthenticationToken(jwt, this.extractAuthorities(jwt), this.extractPrincipal(jwt));
        }

        private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
            return this.authoritiesConverter.convert(jwt);
        }

        private String extractPrincipal(final Jwt jwt) {
            return jwt.getClaimAsString(JwtClaimNames.SUB);
        }
    }

    static class KeyCloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private static final String REALM_ACCESS = "realm_access";
        private static final String ROLES = "roles";
        private static final String RESOURCE_ACCESS = "resource_access";
        private static final String SEPARATOR = "_";
        private static final String ROLE_PREFIX = "ROLE_";

        @Override
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final var realmRoles = this.extractRealmRoles(jwt);
            final var resourceRoles = this.extractResourceRoles(jwt);

            return Stream.concat(realmRoles, resourceRoles)
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(role.toUpperCase())))
                    .collect(Collectors.toSet());
        }

        private Stream<String> extractResourceRoles(Jwt jwt) {
            final Function<Map.Entry<String, Object>, Stream<String>> mapResource = resource -> {
                final var key = resource.getKey();
                final var value = (JSONObject) resource.getValue();
                final var roles = (Collection<String>) value.get(ROLES);
                return roles.stream().map(role -> key.concat(SEPARATOR).concat(role));
            };

            final Function<Set<Map.Entry<String, Object>>, Collection<String>> mapResources = resources -> resources
                    .stream()
                    .flatMap(mapResource)
                    .toList();

            return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                    .map(resources -> resources.entrySet())
                    .map(mapResources)
                    .orElse(Collections.emptyList())
                    .stream();
        }

        private Stream<String> extractRealmRoles(Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                    .map(resource -> (Collection<String>) resource.get(ROLES))
                    .orElse(Collections.emptyList())
                    .stream();
        }
    }
}
