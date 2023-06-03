package nz.ac.auckland.se281;

public class Home extends Policy {

  // Instance fields
  private String address;
  private boolean rental;

  // Constructor
  public Home(int sumInsured, String address, boolean rental) {
    super(sumInsured);
    this.address = address;
    this.rental = rental;
    this.basePremium = 0;
  }

  @Override
  public void calculateBasePremium() {

    int basePremium;

    // If the house is rented base premium is 2% of the sum insured.
    if (isRental()) {
      basePremium = getSumInsured() / 100 * 2;
    } else {
      basePremium = getSumInsured() / 100;
    }
    setBasePremium(basePremium);
  }

  public boolean isRental() {
    return this.rental;
  }

  public String getAddress() {
    return this.address;
  }

}
