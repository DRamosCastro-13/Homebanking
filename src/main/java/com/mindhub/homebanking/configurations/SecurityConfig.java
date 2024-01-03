package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //se configuran los filtros para definir quién tiene acceso a qué
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/index.html", "/web/assets/**", "/web/pages/login.html", "/web/assets/assetsScripts/login.js" ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .requestMatchers("/api/clients/current", "/api/accounts/clients/current", "/web/pages/*", "/api/accounts/{id}").hasAuthority("CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/accounts/clients/current").hasAuthority("CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .requestMatchers(HttpMethod.POST, "api/cards/clients/current").hasAuthority("CLIENT")
                .requestMatchers("/api/clients", "/h2-console/**", "/web/**", "/rest/**").hasAuthority("ADMIN")
                .anyRequest().denyAll()
        );

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //se desactiva el filtro csrf porque estamos trabajando con una API Rest que será
        //accedida mediante peticiones HTTP y no enviando formularios

        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        //Permite el acceso a recursos de APIs externas, en este caso H2-Console

        http.formLogin( formLogin ->
                formLogin.loginPage("/web/pages/login.html")//La página donde se hace el login
                        .loginProcessingUrl("/api/login")//Endpoint donde se envía la petición
                        .usernameParameter("email")//Parámetros que se enviarán a la petición
                        .passwordParameter("password")
                        .failureHandler((request, response, exception) -> response.sendError(403))//Manejo para inicios de sesión fallidos por datos incorrectos
                        .successHandler((request, response, authentication) -> clearAuthenticationAttributes(request))//Manejor para inicio de sesión exitoso
                        .permitAll()
        );

        http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/index.html")));
        //Respuesta si un usuario intenta acceder sin haberse autenticado (no tiene autorización)
        //o intenta acceder a un recurso al cual no tiene autorización

        http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                .logoutUrl("/api/logout")//ruta
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())//Notifica al usuario del cierre de sesión y cierra la sesión en el servidor
                .deleteCookies("JSESSIONID")//Elimina la cookie que representa la sesión del usuario
        );

        http.rememberMe(Customizer.withDefaults());
        //Habilita la funcionalidad de "recordar" al usuario en la aplicación. Les permite a los
        //usuarios permanecer autenticados incluso después de cerrar el navegador


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
    //Si hay fallas al iniciar la sesión serán guardadas en el navegador, este método borra esas
    //fallas una vez el inicio de sesión sea exitoso (línea 45)

}
