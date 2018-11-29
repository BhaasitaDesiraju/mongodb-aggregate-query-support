package com.github.krr.mongodb.aggregate.support.repository;

import com.github.krr.mongodb.aggregate.support.annotations.Aggregate;
import com.github.krr.mongodb.aggregate.support.annotations.Match;
import com.github.krr.mongodb.aggregate.support.beans.Articles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by bhaasita on 10/21/2018
 */
public interface MatchRepository extends MongoRepository<Articles, Integer> {
  @Aggregate(inputType = Articles.class, outputBeanType = Articles.class)
  @Match(query = "{author : '?0'}", order = 0)
  List<Articles> getArticlesAfterMatch(String author);
}