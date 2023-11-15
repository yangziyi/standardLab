package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serializable;

@Schema
public class rolePO extends generalInfo implements Serializable {

    private static final long serialVersionUID = -5178824123632941937L;

    @Schema(description = "角色id")
    private String id;

    @Schema(description = "角色名称")
    private String rolename;

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

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
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
