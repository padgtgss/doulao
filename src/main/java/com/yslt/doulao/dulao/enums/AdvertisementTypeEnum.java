package com.yslt.doulao.dulao.enums;

/**
 * @Description: AdvertisementTypeEnum
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */

public enum AdvertisementTypeEnum {
    FLASH(4, "闪屏广告"),
    BANNER(5, "首页轮播广告"),
    DYNAMIC(6, "动态插播广告");

    private int typeId;  //类型ID
    private String description;  //描述

    AdvertisementTypeEnum(int typeId, String description) {
        this.description = description;
        this.typeId = typeId;
    }


    public static AdvertisementTypeEnum getByType(int typeId) {
        for (AdvertisementTypeEnum cashFromEnum : AdvertisementTypeEnum.values()) {
            if (cashFromEnum.getTypeId() == typeId) {
                return cashFromEnum;
            }
        }
        throw new IllegalArgumentException(typeId + "Is not in AdvertisementTypeEnum");
    }

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
