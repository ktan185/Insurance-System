package nz.ac.auckland.se281;

public class Life extends Policy {

  // Instance field
  String clientName;
  int clientAge;

  // Constructor
  public Life(int sumInsured, String clientName, int clientAge) {
    super(sumInsured);
    this.clientName = clientName;
    this.clientAge = clientAge;
    this.basePremium = 0;
  }

  public void calculateBasePremium() {
    setBasePremium((int) ((1 + ((double) getClientAge() / 100)) / 100 * getSumInsured()));
  }

  public int getClientAge() {
    return this.clientAge;
  }
}
