package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

public class Person {
  // Instance fields
  private List<Policy> clientPolicies = new ArrayList<Policy>();
  private String name;
  private int age;
  private boolean isLoadedProfile;
  private boolean lifeInsured;
  private int totalPremium;

  // Constructors
  public Person(String name, int age) {
    this.name = titleCase(name);
    this.age = age;
  }

  public Person(String name) {
    this.name = titleCase(name);
  }

  // Accessors & Modifiers
  public void setName(String name) {
    this.name = titleCase(name);
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getAge() {
    return this.age;
  }

  public String getAgeString() {
    return String.valueOf(this.age);
  }

  public Boolean getProfileStatus() {
    return this.isLoadedProfile;
  }

  public void setProfileStatus(Boolean value) {
    this.isLoadedProfile = value;
  }

  public boolean isLifeInsured() {
    return this.lifeInsured;
  }

  public void lifeInsuredNow() {
    this.lifeInsured = true;
  }

  public int getNumPolicies() {
    return clientPolicies.size();
  }

  public Policy getPolicy(int index) {
    return this.clientPolicies.get(index);
  }

  public int getTotalPremium() {
    return this.totalPremium;
  }

  public void setTotalPremium(int totalPremium) {
    this.totalPremium = totalPremium;
  }

  public void addPolicy(Policy policy) {
    this.clientPolicies.add(policy);
  }

  public List<Policy> getList() {
    return clientPolicies;
  }

  // Instance methods
  public String titleCase(String name) {

    StringBuilder nameTitleCase = new StringBuilder();
    String newName;

    nameTitleCase.append(name.substring(0, 1).toUpperCase())
        .append(name.substring(1, name.length()).toLowerCase());
    newName = nameTitleCase.toString();

    return newName;
  }
}
