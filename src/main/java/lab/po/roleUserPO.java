package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serializable;
import java.util.List;

@Schema
public class roleUserPO extends generalInfo implements Serializable {
    private static final long serialVersionUID = 5798174172708319517L;

    @Schema(description = "角色id")
    private String roleid;

    @Schema(description = "用户id")
    private List<String> userid;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public List<String> getUserid() {
        return userid;
    }

    public void setUserid(List<String> userid) {
        this.userid = userid;
    }
}
