package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema
public class alertTablePO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -3156620522710975976L;

    @Schema(description = "id")
    private int id;

    @Schema(description = "表单编号")
    private String tabledocno;

    @Schema(description = "tablename表单名称")
    private String tablename;

    @Schema(description = "父节点id")
    private Integer parentid;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "视图语句")
    private String sqlview;

    @Schema(description = "类型,1-表;2-视图")
    private String type;

    @Schema(description = "新表单编号")
    private String newTabledocno;

    @Schema(description = "表操作类型")
    private String tcontroType;

    @Schema(description = "字段明细")
    private List<alertFieldPO> alertFieldPOs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTabledocno() {
        return tabledocno;
    }

    public void setTabledocno(String tabledocno) {
        this.tabledocno = tabledocno;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSqlview() {
        return sqlview;
    }

    public void setSqlview(String sqlview) {
        this.sqlview = sqlview;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewTabledocno() {
        return newTabledocno;
    }

    public void setNewTabledocno(String newTabledocno) {
        this.newTabledocno = newTabledocno;
    }

    public String getTcontroType() {
        return tcontroType;
    }

    public void setTcontroType(String tcontroType) {
        this.tcontroType = tcontroType;
    }

    public List<alertFieldPO> getAlertFieldPOs() {
        return alertFieldPOs;
    }

    public void setAlertFieldPOs(List<alertFieldPO> alertFieldPOs) {
        this.alertFieldPOs = alertFieldPOs;
    }
}
