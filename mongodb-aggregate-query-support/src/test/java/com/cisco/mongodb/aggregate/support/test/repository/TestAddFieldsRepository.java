/*
 *  Copyright (c) 2017 the original author or authors.
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

package com.cisco.mongodb.aggregate.support.test.repository;

import com.cisco.mongodb.aggregate.support.annotation.AddFields;
import com.cisco.mongodb.aggregate.support.annotation.Aggregate;
import com.cisco.mongodb.aggregate.support.annotation.v2.AddFields2;
import com.cisco.mongodb.aggregate.support.annotation.v2.Aggregate2;
import com.cisco.mongodb.aggregate.support.test.beans.TestScoreBean;
import com.cisco.mongodb.aggregate.support.test.beans.ScoreResultsBean;

import java.util.List;

/**
 * Created by rkolliva
 * 1/24/17.
 */


public interface TestAddFieldsRepository extends TestMongoRepository<TestScoreBean, String> {

  @Aggregate(inputType = TestScoreBean.class, outputBeanType = ScoreResultsBean.class,
             addFields = {
                 @AddFields(query = "{\n" +
                                    "       totalHomework: { $sum: \"$homework\" } ,\n" +
                                    "       totalQuiz: { $sum: \"$quiz\" }\n" +
                                    "     }", order = 0),
                 @AddFields(query = "{ totalScore:\n" +
                                    "       { $add: [ \"$totalHomework\", \"$totalQuiz\", \"$extraCredit\" ] } }",
                            order = 1)
             })
  List<ScoreResultsBean> addFieldsToScore();

  @Aggregate2(inputType = TestScoreBean.class, outputBeanType = ScoreResultsBean.class)
  @AddFields2(query = "{\n" +
                      "       totalHomework: { $sum: \"$homework\" } ,\n" +
                      "       totalQuiz: { $sum: \"$quiz\" }\n" +
                      "     }", order = 0)
  @AddFields2(query = "{ totalScore:\n" +
                    "       { $add: [ \"$totalHomework\", \"$totalQuiz\", \"$extraCredit\" ] } }",
            order = 1)
  List<ScoreResultsBean> addFieldsToScore2();
}
