package com.yslt.doulao.dulao.enums;

/**
 * @Description: CashFromEnum
 * @anthor: shi_lin
 * @CreateTime: 2015-11-18
 */
public enum CashFromEnum {
    ONE_CATCH(1,"捞一捞"),
    FRIEND_SHARE(2,"朋友分享注册");
    private int typeId;
    private String description;

    CashFromEnum(int typeId, String description){
        this.description = description;
        this.typeId = typeId;
    }



    public static CashFromEnum getByNumber(int typeId) {
        for (CashFromEnum cashFromEnum : CashFromEnum.values()) {
            if (cashFromEnum.getTypeId() == typeId) {
                return cashFromEnum;
            }
        }
        throw new IllegalArgumentException(typeId + "Is not in OneCatchCashEnum");
    }


    //==============================getter and setter======================================

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
