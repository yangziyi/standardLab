package lab.controller;

import jakarta.servlet.http.HttpServletRequest;
import lab.base.config.security.JwtAuthenticatioToken;
import lab.base.util.SecurityUtils;
import lab.entity.HttpResult;
import lab.entity.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

/**
 * 登录控制器
 */
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @RequestMapping(value = "/login" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
                
        return HttpResult.ok(token);
    }

}