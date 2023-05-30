package com.liquorstore.cloud.gateway.filter;

import com.liquorstore.cloud.gateway.entity.User;
import com.liquorstore.cloud.gateway.model.CustomUserDetails;
import com.liquorstore.cloud.gateway.model.UserDetailsRequest;
import com.liquorstore.cloud.gateway.repository.UserRepository;
import com.liquorstore.cloud.gateway.util.JWTUtility;
import com.liquorstore.cloud.gateway.validator.RouterValidator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class JwtFilter implements GatewayFilter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouterValidator routerValidator;//custom route validator
    @Autowired
    private JWTUtility jwtUtility;

//    @Autowired
//    private RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String authorization = getAuthHeader(request);
        String token = null;
        String userName = null;

        if (routerValidator.isSecured.test(request)) {

            if(!isAuthMissing(authorization) && isAuthFormatValid(authorization)) {
                token = authorization.substring(7);
                userName = jwtUtility.getUsernameFromToken(token);
            } else if(isAuthMissing(authorization)) {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            } else if (!isAuthFormatValid(authorization)) {
                return this.onError(exchange, "Authorization header is value format is incorrect", HttpStatus.UNAUTHORIZED);
            }

//            CustomUserDetails customUserDetails = restTemplate.postForObject(
//                                                    "http://SECURITY-SERVICE/security/getUserDetails",
//                                                    new UserDetailsRequest(userName),
//                                                    CustomUserDetails.class);

            User user = userRepository.findByEmail(userName);

            if (!jwtUtility.isTokenValid(token, user.getEmail()))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }

    private boolean isAuthFormatValid(String authorization) {
        return authorization.startsWith("Bearer ");
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization");
    }

    private boolean isAuthMissing(String authorization) {
        return authorization == null;
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtility.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}