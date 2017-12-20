package com.android.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Post implements Serializable {

    private String author;
    private String category;
    private Map<String, Comment> comments = null;
    private String content;
    private String dateCreate;
    private String description;
    private String img;
    private String postId;
    private List<String> tags = null;
    private String title;
    private List<String> userLikeIds = null;
    private List<String> userShareIds = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4825471899446248081L;

    /**
     * No args constructor for use in serialization
     */
    public Post() {
    }

    /**
     * @param tags
     * @param content
     * @param author
     * @param userShareIds
     * @param title
     * @param description
     * @param category
     * @param img
     * @param dateCreate
     * @param postId
     * @param comments
     * @param userLikeIds
     */
    public Post(String author, String category, Map<String, Comment> comments, String content, String dateCreate, String description, String img, String postId, List<String> tags, String title, List<String> userLikeIds, List<String> userShareIds) {
        super();
        this.author = author;
        this.category = category;
        this.comments = comments;
        this.content = content;
        this.dateCreate = dateCreate;
        this.description = description;
        this.img = img;
        this.postId = postId;
        this.tags = tags;
        this.title = title;
        this.userLikeIds = userLikeIds;
        this.userShareIds = userShareIds;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUserLikeIds() {
        return userLikeIds;
    }

    public void setUserLikeIds(List<String> userLikeIds) {
        this.userLikeIds = userLikeIds;
    }

    public List<String> getUserShareIds() {
        return userShareIds;
    }

    public void setUserShareIds(List<String> userShareIds) {
        this.userShareIds = userShareIds;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}