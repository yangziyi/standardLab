package lab.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class menuVO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2592138606787510960L;

    @Schema(description = "id")
    private int id;

    @Schema(description = "父节点id")
    private int parentid;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "前端页面")
    private String componet;

    @Schema(description = "前端路由")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "在tags是否固定")
    private int affix;

    @Schema(description = "排序")
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComponet() {
        return componet;
    }

    public void setComponet(String componet) {
        this.componet = componet;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getAffix() {
        return affix;
    }

    public void setAffix(int affix) {
        this.affix = affix;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
