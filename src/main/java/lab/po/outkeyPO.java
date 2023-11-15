package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class outkeyPO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6229954024781805601L;
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "表单编号")
    private String tabledocno;

    @Schema(description = "外键表单编号")
    private String outTabledocno;

    @Schema(description = "字段，多字段用,隔开")
    private String field;

    @Schema(description = "字段，多字段用,隔开")
    private String outField;

    @Schema(description = "操作类型")
    private String fcontroType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTabledocno() {
        return tabledocno;
    }

    public void setTabledocno(String tabledocno) {
        this.tabledocno = tabledocno;
    }

    public String getOutTabledocno() {
        return outTabledocno;
    }

    public void setOutTabledocno(String outTabledocno) {
        this.outTabledocno = outTabledocno;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOutField() {
        return outField;
    }

    public void setOutField(String outField) {
        this.outField = outField;
    }

    public String getFcontroType() {
        return fcontroType;
    }

    public void setFcontroType(String fcontroType) {
        this.fcontroType = fcontroType;
    }
}
