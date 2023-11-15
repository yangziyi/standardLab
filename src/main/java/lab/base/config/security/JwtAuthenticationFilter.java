package lab.base.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.base.util.HttpStatus;
import lab.base.util.JwtTokenUtils;
import lab.base.util.SecurityUtils;
import lab.entity.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;


import java.io.IOException;

/**
 * 登录认证检查过滤器
 */
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //对返回对象初始化
        HttpResult result = HttpResult.getInstance();
        result.setCode(0);
        result.setMsg(null);
        result.setInform(null);
        result.setPayload(null);

        // 获取token, 并检查登录状态
        SecurityUtils.checkAuthentication(request);
        chain.doFilter(request, response);
    }
    
}
