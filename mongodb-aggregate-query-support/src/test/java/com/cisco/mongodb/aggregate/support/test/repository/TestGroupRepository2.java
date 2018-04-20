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


import com.cisco.mongodb.aggregate.support.annotation.Aggregate;
import com.cisco.mongodb.aggregate.support.annotation.Group;
import com.cisco.mongodb.aggregate.support.annotation.Match;
import com.cisco.mongodb.aggregate.support.annotation.v2.Aggregate2;
import com.cisco.mongodb.aggregate.support.annotation.v2.Group2;
import com.cisco.mongodb.aggregate.support.annotation.v2.Match2;
import com.cisco.mongodb.aggregate.support.test.beans.TestGroupDataBean;
import com.cisco.mongodb.aggregate.support.test.beans.TestGroupResultsBean;

import java.util.List;

/**
 * Created by rkolliva on 4/1/2017.
 *
 */
public interface TestGroupRepository2 extends TestMongoRepository<TestGroupDataBean, String> {

  // use only $price for $sum because fongo doesn't support all expressions.
  @Aggregate2(inputType = TestGroupDataBean.class, outputBeanType = TestGroupResultsBean.class, genericType = true)
  @Group2(query = "{" +
                             "   \"_id\" : { \"month\": { \"$month\": \"$date\" }, \"day\": " +
                             "{ \"$dayOfMonth\": \"$date\" }, \"year\": { \"$year\": \"$date\" } }," +
                             "   \"totalPrice\": { \"$sum\": \"$price\" }," +
                             "   \"averageQuantity\": { \"$avg\": \"$quantity\" }," +
                             "   \"count\": { \"$sum\": 1 }" +
                             "}", order = 0)
  List<TestGroupResultsBean> salesBySalesDate();

  //Returns the total quantity for a given item.
  @Aggregate(inputType = TestGroupDataBean.class,resultKey = "totalQuantity", outputBeanType = Integer.class)
  @Match2(query = "{'item' : '?0'}", order = 0)
  @Group2(query = "{_id : null, totalQuantity : {$sum:'$quantity'}}", order = 1)
  Integer getTotalQuantityForOneItem(String itemName);
}
