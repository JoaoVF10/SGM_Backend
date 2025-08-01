package br.edu.ifpb.sgm.projeto_sgm;

import br.edu.ifpb.sgm.projeto_sgm.util.JwtAuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.*;

@Configuration
public class WebConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public WebConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ✅ CORS configurado para permitir acesso do frontend React
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:*")
                        // Porta correta do seu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/alunos").permitAll() // ✅ permite cadastro de aluno
                        .requestMatchers(HttpMethod.GET, "/api/alunos/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers(HttpMethod.PUT, "/api/alunos/{id}").hasAnyRole(ADMIN, COORDENADOR, DISCENTE)
                        .requestMatchers("/api/alunos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers("/api/instituicoes/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers("/api/cursos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers(HttpMethod.GET, "/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers("/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers("/api/processos-seletivos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers("/api/monitorias/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/atividades/**").authenticated()
                        .requestMatchers("/api/atividades/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/professores/**").authenticated()
                        .requestMatchers("/api/professores/**").hasAnyRole(ADMIN, COORDENADOR)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout(logout -> logout
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Logout realizado com sucesso!");
                    }
                })
        );

        return http.build();
    }
}
