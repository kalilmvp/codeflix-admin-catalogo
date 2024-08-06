package com.fullcycle.admin.catalogo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

/**
 * @author kalil.peixoto
 * @date 3/16/23 22:16
 * @email kalilmvp@gmail.com
 */
public interface ApiTest {
    JwtRequestPostProcessor ADMIN_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"));
    JwtRequestPostProcessor CATEGORIES_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"));
    JwtRequestPostProcessor GENRES_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"));
    JwtRequestPostProcessor CAST_MEMBERS_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"));
    JwtRequestPostProcessor VIDEO_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"));
}
