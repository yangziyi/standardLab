package lab.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class userInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2349775220950553079L;



    //线程安全的
    //类初始化时，立即加载这个对象
    private static userInfo instance = new userInfo();

    private userInfo() {

    }

    //方法没有加同步块，所以它效率高
    public static userInfo getInstance() {
        return instance;
    }

    @Schema(description = "所有者id")
    private int ownerid;
    @Schema(description = "所有者用户")
    private String ownerusername;
    @Schema(description = "所有者姓名")
    private String ownername;
    @Schema(description = "创建时间")
    private String createtime;
    @Schema(description = "最后修改者id")
    private int updateid;
    @Schema(description = "最后修改者用户名")
    private String updateusername;
    @Schema(description = "最后修改者姓名")
    private String updatename;
    @Schema(description = "最后修改时间")
    private String updatetime;

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnerusername() {
        return ownerusername;
    }

    public void setOwnerusername(String ownerusername) {
        this.ownerusername = ownerusername;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getUpdateid() {
        return updateid;
    }

    public void setUpdateid(int updateid) {
        this.updateid = updateid;
    }

    public String getUpdateusername() {
        return updateusername;
    }

    public void setUpdateusername(String updateusername) {
        this.updateusername = updateusername;
    }

    public String getUpdatename() {
        return updatename;
    }

    public void setUpdatename(String updatename) {
        this.updatename = updatename;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
