package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serializable;

@Schema
public class userUploadPO extends generalInfo implements Serializable {
    private static final long serialVersionUID = -3798761455161265749L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "密码")
    private String password;

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

}
