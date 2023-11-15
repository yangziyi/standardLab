package lab.base.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lab.base.config.security.GrantedAuthorityImpl;
import lab.base.config.security.JwtAuthenticatioToken;
import lab.entity.HttpResult;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

/**
 * JWT工具类
 */
public class JwtTokenUtils implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private static final String USERNAME = Claims.SUBJECT;

    /**
     * 创建时间
     */
    private static final String CREATED = "created";
    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";
    /**
     * 密钥
     */
    private static final String SECRET = "standardLabProject15821659812749127590712957120957129047921047924791275901275912749721904712";
    /**
     * 有效期24小时
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 刷新有效期72小时
     */
    private static final long refresh_EXPIRE_TIME = 3 * 24 * 60 * 60 * 1000;

    /**
     * 生成令牌
     *
     * @param authentication 验证信息
     * @return 令牌
     */
    public static String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(USERNAME, SecurityUtils.getUsername(authentication));
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentication.getAuthorities());
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return generateToken(claims, expirationDate);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 根据请求令牌获取登录认证信息
     * @param token 令牌
     * @return 用户名
     */
    public static Authentication getAuthenticationsFromToken(HttpServletRequest request) {
        Authentication authentication = null;
        // 获取请求携带的令牌
        String token = JwtTokenUtils.getToken(request);
        String refreshToken = JwtTokenUtils.getRefreshToken(request);
        if(token != null) {
            Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            // 请求令牌不能为空
            if(SecurityUtils.getAuthentication() == null) {
                // 上下文中Authentication为空
                if(claims == null) {
                    return null;
                }
                if(username == null) {
                    return null;
                }

//                if(isTokenExpired(token)) {
//                    return null;
//                }
                int status = validateToken(token, refreshToken, username);
                if (status == 2) {
                    //如果验证码和刷新码都已过期，则重新登入
                    HttpResult result = HttpResult.getInstance();
                    result.setCode(HttpStatus.SC_FORBIDDEN);
                    result.setMsg("登入已过期，请重新登入");
                    throw new BadCredentialsException("登入已过期，请重新登入");
                } else if (status == 3) {
                    //如果验证码过期但刷新码未过期，则状态返回402,重新生成验证码和刷新码
                    HttpResult result = HttpResult.getInstance();
                    result.setCode(HttpStatus.SC_PAYMENT_REQUIRED);
                    claims.put(CREATED, new Date());
                    Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
                    token = generateToken(claims, expirationDate);
                    refreshToken = JwtTokenUtils.getRefreshToken(request);
                    JwtAuthenticatioToken inform
                            = new JwtAuthenticatioToken(null, null, token, refreshToken);
                    result.setInform(inform);
                }

                Object authors = claims.get(AUTHORITIES);
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                if (authors != null && authors instanceof List) {
                    for (Object object : (List) authors) {
                        authorities.add(new GrantedAuthorityImpl((String) ((Map) object).get("authority")));
                    }
                }
                authentication = new JwtAuthenticatioToken(username, null, authorities, token);
            } else {
                int status = validateToken(token, refreshToken, username);
                if(status == 0){
                    // 如果上下文中Authentication非空，且请求令牌合法，直接返回当前登录认证信息
                    authentication = SecurityUtils.getAuthentication();
                } else if (status == 2) {
                    //如果验证码和刷新码都已过期，则重新登入
                    HttpResult result = HttpResult.getInstance();
                    result.setCode(HttpStatus.SC_FORBIDDEN);
                    result.setMsg("登入已过期，请重新登入");
                    throw new BadCredentialsException("登入已过期，请重新登入");
                } else if (status == 3) {
                    //如果验证码过期但刷新码未过期，则状态返回402,重新生成验证码和刷新码
                    HttpResult result = HttpResult.getInstance();
                    result.setCode(HttpStatus.SC_PAYMENT_REQUIRED);
                    claims.put(CREATED, new Date());
                    Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
                    token = generateToken(claims, expirationDate);
                    refreshToken = JwtTokenUtils.getRefreshToken(request);
                    JwtAuthenticatioToken inform
                            = new JwtAuthenticatioToken(null, null, token, refreshToken);
                    result.setInform(inform);
                }
            }
        }
        return authentication;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }catch (PrematureJwtException e) {
            return e.getClaims();
        }catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 验证令牌
     * @param token
     * @param username
     * @return
     * 1-用户名与token用户名不一致
     * 2-验证码过期
     * 3-验证码过期但刷新码未过期
     */
    public static int validateToken(String token, String refreshToken, String username) {
        //登入用户名与token用户名一致
        String userName = getUsernameFromToken(token);
        if(!userName.equals(username)){
            return 1;
        }

        //验证码过期
        if(isTokenExpired(token) && isTokenExpired(refreshToken)){
            return 2;
        }

        //验证码过期但刷新码未过期
        if(isTokenExpired(token) && !isTokenExpired(refreshToken)){
            return 3;
        }

        return 0;
    }

    /**
     * 刷新令牌
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, new Date());
            Date expirationDate = new Date(System.currentTimeMillis() + refresh_EXPIRE_TIME);
            refreshedToken = generateToken(claims, expirationDate);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            boolean success = expiration.before(new Date());
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取请求token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if(token == null) {
            token = request.getHeader("token");
        } else if(token.contains(tokenHead)){
            token = token.substring(tokenHead.length());
        }
        if("".equals(token)) {
            token = null;
        }
        return token;
    }

    /**
     * 获取请求refresh_token
     * @param request
     * @return
     */
    public static String getRefreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if(token == null) {
            token = request.getHeader("refresh_token");
        } else if(token.contains(tokenHead)){
            token = token.substring(tokenHead.length());
        }
        if("".equals(token)) {
            token = null;
        }
        return token;
    }

}
