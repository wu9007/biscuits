package org.hv.biscuit.utils;

import org.hv.biscuit.config.TokenConfig;
import org.hv.biscuit.spine.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujianchuan 2019/1/30
 */
@Component(value = "BiscuitJwtUtil")
public class TokenUtil {
    private static final String CLAIM_KEY_AVATAR = "avatar";
    private static final String CLAIM_KEY_CREATED = "created";

    private final TokenConfig tokenConfig;

    @Autowired
    public TokenUtil(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public TokenConfig getTokenConfig() {
        return tokenConfig;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_KEY_AVATAR, user.getAvatar());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return this.generateToken(claims);
    }

    public Boolean validateToken(String token, User user) {
        Date created = this.getCreatedDateFromToken(token);
        return !isTokenExpired(token) && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate());
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = this.generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String getAvatarByToken(String token) {
        String avatar;
        try {
            final Claims claims = getClaimsFromToken(token);
            avatar = (String) claims.get(CLAIM_KEY_AVATAR);
        } catch (Exception e) {
            avatar = null;
        }
        return avatar;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = this.getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(this.generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, tokenConfig.getSecret())
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(tokenConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.tokenConfig.getExpiration() * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Claims claims = getClaimsFromToken(token);
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
}
