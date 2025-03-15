package io.github.hoangtuyen04work.social_backend.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import io.github.hoangtuyen04work.social_backend.entities.Authority;
import io.github.hoangtuyen04work.social_backend.entities.RoleEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import com.nimbusds.jwt.SignedJWT;import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenUtils {
    private String SIGNER_KEY;

    //Check token valid
    public void isValidToken(String token) throws JOSEException, ParseException, AppException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.NOT_AUTHENTICATION);
        }
    }

    //Generate token
    public String generateToken(UserEntity user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet;
        if(user.getRoles().size() == 1){
            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .issuer("hoangtuyen.com")
                    .subject(user.getId())
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(48*60*60, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("roles",buildRoles(user.getRoles()))
                    .build();
        }
        else{
            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .issuer("hoangtuyen.com")
                    .subject(user.getId())
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(48*60*60, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("roles",buildRoles(user.getRoles()))
                    .claim("authorities", buildAuthorities(user.getRoles()))
                    .build();
        }
        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(jwtClaimsSet.toJSONObject()));
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    //build list Roles for claim roles
    private List<String> buildRoles(Set<RoleEntity> roles){
        List<String> list = new ArrayList<>();
        for(RoleEntity role : roles) list.add(role.getRoleName());
        return list;
    }

    //Build list authorities for claim authorities
    private List<String> buildAuthorities(Set<RoleEntity> roles){
        List<String> list = new ArrayList<>();
        for(RoleEntity role : roles)
            for(Authority auth : role.getAuthorities())
                list.add(auth.getName());
        return list;
    }
}
