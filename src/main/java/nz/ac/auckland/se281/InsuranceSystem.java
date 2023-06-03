package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Main.PolicyType;
import java.util.ArrayList;
import java.util.List;

public class InsuranceSystem {

  // Instance field
  private List<Person> clientList = new ArrayList<Person>();
  private int loadedProfileIndex;
  private Person loadedProfile;
  private boolean profileIsLoaded;

  public InsuranceSystem() {
    // Only this constructor can be used (if you need to initialise fields).
  }

  public void printDatabase() {

    // Initialise variables
    List<Policy> policyList;
    String name;
    String age;
    String numPolicies;
    String index;
    String numProfiles = String.valueOf(clientList.size());
    String totalPremium;
    Person client;
    String prefix;
    String suffix;
    String profileSuffix = "s:";
    String profileEnding = "";

    // Determine number of profiles in the system.
    if (clientList.isEmpty()) {
      profileSuffix = "s";
      profileEnding = ".";
    } else if (clientList.size() == 1) {
      profileSuffix = ":";
    }
    MessageCli.PRINT_DB_POLICY_COUNT.printMessage(numProfiles, profileSuffix, profileEnding);

    for (int i = 0; i < clientList.size(); i++) {

      // Initialise loop variables
      client = clientList.get(i);
      name = client.getName();
      age = client.getAgeString();
      index = String.valueOf(i + 1);
      totalPremium = String.valueOf(client.getTotalPremium());
      numPolicies = String.valueOf(client.getNumPolicies());
      policyList = client.getList();
      prefix = "";
      suffix = "y";
      if (client.getProfileStatus()) {
        prefix = "*** ";
      }
      if (client.getNumPolicies() != 1) {
        suffix = "ies";
      }
      MessageCli.PRINT_DB_PROFILE_HEADER_LONG.printMessage(prefix, index,
          name, age, numPolicies, suffix, totalPremium);

      // If profile is loaded print all policies for the profile.
      for (Policy policy : policyList) {

        // Initialise variables...
        String sumInsured = String.valueOf(policy.getSumInsured());
        String basePremium = String.valueOf(policy.getBasePremium());
        int discount = policy.getDiscountedPremium();

        if (discount == 0) {
          discount = policy.getBasePremium();
        }
        String discountedPremium = String.valueOf(discount);

        // Determine type of policy...
        if (policy instanceof Home) {
          Home policyHome = (Home) policy;
          String address = policyHome.getAddress();
          MessageCli.PRINT_DB_HOME_POLICY.printMessage(address, sumInsured,
              basePremium, discountedPremium);

        } else if (policy instanceof Car) {
          Car policyCar = (Car) policy;
          MessageCli.PRINT_DB_CAR_POLICY.printMessage(policyCar.getMakeandModel(),
              sumInsured, basePremium, discountedPremium);

        } else {
          MessageCli.PRINT_DB_LIFE_POLICY.printMessage(sumInsured,
              basePremium, discountedPremium);
        }
      }
    }
  }

  public void createNewProfile(String userName, String age) {

    // Create a new client
    Person client = new Person(userName);
    String newName = client.getName();
    int ageConverted = 0;

    // Determine that the person does not already exist.
    for (int i = 0; i < clientList.size(); i++) {
      if (newName.equals(clientList.get(i).getName())) {
        MessageCli.INVALID_USERNAME_NOT_UNIQUE.printMessage(newName);
        return;
      }
    }

    // If there is already a profile loaded, give an error message.
    if (profileIsLoaded) {
      MessageCli.CANNOT_CREATE_WHILE_LOADED.printMessage(loadedProfile.getName());
      return;
    }
    // determine if age only contains integers.
    if (!age.matches("[0-9]+")) {
      MessageCli.INVALID_AGE.printMessage(age, newName);
      return;
    } else {
      ageConverted = Integer.valueOf(age); // convert from String to Integer.
    }
    client.setAge(ageConverted);
    if (newName.length() < 3) { // Ensure that username is more than 2 characters!
      MessageCli.INVALID_USERNAME_TOO_SHORT.printMessage(newName);
      return;
    } else if (ageConverted < 1) { // Ensure age is a positve whole number.
      MessageCli.INVALID_AGE.printMessage(age, newName);
      return;
    } else { // Create a new profile & add to database.
      MessageCli.PROFILE_CREATED.printMessage(newName, age);
      clientList.add(client);
      return;
    }
  }

  public void loadProfile(String userName) {

    String nameTitlecased = titleCase(userName);

    // Determine if profile exists
    for (int i = 0; i < clientList.size(); i++) {
      Person client = clientList.get(i);
      if (nameTitlecased.equals(client.getName())) {
        // Unload any previously loaded client
        Person clientCurrentlyloaded = clientList.get(loadedProfileIndex);
        if (clientCurrentlyloaded.getProfileStatus()) {
          clientCurrentlyloaded.setProfileStatus(false);
        }
        // Load the profile specified by the user.
        MessageCli.PROFILE_LOADED.printMessage(nameTitlecased);
        this.loadedProfileIndex = i;
        this.profileIsLoaded = true;
        client.setProfileStatus(true);
        loadedProfile = client;
        return;
      }
    }
    MessageCli.NO_PROFILE_FOUND_TO_LOAD.printMessage(nameTitlecased);
    return;
  }

