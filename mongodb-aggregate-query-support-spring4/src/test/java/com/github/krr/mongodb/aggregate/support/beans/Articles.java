package com.github.krr.mongodb.aggregate.support.beans;

import com.github.krr.mongodb.aggregate.support.nonreactive.annotations.MongoId;

/**
 * Created by bhaasita on 10/21/2018
 */
public class Articles {

  @MongoId
  private String id;

  private String author;

  private int score;

  private int views;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getViews() {
    return views;
  }

  public void setViews(int views) {
    this.views = views;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Articles articles = (Articles) o;

    if (score != articles.score) return false;
    if (views != articles.views) return false;
    if (id != null ? !id.equals(articles.id) : articles.id != null) return false;
    return author != null ? author.equals(articles.author) : articles.author == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (author != null ? author.hashCode() : 0);
    result = 31 * result + score;
    result = 31 * result + views;
    return result;
  }
}
