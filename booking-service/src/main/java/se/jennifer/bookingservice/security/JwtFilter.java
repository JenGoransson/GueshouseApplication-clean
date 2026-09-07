package se.jennifer.bookingservice.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    JwtFilter(JwtService j){
        this.jwt = j; }

    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String h = req.getHeader("Authorization"); //Läser in själva headern
        if (h != null && h.startsWith("Bearer ")){ // kollar så h inte är null, börjar med Bearer och ett mellanslag efter
            String token = h.substring(7); // cuttar och tar bort så bearer inte kommer med
            if (jwt.isTokenValid(token)){  // kollar om token är valid
                var user = jwt.extractUsername(token);
                var auth = new UsernamePasswordAuthenticationToken(
                        user, null, List.of());
                SecurityContextHolder.getContext() //Säger till Spring security att denna användare är inloggad
                        .setAuthentication(auth);  //för detta anropet.
            }
        }
        chain.doFilter(req, res);
    }
}

