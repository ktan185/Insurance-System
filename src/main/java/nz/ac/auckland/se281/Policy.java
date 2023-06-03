package nz.ac.auckland.se281;

public abstract class Policy {

  // Instance fields
  protected int sumInsured;
  protected int basePremium;
  protected int discountedPremium;

  public Policy(int sumInsured) {
    this.sumInsured = sumInsured;
  }

  public abstract void calculateBasePremium();

  public int getSumInsured() {
    return this.sumInsured;
  }

  public void setBasePremium(int basePremium) {
    this.basePremium = basePremium;
  }

  public int getBasePremium() {
    return this.basePremium;
  }

  public void setDiscountedPremium(int discountedPremium) {
    this.discountedPremium = discountedPremium;
  }

  public int getDiscountedPremium() {
    return this.discountedPremium;
  }

}
