package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class fieldPO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7999919469820719669L;

    @Schema(description = "id")
    private int id;

    @Schema(description = "表单id")
    private int tableid;

    @Schema(description = "表单编号")
    private String tabledocno;

    @Schema(description = "字段编号")
    private String fielddocno;

    @Schema(description = "字段名")
    private String fieldname;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "字段类型")
    private String widget;

    @Schema(description = "字段布局")
    private String widgetLayout;

    @Schema(description = "校验")
    private String verify;

    @Schema(description = "是否删除，0-否；1-是，默认是0")
    private Integer isdelete;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "VARCHAR,INT等")
    private String type;

    @Schema(description = "长度")
    private Integer length;

    @Schema(description = "小数部分长度")
    private Integer decimals;

    @Schema(description = "非空,Y不可为空，N可为空")
    private String notNull;

    @Schema(description = "主键,Y是，N不是")
    private String primaryKey;

    @Schema(description = "自增,Y是，N不是")
    private String increment;

    @Schema(description = "默认值")
    private String defaults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    public String getTabledocno() {
        return tabledocno;
    }

    public void setTabledocno(String tabledocno) {
        this.tabledocno = tabledocno;
    }

    public String getFielddocno() {
        return fielddocno;
    }

    public void setFielddocno(String fielddocno) {
        this.fielddocno = fielddocno;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getWidgetLayout() {
        return widgetLayout;
    }

    public void setWidgetLayout(String widgetLayout) {
        this.widgetLayout = widgetLayout;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }
}
