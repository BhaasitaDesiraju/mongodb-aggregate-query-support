/*
 *  Copyright (c) 2016 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *
 */

package com.cisco.mongodb.aggregate.support.factory;


import com.cisco.mongodb.aggregate.support.annotation.Aggregate;
import com.cisco.mongodb.aggregate.support.annotation.v2.Aggregate2;
import com.cisco.mongodb.aggregate.support.query.AggregateMongoQuery;
import com.cisco.mongodb.aggregate.support.query.MongoQueryExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

import java.lang.reflect.Method;

/**
 * Created by rkolliva
 * 10/10/2015.
 */
public class AggregateQuerySupportingRepositoryFactory extends MongoRepositoryFactory {

  private final MongoOperations mongoOperations;

  private MongoQueryExecutor queryExecutor;

  /**
   * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
   *
   * @param mongoOperations must not be {@literal null}
   * @param queryExecutor - the query executor
   */
  public AggregateQuerySupportingRepositoryFactory(MongoOperations mongoOperations, MongoQueryExecutor queryExecutor) {
    super(mongoOperations);
    this.mongoOperations = mongoOperations;
    this.queryExecutor = queryExecutor;
  }

  @Override
  public QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key,
                                                    EvaluationContextProvider evaluationContextProvider) {

    QueryLookupStrategy parentQueryLookupStrategy = super.getQueryLookupStrategy(key, evaluationContextProvider);
    return new AggregateQueryLookupStrategy(parentQueryLookupStrategy);
  }

  private boolean isAggregateQueryAnnotated(Method method) {
    return (method.getAnnotation(Aggregate.class) != null) || (method.getAnnotation(Aggregate2.class) != null);
  }

  private class AggregateQueryLookupStrategy implements QueryLookupStrategy {

    private final QueryLookupStrategy parentQueryLookupStrategy;

    AggregateQueryLookupStrategy(QueryLookupStrategy parentQueryLookupStrategy) {
      this.parentQueryLookupStrategy = parentQueryLookupStrategy;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata repositoryMetadata,
                                        ProjectionFactory projectionFactory, NamedQueries namedQueries) {
      if (!isAggregateQueryAnnotated(method)) {
        return parentQueryLookupStrategy.resolveQuery(method, repositoryMetadata, projectionFactory, namedQueries);
      }
      else {
        return new AggregateMongoQuery(method, repositoryMetadata, mongoOperations, projectionFactory, queryExecutor);
      }
    }
  }
}