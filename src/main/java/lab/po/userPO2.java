package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serializable;

@Schema
public class userPO2 extends generalInfo implements Serializable {
    private static final long serialVersionUID = -1275562783937654555L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "状态")
    private int state;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
