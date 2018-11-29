package com.github.krr.mongodb.aggregate.support.repository;

import com.github.krr.mongodb.aggregate.support.annotations.Aggregate;
import com.github.krr.mongodb.aggregate.support.annotations.Limit;
import com.github.krr.mongodb.aggregate.support.beans.Score;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by bhaasita on 10/22/2018
 */
public interface LimitRepository extends MongoRepository<Score, Integer> {

  @Aggregate(inputType = Score.class, outputBeanType = Score.class)
  @Limit(query = "?0", order = 0)
  List<Score> getScoresAfterLimit(int limitNumber);

}
