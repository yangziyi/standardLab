package lab.base.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义令牌对象
 */
public class JwtAuthenticatioToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private String token;

	private String refresh_token;

    public JwtAuthenticatioToken(Object principal, Object credentials){
    	super(principal, credentials);
    }
    
    public JwtAuthenticatioToken(Object principal, Object credentials, String token, String refresh_token){
    	super(principal, credentials);
    	this.token = token;
		this.refresh_token = refresh_token;
    }

    public JwtAuthenticatioToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
    	super(principal, credentials, authorities);
    	this.token = token;
		this.refresh_token = refresh_token;
    }
    
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
    	this.token = token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public static long getSerialversionuid() {
    	return serialVersionUID;
	}

}
