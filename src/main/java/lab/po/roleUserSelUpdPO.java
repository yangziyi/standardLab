package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema
public class roleUserSelUpdPO implements Serializable {
    private static final long serialVersionUID = -2038005021499371586L;

    @Schema(description = "角色id")
    private String roleid;

    @Schema(description = "用户id")
    private String userid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "起始位置")
    private int start;

    @Schema(description = "结束位置")
    private int end;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
