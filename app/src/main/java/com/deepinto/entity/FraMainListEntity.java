package com.deepinto.entity;

import com.deepinto.Bean.BaseBean;

/**
 * 首页瀑布流
 *
 * @author Ming
 */
public class FraMainListEntity extends BaseBean {

    private String imageurl;
    private String desc;
    private String price;
    private String buyers;

    public FraMainListEntity(String imageurl, String desc, String price, String buyers) {
        this.imageurl = imageurl;
        this.desc = desc;
        this.price = price;
        this.buyers = buyers;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuyers() {
        return buyers;
    }

    public void setBuyers(String buyers) {
        this.buyers = buyers;
    }

    @Override
    public String toString() {
        return "FraMainListEntity{" +
                "desc='" + desc + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", price='" + price + '\'' +
                ", buyers='" + buyers + '\'' +
                '}';
    }
}
