package org.example.oauth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
class TokenController {
    private final OAuth2AuthorizedClientManager manager;

    TokenController(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @GetMapping("/client-token")
    public Map<String, Object> token(@RequestParam(defaultValue = "ping") String registrationId,
                                     @RequestParam(defaultValue = "false") boolean includeValue) {
        var principal = new AnonymousAuthenticationToken(
                "key", "client-credentials", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        var request = OAuth2AuthorizeRequest.withClientRegistrationId(registrationId)
                .principal(principal)
                .build();

        var authorized = manager.authorize(request);
        if (authorized == null || authorized.getAccessToken() == null) {
            return Map.of("error", "Failed to obtain access token for registration: " + registrationId);
        }
        OAuth2AccessToken token = authorized.getAccessToken();
        Map<String, Object> body = new java.util.LinkedHashMap<>();
        if (includeValue) body.put("access_token", token.getTokenValue());
        body.put("token_type", token.getTokenType().getValue());
        body.put("scopes", token.getScopes());
        body.put("issued_at", token.getIssuedAt());
        body.put("expires_at", token.getExpiresAt());
        body.put("registration_id", registrationId);
        return body;
    }
}

