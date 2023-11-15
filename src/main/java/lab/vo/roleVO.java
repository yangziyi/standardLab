package lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema
public class roleVO implements Serializable {

    private static final long serialVersionUID = -6003081912360568911L;

    @Schema(description = "角色id")
    private String roleid;

    @Schema(description = "角色名称")
    private String rolename;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
