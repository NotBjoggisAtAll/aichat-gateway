package com.bjoggis.admin;

import com.bjoggis.admin.properties.AdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebFluxSecurity
public class AdminGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminGatewayApplication.class, args);
  }


  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
    httpSecurity
        .authorizeExchange(exchanges ->
            exchanges
                .anyExchange().authenticated()
        )
        .oauth2Login(oauth -> {
        })
        .csrf(
//            csrf -> csrf.csrfTokenRepository(new CookieServerCsrfTokenRepository()));
            csrf -> csrf.disable());
    return httpSecurity.build();
  }

  @Component
  static
  class AddCsrfHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
      Mono<CsrfToken> tokenMono = (Mono<CsrfToken>) exchange.getAttributes()
          .get(CsrfToken.class.getName());
      if (tokenMono != null) {
        return tokenMono.flatMap(
            token -> chain.filter(exchange)); // When the application subscribes to this
        // producer, it causes the token to be generated, and it is automatically added to the response
      }
      return chain.filter(exchange);
    }
  }

  @Bean
  RouteLocator gateway(RouteLocatorBuilder rlb, AdminProperties properties) {
    // @formatter:off
    return rlb.routes()
        .route(r -> r.path("/api/spillhuset/**")
            .filters(f -> f
                .tokenRelay()
                .rewritePath("/api/spillhuset/(?<segment>.*)", "/${segment}"))
            .uri("http://spillhuset-bot-service"))
        .route(r -> r.path("/**")
            .filters(GatewayFilterSpec::tokenRelay)
            .uri(properties.uiUrl()))
        .build();
    // @formatter:on
  }
}
