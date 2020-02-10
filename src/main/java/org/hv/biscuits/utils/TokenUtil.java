package org.hv.biscuits.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hv.biscuits.config.TokenConfig;
import org.hv.biscuits.controller.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2019/1/30
 */
@Component
public class TokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

    public static final String CLAIM_KEY_AVATAR = "avatar";
    private static final String CLAIM_KEY_CREATED = "created";
    public static final String CLAIM_KEY_AUTH = "auth";

    private String tokenSecret;
    private long tokenExpiration;
    private long refreshTime;

    @Autowired
    public TokenUtil(TokenConfig tokenConfig) {
        tokenSecret = tokenConfig.getSecret();
        tokenExpiration = tokenConfig.getExpiration();
        refreshTime = tokenConfig.getRefreshTime();
    }

    public String generateToken(UserView user) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_KEY_AVATAR, user.getAvatar());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUTH, String.join(",", user.getAuthIds()));
        return this.generateToken(claims);
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String refreshToken(Claims claims) {
        String refreshedToken;
        try {
            claims.remove(CLAIM_KEY_CREATED, new Date());
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = this.generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public boolean shouldRefresh(Claims claims) {
        if (!claims.containsKey(CLAIM_KEY_CREATED)) {
            return false;
        }
        long createTime = (long) claims.get(CLAIM_KEY_CREATED);
        long plusTimeMillis = tokenExpiration - refreshTime;
        if (plusTimeMillis <= 0) {
            throw new IllegalArgumentException("The expiration of token shall not be less than refresh time of token.");
        }
        return new Date(createTime + tokenExpiration - refreshTime).before(new Date());
    }
}
