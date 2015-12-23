package common.util.pojo;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import common.util.enums.AvailableEnum;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 实体通用属性类，所有doulao数据实体都必须继承此类
 * @anthor: shi_lin
 * @CreateTime: 2015-12-04
 */

public class BaseDomain implements Serializable, Cloneable {
    private static final long serialVersionUID = 2820473684086191843L;

    @Id
    protected ObjectId id;   //主键

   /* @Property("create_time")
    protected Date createTime;   //创建时间

    @Property("update_time")
    protected  Date updateTime;   //修改时间*/

    @Property("create_time")
    protected String createTime;   //创建时间

    @Property("update_time")
    protected  String updateTime;
    @Property("available")
    protected AvailableEnum availableEnum;


    //===============================getter and setter ===================================


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

/*    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }*/

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public AvailableEnum getAvailableEnum() {
        return availableEnum;
    }

    public void setAvailableEnum(AvailableEnum availableEnum) {
        this.availableEnum = availableEnum;
    }
}
