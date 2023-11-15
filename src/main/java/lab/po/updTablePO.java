package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema
public class updTablePO implements Serializable {

    @Serial
    private static final Long serialVersionUID = 596201466767866922L;

    @Schema(description = "表单信息")
    private tablePO tablePO;
    @Schema(description = "字段明细")
    private List<fieldPO> fieldPOs;

    public tablePO getTablePO() {
        return tablePO;
    }

    public void setTablePO(lab.po.tablePO tablePO) {
        this.tablePO = tablePO;
    }

    public List<fieldPO> getFieldPOs() {
        return fieldPOs;
    }

    public void setFieldPOs(List<fieldPO> fieldPOs) {
        this.fieldPOs = fieldPOs;
    }
}
