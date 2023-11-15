package lab.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lab.entity.generalInfo;

import java.io.Serial;
import java.io.Serializable;

@Schema
public class controlsLogPO extends generalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8311203892995242012L;
    @Schema(description = "id")
    private int id;

    @Schema(description = "目标类")
    private String classname;

    @Schema(description = "方法名")
    private String process;

    @Schema(description = "操作")
    private String controls;

    @Schema(description = "请求数据")
    private String content;

    @Schema(description = "返回结果")
    private String result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getControls() {
        return controls;
    }

    public void setControls(String controls) {
        this.controls = controls;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
