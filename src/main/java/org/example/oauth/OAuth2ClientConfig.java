package org.example.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Configuration
class OAuth2ClientConfig {
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService) {

        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    //For audience support
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService,
            @org.springframework.beans.factory.annotation.Value("${app.oauth2.audience:}") String audience) {

        var providerBuilder = OAuth2AuthorizedClientProviderBuilder.builder();
        providerBuilder.clientCredentials(builder -> {
            if (org.springframework.util.StringUtils.hasText(audience)) {
                var converter = new OAuth2ClientCredentialsGrantRequestEntityConverter() {
                    @Override
                    public org.springframework.http.RequestEntity<?> convert(OAuth2ClientCredentialsGrantRequest request) {
                        var entity = super.convert(request);
                        if (entity == null) return null;
                        var newBody = new LinkedMultiValueMap<>();
                        Object body = entity.getBody();
                        if (body instanceof MultiValueMap<?, ?> mv) {
                            mv.forEach((k, vList) -> vList.forEach(v -> newBody.add(String.valueOf(k), v == null ? null : String.valueOf(v))));
                        }
                        newBody.add("audience", audience);
                        return new org.springframework.http.RequestEntity<>(newBody, entity.getHeaders(), entity.getMethod(), entity.getUrl());
                    }
                };
                var client = new DefaultClientCredentialsTokenResponseClient();
                client.setRequestEntityConverter(converter);
                builder.accessTokenResponseClient(client);
            }
        });
        var provider = providerBuilder.build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

}

