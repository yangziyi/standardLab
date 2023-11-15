package lab.entity;

public class DataEntity {
    //内容
    private String value;
    //字体颜色
    private String color;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public DataEntity(String value , String color){
        this.value = value;
        this.color = color;
    }
}