  public void unloadProfile() {

    if (!profileIsLoaded) {
      MessageCli.NO_PROFILE_LOADED.printMessage();
      return;
    } else { // If there is a profile loaded, then unload the profile.
      profileIsLoaded = false;
      loadedProfile.setProfileStatus(false);
      MessageCli.PROFILE_UNLOADED.printMessage(loadedProfile.getName());
    }
  }

  public void deleteProfile(String userName) {

    String nameTitlecased = titleCase(userName);
    String clientName = clientList.get(loadedProfileIndex).getName();

    if (profileIsLoaded && clientName.equals(nameTitlecased)) {
      // Display error message if trying to delete a profile currently loaded.
      MessageCli.CANNOT_DELETE_PROFILE_WHILE_LOADED.printMessage(nameTitlecased);
      return;
    }
    for (int i = 0; i < clientList.size(); i++) {
      if (nameTitlecased.equals(clientList.get(i).getName())) {
        // Delete profile requested.
        clientList.remove(i);
        loadedProfileIndex--;
        MessageCli.PROFILE_DELETED.printMessage(nameTitlecased);
        return;
      }
    }
    // If there is no profile found then display error message.
    MessageCli.NO_PROFILE_FOUND_TO_DELETE.printMessage(nameTitlecased);
    return;
  }

  public void createPolicy(PolicyType type, String[] options) {

    // initialise variables
    Policy policy;
    Person client = clientList.get(loadedProfileIndex);
    String clientName = client.getName();
    int clientAge = client.getAge();
    int sumInsured = Integer.parseInt(options[0]);
    boolean rental = false;
    boolean mechanicalBreakdown = false;

    if (!profileIsLoaded) {
      MessageCli.NO_PROFILE_FOUND_TO_CREATE_POLICY.printMessage();
      return;
    }
    // Determine the type of policy
    switch (type) {

      case HOME:
        if (options[2].matches("yes|YES|Y|y")) {
          rental = true;
        }
        policy = new Home(sumInsured, options[1], rental);
        createNewPolicy(client, policy);
        MessageCli.NEW_POLICY_CREATED.printMessage("home", clientName);
        break;

      case CAR:
        if (options[3].matches("yes|YES|Y|y")) {
          mechanicalBreakdown = true;
        }
        policy = new Car(sumInsured, options[1], options[2], mechanicalBreakdown, clientAge);
        createNewPolicy(client, policy);
        MessageCli.NEW_POLICY_CREATED.printMessage("car", clientName);
        break;

      case LIFE:
        if (client.isLifeInsured()) {
          MessageCli.ALREADY_HAS_LIFE_POLICY.printMessage(clientName);
          break;
        } else if (client.getAge() > 100) {
          MessageCli.OVER_AGE_LIMIT_LIFE_POLICY.printMessage(clientName);
          break;
        } else {
          policy = new Life(sumInsured, clientName, clientAge);
          createNewPolicy(client, policy);
          client.lifeInsuredNow();
          MessageCli.NEW_POLICY_CREATED.printMessage("life", clientName);
          break;
        }
    }

  }

  public String titleCase(String name) {

    // Change the first letter to upperdcase...
    StringBuilder nameTitleCase = new StringBuilder();
    nameTitleCase.append(name.substring(0, 1).toUpperCase())
        .append(name.substring(1, name.length()).toLowerCase());
    // Change the rest of the string to lower case.
    String newName = nameTitleCase.toString();
    return newName;
  }

  public void policyDiscount(Person client, Policy policy) {

    // Initialise variable
    double discountedPremium = 0;
    double base = (double) policy.getBasePremium();

    // Determine number of policies the client has and apply discounts accordingly.
    if (client.getNumPolicies() == 2) {
      discountedPremium = (base * 0.90);
    } else if (client.getNumPolicies() >= 3) {
      discountedPremium = (base * 0.80);
    }
    policy.setDiscountedPremium((int) discountedPremium);
  }

  public void calculateTotalPremium(Person client) {

    // Initialise variable
    int totalPremium = 0;

    // Loop through clients policies add sum up the total amount insured.

    for (int i = 0; i < client.getNumPolicies(); i++) {

      if (client.getPolicy(i).getDiscountedPremium() == 0) {
        totalPremium += client.getPolicy(i).getBasePremium();
      } else { // Where possible, take the discounted value.
        totalPremium += client.getPolicy(i).getDiscountedPremium();

      }
    }
    client.setTotalPremium(totalPremium);
  }

  public void applyDiscount(Person client) {

    List<Policy> clientPolicies = client.getList();

    for (Policy policy : clientPolicies) {
      // Calculate the discounts for each policy.
      policyDiscount(client, policy);
      // Determine the total price the client has to pay.
      calculateTotalPremium(client);
    }
  }

  public void createNewPolicy(Person client, Policy policy) {
    policy.calculateBasePremium();
    client.addPolicy(policy);
    applyDiscount(client);
  }

}
