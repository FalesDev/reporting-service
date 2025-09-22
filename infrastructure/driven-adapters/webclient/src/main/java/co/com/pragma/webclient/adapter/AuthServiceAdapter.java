package co.com.pragma.webclient.adapter;

import co.com.pragma.model.auth.LoginRequest;
import co.com.pragma.model.auth.TokenResponse;
import co.com.pragma.model.auth.gateways.AuthenticationGateway;
import co.com.pragma.model.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceAdapter implements AuthenticationGateway {

    private final WebClient authWebClient;

    @Value("${job.auth.email}")
    private String serviceEmail;

    @Value("${job.auth.password}")
    private String servicePassword;

    public AuthServiceAdapter(@Qualifier("authWebClient") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    @Override
    public Mono<String> login() {
        LoginRequest loginRequest = new LoginRequest(serviceEmail, servicePassword);

        return authWebClient
                .post()
                .uri("/auth/api/v1/login")
                .bodyValue(loginRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        return Mono.error(new UnauthorizedException("Invalid credentials for job"));
                    }
                    return response.createException().flatMap(Mono::error);
                })
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::getAccessToken);
    }
}
