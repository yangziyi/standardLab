package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serializable;

/**
 * 登录接口封装对象
 */
@Schema
public class userPO extends generalInfo implements Serializable {

	private static final long serialVersionUID = -4759819730051213047L;

	@Schema(description = "id")
	private String id;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "密码")
	private String password;

	@Schema(description = "姓名")
	private String name;

	@Schema(description = "状态")
	private int state;

	@Schema(description = "起始位置")
	private int start;

	@Schema(description = "结束位置")
	private int end;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}