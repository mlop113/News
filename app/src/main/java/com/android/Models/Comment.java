package com.android.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Comment implements Serializable
{

    private String commentId;
    private String dateCreate;
    private String message;
    private Map<String,ReplyComment> replyComments = null;
    private List<String> userLikeIds = null;
    private String userOfCommentId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6995955364785009071L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Comment() {
    }

    /**
     *
     * @param message
     * @param replyComments
     * @param userOfCommentId
     * @param dateCreate
     * @param commentId
     * @param userLikeIds
     */
    public Comment(String commentId, String dateCreate, String message, Map<String,ReplyComment> replyComments, List<String> userLikeIds, String userOfCommentId) {
        super();
        this.commentId = commentId;
        this.dateCreate = dateCreate;
        this.message = message;
        this.replyComments = replyComments;
        this.userLikeIds = userLikeIds;
        this.userOfCommentId = userOfCommentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String,ReplyComment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(Map<String,ReplyComment> replyComments) {
        this.replyComments = replyComments;
    }

    public List<String> getUserLikeIds() {
        return userLikeIds;
    }

    public void setUserLikeIds(List<String> userLikeIds) {
        this.userLikeIds = userLikeIds;
    }

    public String getUserOfCommentId() {
        return userOfCommentId;
    }

    public void setUserOfCommentId(String userOfCommentId) {
        this.userOfCommentId = userOfCommentId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
