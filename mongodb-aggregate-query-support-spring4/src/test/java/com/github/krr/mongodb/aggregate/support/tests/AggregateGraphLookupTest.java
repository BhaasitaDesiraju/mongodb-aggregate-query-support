package com.github.krr.mongodb.aggregate.support.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.krr.mongodb.aggregate.support.beans.Employee;
import com.github.krr.mongodb.aggregate.support.config.AggregateTestConfiguration;
import com.github.krr.mongodb.aggregate.support.repository.GraphLookupRepository;
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
 * Created by bhaasita on 10/29/2018
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ContextConfiguration(classes = AggregateTestConfiguration.class)
public class AggregateGraphLookupTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private GraphLookupRepository graphLookupRepository;

  private static final String[] EMPLOYEE_DOCS = {"{\"_id\" : 1, \"name\" : \"Dev\" }",
          "{ \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : \"Dev\" }",
          "{ \"_id\" : 3, \"name\" : \"Ron\", \"reportsTo\" : \"Eliot\" }",
          "{ \"_id\" : 4, \"name\" : \"Andrew\", \"reportsTo\" : \"Eliot\" }",
          "{ \"_id\" : 5, \"name\" : \"Asya\", \"reportsTo\" : \"Ron\" }",
          "{ \"_id\" : 6, \"name\" : \"Dan\", \"reportsTo\" : \"Andrew\" }"};

  private static final String[] LOOKUP_DOCS = {"{\"_id\" : 1, \"name\" : \"Dev\", \"reportingHierarchy\" : [ ] }",
          "{ \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : \"Dev\", \"reportingHierarchy\" : [ { \"_id\" : 1, " +
                  "\"name\" : \"Dev\" }]}",
          "{ \"_id\" : 3, \"name\" : \"Ron\", \"reportsTo\" : \"Eliot\", \"reportingHierarchy\" : [ { \"_id\" : 1, " +
                  "\"name\" : \"Dev\" }, { \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : \"Dev\" }]}",
          "{ \"_id\" : 4, \"name\" : \"Andrew\", \"reportsTo\" : \"Eliot\", \"reportingHierarchy\" : " +
                  "[ { \"_id\" : 1, \"name\" : \"Dev\" }, { \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : " +
                  "\"Dev\" }]}",
          "{ \"_id\" : 5, \"name\" : \"Asya\", \"reportsTo\" : \"Ron\", \"reportingHierarchy\" : [ " +
                  "{ \"_id\" : 1, \"name\" : \"Dev\" }, { \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : " +
                  "\"Dev\" }, { \"_id\" : 3, \"name\" : \"Ron\", \"reportsTo\" : \"Eliot\" }]}",
          "{ \"_id\" : 6, \"name\" : \"Dan\", \"reportsTo\" : \"Andrew\", \"reportingHierarchy\" : [ " +
                  "{ \"_id\" : 1, \"name\" : \"Dev\" }, { \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : " +
                  "\"Dev\" }, { \"_id\" : 4, \"name\" : \"Andrew\", \"reportsTo\" : \"Eliot\" }]}"};

  private static final Map<String, Employee> employeeMap = new HashMap<>();

  @BeforeClass
  @SuppressWarnings("Duplicates")
  public void setup() throws Exception {
    graphLookupRepository.deleteAll();
    ObjectMapper mapper = new ObjectMapper();
    List<Employee> employees = Arrays.stream(EMPLOYEE_DOCS).map(s -> {
      try {
        return mapper.readValue(s, Employee.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());
    graphLookupRepository.insert(employees);

    ObjectMapper objectMapper = new ObjectMapper();
    for (int i = 0; i < LOOKUP_DOCS.length; i++) {
      try {
        Employee employee = objectMapper.readValue(LOOKUP_DOCS[i], Employee.class);
        employeeMap.put(employee.getId(), employee);
      } catch (IOException e) {
        assertTrue(false, e.getMessage());
      }
    }
  }

  @Test
  public void lookupReportingHierarchies() {
    assertNotNull(graphLookupRepository, "Must have a repository");
    List<Employee> employees = graphLookupRepository.findAll();
    assertNotNull(employees);
    assertEquals(employees.size(), EMPLOYEE_DOCS.length);
    List<Employee> reportingEmployees = graphLookupRepository.getReportingHierarchies();
    assertNotNull(reportingEmployees);
    assertNotNull(employeeMap);
    assertEquals(employeeMap.size(), reportingEmployees.size());
    for (Employee e : reportingEmployees) {
      String id = e.getId();
      assertTrue(e.equals(employeeMap.get(id)));
    }
  }
}
