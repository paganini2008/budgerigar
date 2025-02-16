/*
 * This file is generated by jOOQ.
 */
package com.github.budgerigar.db.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class BgrigarDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String title;
    private String extention;
    private String path;
    private LocalDateTime lastModified;
    private String content;

    public BgrigarDocument() {}

    public BgrigarDocument(BgrigarDocument value) {
        this.id = value.id;
        this.name = value.name;
        this.title = value.title;
        this.extention = value.extention;
        this.path = value.path;
        this.lastModified = value.lastModified;
        this.content = value.content;
    }

    public BgrigarDocument(
        Integer id,
        String name,
        String title,
        String extention,
        String path,
        LocalDateTime lastModified,
        String content
    ) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.extention = extention;
        this.path = path;
        this.lastModified = lastModified;
        this.content = content;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.ID</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.ID</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.NAME</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.NAME</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.TITLE</code>.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.TITLE</code>.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.EXTENTION</code>.
     */
    public String getExtention() {
        return this.extention;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.EXTENTION</code>.
     */
    public void setExtention(String extention) {
        this.extention = extention;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.PATH</code>.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.PATH</code>.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.LAST_MODIFIED</code>.
     */
    public LocalDateTime getLastModified() {
        return this.lastModified;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.LAST_MODIFIED</code>.
     */
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Getter for <code>PUBLIC.BGRIGAR_DOCUMENT.CONTENT</code>.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Setter for <code>PUBLIC.BGRIGAR_DOCUMENT.CONTENT</code>.
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BgrigarDocument other = (BgrigarDocument) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.title == null) {
            if (other.title != null)
                return false;
        }
        else if (!this.title.equals(other.title))
            return false;
        if (this.extention == null) {
            if (other.extention != null)
                return false;
        }
        else if (!this.extention.equals(other.extention))
            return false;
        if (this.path == null) {
            if (other.path != null)
                return false;
        }
        else if (!this.path.equals(other.path))
            return false;
        if (this.lastModified == null) {
            if (other.lastModified != null)
                return false;
        }
        else if (!this.lastModified.equals(other.lastModified))
            return false;
        if (this.content == null) {
            if (other.content != null)
                return false;
        }
        else if (!this.content.equals(other.content))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
        result = prime * result + ((this.extention == null) ? 0 : this.extention.hashCode());
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
        result = prime * result + ((this.lastModified == null) ? 0 : this.lastModified.hashCode());
        result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BgrigarDocument (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(title);
        sb.append(", ").append(extention);
        sb.append(", ").append(path);
        sb.append(", ").append(lastModified);
        sb.append(", ").append(content);

        sb.append(")");
        return sb.toString();
    }
}
