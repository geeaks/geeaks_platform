package com.mljr.framework.core.model;

import java.util.Date;

/**
 * 抽象是数据记录实体对象
 * @Description:DbEntity.java
 * @Author:Sinamber
 * @Version:1.0
 * @Date:Aug 11, 2013 4:09:40 PM
 * @Copyright: www.sinamber.com All Rights Reserved. Copyright(c) 2013
 */
@SuppressWarnings("serial")
public abstract class DbEntity extends IdEntity {
    private Date created;
    private Date modified;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

}
