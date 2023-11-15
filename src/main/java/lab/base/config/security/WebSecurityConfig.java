package lab.base.config.security;

import jakarta.annotation.PostConstruct;
import lab.base.util.HttpStatus;
import lab.entity.HttpResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

//import org.springframework.security.core.userdetails.UserDetailsService;
//import com.base.config.security.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /*@Bean
	public void loginConfigure(AuthenticationManagerBuilder auth) throws Exception {

	    // 使用自定义登录身份认证组件
	    //auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
	    //auth.userDetailsService(userDetailsService);
	    auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
			    return charSequence.toString();
			}

	        @Override
	        public boolean matches(CharSequence charSequence, String s) {
	    		HttpResult result = HttpResult.getInstance();
        		boolean success = s.equals(charSequence.toString());
	        	if(!success && result.getMsg() == null ) {
	        		result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	        		result.setMsg("账号或密码错误");
	        		throw new BadCredentialsException("账号或密码错误");
	        	}
	            return success;
	        }
	    });
	}*/

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false);
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return charSequence.toString();
			}

			@Override
			public boolean matches(CharSequence charSequence, String s) {
				HttpResult result = HttpResult.getInstance();
				boolean bl = s.equals(DigestUtils.md5Hex(charSequence.toString()).toUpperCase());
				if(!bl) {
				//if(!s.equals(charSequence.toString())) {
					result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					result.setMsg("账号或密码错误");
					throw new BadCredentialsException("账号或密码错误");
				}
				return bl;
			}
		});
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        http
			.cors()
			.and().csrf().disable()
            .authorizeRequests()
            // 跨域预检请求
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // 登录URL
            .requestMatchers("/login").permitAll()
            // swagger
            .requestMatchers("/swagger**/**").permitAll()
            .requestMatchers("/webjars/**").permitAll()
			.requestMatchers("/v3/**").permitAll()
            .requestMatchers("/**").permitAll()
            // 其他所有请求需要身份认证
            .anyRequest().authenticated();
        // 退出登录处理器
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		AuthenticationConfiguration authenticationConfiguration = new AuthenticationConfiguration();
        // 开启登录认证流程过滤器
        http.addFilterBefore(new JwtLoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);
        // 访问控制时登录状态检查过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

		return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
    }

	/**
	 * 配置地址栏不能识别 // 的情况
	 * @return
	 */
	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		//此处可添加别的规则,目前只设置 允许双 //
		firewall.setAllowUrlEncodedDoubleSlash(true);
		return firewall;
	}

}
