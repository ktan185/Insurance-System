package nz.ac.auckland.se281;

public class Car extends Policy {

  // Instance fields
  private int clientAge;
  private String makeAndModel;
  private String plateNum;
  private boolean mechanicalBreakdown;

  // Constructor
  public Car(int sumInsured, String model, String plateNum, boolean breakdown, int age) {
    super(sumInsured);
    this.makeAndModel = model;
    this.plateNum = plateNum;
    this.mechanicalBreakdown = breakdown;
    this.clientAge = age;
    this.basePremium = 0;
  }

  @Override
  public void calculateBasePremium() {

    // Calculate base premium based on clients age.
    if (clientAge < 25) {
      setBasePremium((int) (((double) getSumInsured() / 100 * 15)));
    } else {
      setBasePremium((int) (((double) getSumInsured() / 100 * 10)));
    }
    // Add additional $80 if there is mechanical breakdown.
    if (mechanicalBreakdown) {
      setBasePremium(getBasePremium() + 80);
    }
  }

  public String getMakeandModel() {
    return makeAndModel;
  }

  public String getCarPlate() {
    return this.plateNum;
  }

}
