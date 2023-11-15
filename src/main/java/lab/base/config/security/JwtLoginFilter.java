package lab.base.config.security;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lab.base.util.HttpStatus;
import lab.base.util.HttpUtils;
import lab.base.util.JwtTokenUtils;
import lab.entity.HttpResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 启动登录认证流程过滤器
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	public JwtLoginFilter(AuthenticationManager authManager) {
        setAuthenticationManager(authManager);
    }
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// POST 请求 /login 登录时拦截， 由此方法触发执行登录认证流程，可以在此覆写整个登录认证逻辑
		super.doFilter(req, res, chain);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// 可以在此覆写尝试进行登录认证的逻辑，登录成功之后等操作不再此方法内
		// 如果使用此过滤器来触发登录认证流程，注意登录请求数据格式的问题
		// 此过滤器的用户名密码默认从request.getParameter()获取，但是这种
		// 读取方式不能读取到如 application/json 等 post 请求数据，需要把
		// 用户名密码的读取逻辑修改为到流中读取request.getInputStream()

		String body = getBody(request);
		JSONObject jsonObject = JSON.parseObject(body);
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		JwtAuthenticatioToken authRequest = new JwtAuthenticatioToken(username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// 存储登录认证信息到上下文
		SecurityContextHolder.getContext().setAuthentication(authResult);
		// 记住我服务
		getRememberMeServices().loginSuccess(request, response, authResult);
		// 触发事件监听器
		if (this.eventPublisher != null) {
			eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
		}
		// 生成并返回token给客户端，后续访问携带此token
		String access_token = JwtTokenUtils.generateToken(authResult);
		String refresh_token = JwtTokenUtils.refreshToken(access_token);
		JwtAuthenticatioToken token
				= new JwtAuthenticatioToken(null, null, access_token, refresh_token);

		HttpResult result = HttpResult.getInstance();
		result.setCode(HttpStatus.SC_OK);
		result.setMsg("欢迎您," + JwtTokenUtils.getUsernameFromToken(access_token));
		result.setInform(token);
		result.setPayload(null);
		HttpUtils.write(response, result);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,HttpServletResponse response, 
			AuthenticationException failed) throws IOException, ServletException {
		getRememberMeServices().loginFail(request, response);
		
		HttpUtils.errorWrite(response);
	}
	
	/** 
	 * 获取请求Body
	 * @param request
	 * @return
	 */
	public String getBody(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
