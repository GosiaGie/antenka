package pl.volleylove.antenka.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class JwtIssuer {

    @Autowired
    private final JwtProperties jwtProperties;

    public String issue(long userID, String email, List<String> roles){

        return JWT.create()
                .withSubject(String.valueOf(userID))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("email", email)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }
}
