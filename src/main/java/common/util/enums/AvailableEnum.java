package common.util.enums;

/**
 * @Description: AvailableEnum
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */
public enum AvailableEnum {
    UNAVAILABLE(0, "不可用"),
    AVAILABLE(1, "可用");

    private int id;
    private String des;

    AvailableEnum(int id, String des) {
        this.id = id;
        this.des = des;
    }


    //================================getter and setter ===============================
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
