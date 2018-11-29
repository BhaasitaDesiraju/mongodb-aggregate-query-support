package com.github.krr.mongodb.aggregate.support.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.krr.mongodb.aggregate.support.beans.Articles;
import com.github.krr.mongodb.aggregate.support.config.AggregateTestConfiguration;
import com.github.krr.mongodb.aggregate.support.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by bhaasita on 10/20/2018
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ContextConfiguration(classes = AggregateTestConfiguration.class)
public class AggregateMatchTest extends AbstractTestNGSpringContextTests {

  private static final String[] ARTICLES_DOCS = {"{ \"_id\" : \"512bc95fe835e68f199c8686\", \"author\" : \"dave\", " +
          "\"score\" : 80, \"views\" : 100 }",
          "{ \"_id\" : \"512bc962e835e68f199c8687\", \"author\" : \"dave\"," +
                  " \"score\" : 85, \"views\" : 521 }",
          "{ \"_id\" : \"55f5a192d4bede9ac365b257\", \"author\" : \"ahn\", " +
                  "\"score\" : 60, \"views\" : 1000 }",
          "{ \"_id\" : \"55f5a192d4bede9ac365b258\", \"author\" : \"li\", " +
                  "\"score\" : 55, \"views\" : 5000 }",
          "{ \"_id\" : \"55f5a1d3d4bede9ac365b259\", \"author\" : \"annT\", " +
                  "\"score\" : 60, \"views\" : 50 }",
          "{ \"_id\" : \"55f5a1d3d4bede9ac365b25a\", \"author\" : \"li\", " +
                  "\"score\" : 94, \"views\" : 999 }",
          "{ \"_id\" : \"55f5a1d3d4bede9ac365b25b\", \"author\" : \"ty\", " +
                  "\"score\" : 95, \"views\" : 1000 }"};


  private static final String[] MATCHED_DOCS = {"{\"_id\" : \"512bc95fe835e68f199c8686\", \"author\" : \"dave\", " +
          "\"score\" : 80, \"views\" : 100 }",
          "{ \"_id\" : \"512bc962e835e68f199c8687\", \"author\" : \"dave\", " +
                  "\"score\" : 85, \"views\" : 521 }"};

  private static final Map<String, Articles> matchedArticlesMap = new HashMap<>();

  @Autowired
  private MatchRepository matchRepository;

  @BeforeClass
  @SuppressWarnings("Duplicates")
  public void setup() {
    matchRepository.deleteAll();
    List<Articles> articles = getArticles();
    matchRepository.insert(articles);
    ObjectMapper objectMapper = new ObjectMapper();
        /*matchedArticlesMap = Arrays.stream(MATCHED_DOCS).map(s -> {
          try {
            return objectMapper.readValue(s, Articles.class);
          }
          catch (IOException e) {
            throw new RuntimeException(e);
          }
        }).collect(a -> Collectors.toMap(a.getId, a));*/

    for (int i = 0; i < MATCHED_DOCS.length; i++) {
      try {
        Articles article = objectMapper.readValue(MATCHED_DOCS[i], Articles.class);
        matchedArticlesMap.put(article.getId(), article);
      } catch (IOException e) {
        assertTrue(false, e.getMessage());
      }
    }
  }

  private List<Articles> getArticles() {
    ObjectMapper mapper = new ObjectMapper();
//        List<Articles> articles = new ArrayList<>();
    return Arrays.stream(ARTICLES_DOCS).map(s -> {
      try {
        return mapper.readValue(s, Articles.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());
  }


  @Test
  public void getMatchedDocumentsFromRepository() {
    assertNotNull(matchRepository, "Must have a repository");
    List<Articles> articles = matchRepository.findAll();
    assertNotNull(articles);
    assertEquals(articles.size(), ARTICLES_DOCS.length);
    List<Articles> matchedArticles = matchRepository.getArticlesAfterMatch("dave");
    assertNotNull(matchedArticles);
    assertNotNull(matchedArticlesMap);
    assertEquals(matchedArticlesMap.size(), matchedArticles.size());
    for (Articles a : matchedArticles) {
      String id = a.getId();
      assertTrue(a.equals(matchedArticlesMap.get(id)));
    }
  }
}

