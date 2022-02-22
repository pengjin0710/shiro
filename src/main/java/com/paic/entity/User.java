package com.paic.entity;

import java.io.Serializable;

/**
 * user(User)实体类
 *
 * @author makejava
 * @since 2021-12-25 16:49:44
 */
public class User implements Serializable {
    private static final long serialVersionUID = 892273300707940745L;
    /**
     * id
     */
    private Long id;
    /**
     * name
     */
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

