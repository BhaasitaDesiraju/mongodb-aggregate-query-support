package com.github.krr.mongodb.aggregate.support.repository;

import com.github.krr.mongodb.aggregate.support.annotations.Aggregate;
import com.github.krr.mongodb.aggregate.support.annotations.GraphLookup;
import com.github.krr.mongodb.aggregate.support.beans.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhaasita on 10/29/2018
 */
public interface GraphLookupRepository extends MongoRepository<Employee, String> {

  @Aggregate(inputType = Employee.class, outputBeanType = Employee.class)
  @GraphLookup(query = "{\n" +
          "         \"from\": 'employees',\n" +
          "         \"startWith\": \"$reportsTo\",\n" +
          "         \"connectFromField\": \"reportsTo\",\n" +
          "         \"connectToField\": \"name\",\n" +
          "         \"as\": \"reportingHierarchy\"\n" +
          "      }", order = 0)
  List<Employee> getReportingHierarchies();
}
