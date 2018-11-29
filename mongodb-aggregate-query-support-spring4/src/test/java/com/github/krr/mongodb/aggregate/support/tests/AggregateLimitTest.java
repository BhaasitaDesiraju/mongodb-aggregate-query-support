package com.github.krr.mongodb.aggregate.support.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.krr.mongodb.aggregate.support.beans.Score;
import com.github.krr.mongodb.aggregate.support.config.AggregateTestConfiguration;
import com.github.krr.mongodb.aggregate.support.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by bhaasita on 10/22/2018
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ContextConfiguration(classes = AggregateTestConfiguration.class)
public class AggregateLimitTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private LimitRepository limitRepository;

  private static final String[] SCORE_DOCS = {"{ \"_id\" : 1, \"subject\" : \"History\", \"score\" : 88 }",
          "{ \"_id\" : 2, \"subject\" : \"History\", \"score\" : 92 }",
          "{ \"_id\" : 3, \"subject\" : \"History\", \"score\" : 97 }",
          "{ \"_id\" : 4, \"subject\" : \"History\", \"score\" : 71 }",
          "{ \"_id\" : 5, \"subject\" : \"History\", \"score\" : 79 }",
          "{ \"_id\" : 6, \"subject\" : \"History\", \"score\" : 83 }"};

  @BeforeClass
  @SuppressWarnings("Duplicates")
  public void setup() throws Exception {
    limitRepository.deleteAll();
    ObjectMapper mapper = new ObjectMapper();
    List<Score> scores = Arrays.stream(SCORE_DOCS).map(s -> {
      try {
        return mapper.readValue(s, Score.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());
    limitRepository.insert(scores);
  }

  @Test
  public void mustLimitDocumentsFromRepository() {
    assertNotNull(limitRepository, "Must have a repository");
    List<Score> scores = limitRepository.findAll();
    assertNotNull(scores);
    assertEquals(scores.size(), SCORE_DOCS.length);
    List scoresAfterLimit = limitRepository.getScoresAfterLimit(3);
    assertNotNull(scoresAfterLimit);
    assertEquals(scoresAfterLimit.size(), 3);
  }
}
