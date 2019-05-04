package com.example.demo;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Import(SecurityConfiguration.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


    @Bean
    public EventStore eventStore() {
        InMemoryEventStorageEngine storageEngine = new InMemoryEventStorageEngine();
        return new EmbeddedEventStore.Builder()
                .storageEngine(storageEngine)
                .build();
    }


    @Bean
    public CorrelationDataProvider correlationDataProvider() {
        return new CorrelationDataProvider() {
            @Override
            public Map<String, ?> correlationDataFor(Message<?> message) {
                Map<String, Object> m = new HashMap<>();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication!=null && authentication.isAuthenticated()) {
                    m.put("userId", ((KeycloakPrincipal)authentication.getPrincipal()).getKeycloakSecurityContext().getToken().getId());
                    m.put("username", ((KeycloakPrincipal)authentication.getPrincipal()).getKeycloakSecurityContext().getToken().getPreferredUsername());
                } else {
                    m.put("userId", "empty-user-id");
                }
                return m;
            }
        };
    }


    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AccessToken getAccessToken() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes()).getRequest();
        return ((KeycloakPrincipal) request.getUserPrincipal())
                .getKeycloakSecurityContext().getToken();
    }

}
