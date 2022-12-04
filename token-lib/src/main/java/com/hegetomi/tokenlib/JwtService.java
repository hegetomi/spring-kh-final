package com.hegetomi.tokenlib;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String ISSUER = "ws-final";

    @Value("${com.hegetomi.tokenlib.keypaths.private:#{null}}")
    private String pathToPemWithPrivateKey;
    @Value("${com.hegetomi.tokenlib.keypaths.public:#{null}}")
    private String pathToPemWithPublicKey;

    private Algorithm signerAlg;
    private Algorithm validatorAlg;

    @PostConstruct
    public void init() throws Exception {
        if (pathToPemWithPrivateKey != null) {
            signerAlg = Algorithm.ECDSA512(null, (ECPrivateKey) PemUtils.getPrivateKey(pathToPemWithPrivateKey));
        }
        if (pathToPemWithPublicKey != null) {
            validatorAlg = Algorithm.ECDSA512((ECPublicKey) PemUtils.getPublicKey(pathToPemWithPublicKey), null);
        }

    }

    public String createJwtToken(UserDetails principal) {

        return JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .withIssuer(ISSUER)
                .sign(signerAlg);
    }

    public UserDetails parseJwt(String jwtToken) {
        DecodedJWT dec = JWT.require(validatorAlg)
                .withIssuer(ISSUER)
                .build()
                .verify(jwtToken);

        return new User(dec.getSubject(), "dummy",
                dec.getClaim("auth").asList(String.class)
                        .stream().map(SimpleGrantedAuthority::new).toList()
        );
    }
}
