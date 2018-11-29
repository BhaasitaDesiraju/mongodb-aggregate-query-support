package com.github.krr.mongodb.aggregate.support.beans;

import com.github.krr.mongodb.aggregate.support.nonreactive.annotations.MongoId;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by bhaasita on 10/29/2018
 */
public class Employee {

  @MongoId
  private String id;

  private String name;

  private String reportsTo;

  private List<Employee> reportingHierarchy;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReportsTo() {
    return reportsTo;
  }

  public void setReportsTo(String reportsTo) {
    this.reportsTo = reportsTo;
  }

  public List<Employee> getReportingHierarchy() {
    return reportingHierarchy;
  }

  public void setReportingHierarchy(List<Employee> reportingHierarchy) {
    this.reportingHierarchy = reportingHierarchy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return Objects.equal(id, employee.id) &&
            Objects.equal(name, employee.name) &&
            Objects.equal(reportsTo, employee.reportsTo) &&
            Objects.equal(reportingHierarchy, employee.reportingHierarchy);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, reportsTo, reportingHierarchy);
  }
}
