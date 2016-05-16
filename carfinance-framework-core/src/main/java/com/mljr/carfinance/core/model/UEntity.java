package com.mljr.carfinance.core.model;


/**
 * 抽象是数据记录实体对象
 * @Description:UEntity.java
 * @Author:Sinamber
 * @Version:1.0
 * @Date:Aug 11, 2013 4:09:40 PM
 * @Copyright: www.sinamber.com All Rights Reserved. Copyright(c) 2013
 */
@SuppressWarnings("serial")
public abstract class UEntity extends DbEntity {
    private Long cuid;//创建用户id
    private Long muid;//修改用户id

    public Long getCuid() {
        return cuid;
    }

    public void setCuid(Long cuid) {
        this.cuid = cuid;
    }

    public Long getMuid() {
        return muid;
    }

    public void setMuid(Long muid) {
        this.muid = muid;
    }
}
