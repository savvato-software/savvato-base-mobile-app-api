package com.savvato.basemobileapp.config.filters;

import java.io.IOException;
import java.util.List;

import io.jsonwebtoken.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.savvato.basemobileapp.constants.Constants;
import com.savvato.basemobileapp.services.UserPrincipalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
    UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ((header == null || header.equals("")) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = this.userPrincipalService.getUserPrincipalByEmail(getEmailAddress(token));

        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                    List.of() : userDetails.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private boolean validate(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith((SecretKey) Constants.JWT_SECRET_KEY) // Cast to SecretKey
                    .build();

            parser.parseSignedClaims(token); // Parses and verifies the JWT

            return true;
        } catch (SecurityException | SignatureException ex) {
            log.error("Invalid JWT signature - " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - " + ex.getMessage());
        }
        return false;
    }

    public String getEmailAddress(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith((SecretKey) Constants.JWT_SECRET_KEY) // Cast to SecretKey
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        return claims.getSubject().split(",")[1];
    }
}
