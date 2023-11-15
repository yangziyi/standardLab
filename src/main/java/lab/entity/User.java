package lab.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema
public class User implements Serializable {
    private static final long serialVersionUID = -1275562783937654555L;

    @Schema(description = "角色id")
    private int id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户名称")
    private String name;
    @Schema(description = "密码")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

}
