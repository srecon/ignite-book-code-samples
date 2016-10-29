package com.blu.imdg.jdbc.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by developer on 9/26/16.
 */
public class Post implements Serializable {
    private static final long serialVersionUID = 0L;

    private String id;
    private String title;
    private String description;
    private LocalDate creationDate;
    private String author;

    public Post() {
    }

    public Post(String id, String title, String description, LocalDate creationDate, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Post post = (Post)o;
        return Objects.equals(id, post.id) &&
            Objects.equals(title, post.title) &&
            Objects.equals(description, post.description) &&
            Objects.equals(creationDate, post.creationDate) &&
            Objects.equals(author, post.author);
    }

    @Override public int hashCode() {
        return Objects.hash(id, title, description, creationDate, author);
    }

    @Override public String toString() {
        return "Post{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", creationDate=" + creationDate +
            ", author='" + author + '\'' +
            '}';
    }
}
