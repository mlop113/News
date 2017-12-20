package com.android.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class UserMember implements Serializable
{

    private String address;
    private String dateCreate;
    private String email;
    private String img;
    private String loginName;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String userId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6227576774894892497L;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserMember() {
    }

    /**
     *
     * @param sex
     * @param phone
     * @param email
     * @param address
     * @param userId
     * @param name
     * @param img
     * @param dateCreate
     * @param password
     * @param loginName
     */
    public UserMember(String address, String dateCreate, String email, String img, String loginName, String name, String password, String phone, String sex, String userId) {
        super();
        this.address = address;
        this.dateCreate = dateCreate;
        this.email = email;
        this.img = img;
        this.loginName = loginName;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.sex = sex;
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
