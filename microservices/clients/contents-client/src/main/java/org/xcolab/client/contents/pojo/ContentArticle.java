package org.xcolab.client.contents.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ContentArticle implements Serializable {

    private static final long serialVersionUID = -2076774009;

    private Long contentArticleId;
    private Long authorId;
    private Timestamp createDate;
    private Long maxVersionId;
    private Long editRoleGroupId;
    private Long viewRoleGroupId;
    private Boolean visible;
    private String title;
    private Long folderId;

    public ContentArticle() {
    }

    public ContentArticle(Long contentArticleId, Long authorId, Timestamp createDate,
            Long maxVersionId, Long editRoleGroupId, Long viewRoleGroupId, Boolean visible,
            String title, Long folderId) {
        this.contentArticleId = contentArticleId;
        this.authorId = authorId;
        this.createDate = createDate;
        this.maxVersionId = maxVersionId;
        this.editRoleGroupId = editRoleGroupId;
        this.viewRoleGroupId = viewRoleGroupId;
        this.visible = visible;
        this.title = title;
        this.folderId = folderId;
    }

    public Long getContentArticleId() {
        return this.contentArticleId;
    }

    public void setContentArticleId(Long contentArticleId) {
        this.contentArticleId = contentArticleId;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getMaxVersionId() {
        return this.maxVersionId;
    }

    public void setMaxVersionId(Long maxVersionId) {
        this.maxVersionId = maxVersionId;
    }

    public Long getEditRoleGroupId() {
        return this.editRoleGroupId;
    }

    public void setEditRoleGroupId(Long editRoleGroupId) {
        this.editRoleGroupId = editRoleGroupId;
    }

    public Long getViewRoleGroupId() {
        return this.viewRoleGroupId;
    }

    public void setViewRoleGroupId(Long viewRoleGroupId) {
        this.viewRoleGroupId = viewRoleGroupId;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    @JsonIgnore
    public String getLinkUrl() {
        return "/web/guest/wiki/-/wiki/page/" + title;
    }

    @Override
    public String toString() {
        return "ContentArticle (" + contentArticleId +
                ", " + authorId +
                ", " + createDate +
                ", " + maxVersionId +
                ", " + editRoleGroupId +
                ", " + viewRoleGroupId +
                ", " + visible +
                ")";
    }
}