package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class delTablePO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7909989776323106220L;
    @Schema(description = "表单编号")
    private String tabledocno;

    public String getTabledocno() {
        return tabledocno;
    }

    public void setTabledocno(String tabledocno) {
        this.tabledocno = tabledocno;
    }
}
