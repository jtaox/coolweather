package com.jtao.coolweather.model;

import org.json.JSONArray;

/**
 * Created by jtaob on 2015/9/24.
 */
public class Index{
    public String title;
    /**
     * 详细描述信息
     */
    public String desc;
    /**
     * 简单描述信息
     */
    public String zs;

    /**
     *
     * @param title 标题
     * @param desc 详细描述信息
     * @param zs 简单描述信息
     */
    public Index(String title, String desc, String zs) {
        this.title = title;
        this.desc = desc;
        this.zs = zs;
    }


}