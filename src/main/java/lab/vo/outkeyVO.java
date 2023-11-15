package lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class outkeyVO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3744429984571153856L;
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

    @Override
    public String toString() {
        return this.id + "," + this.tabledocno + "," + this.outTabledocno + "," + this.field + "," + this.outField;
    }
}
