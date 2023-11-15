package lab.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 登录接口封装对象
 */
@Schema
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -4759819730051213047L;
	@Schema(description = "用户名")
	private String username;
	@Schema(description = "密码")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}