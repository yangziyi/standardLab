package lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class tableVO extends generalInfo implements Serializable {


    @Serial
    private static final long serialVersionUID = 8111724478958677802L;
    @Schema(description = "id")
    private int id;

    @Schema(description = "表单编号")
    private String tabledocno;

    @Schema(description = "tablename表单名称")
    private String tablename;

    @Schema(description = "父节点id")
    private int parentid;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建表单/视图语句")
    private String sql;

    @Schema(description = "视图语句")
    private String sqlview;

    @Schema(description = "类型,1-表;2-视图")
    private String type;

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

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
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
}
