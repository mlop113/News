package com.android.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class ReplyComment implements Serializable
{
    private String content;
    private String dateCreate;
    private String userReplyId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3024150865873725111L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ReplyComment() {
    }

    /**
     *
     * @param content
     * @param userReplyId
     * @param dateCreate
     */
    public ReplyComment(String content, String dateCreate, String userReplyId) {
        super();
        this.content = content;
        this.dateCreate = dateCreate;
        this.userReplyId = userReplyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserReplyId() {
        return userReplyId;
    }

    public void setUserReplyId(String userReplyId) {
        this.userReplyId = userReplyId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
