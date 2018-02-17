package hw1;

/**
 * Class representing requested actions for a transaction.
 */
public class Actions {
  int act;

  private Actions(int permLevel) {
    this.act = act;
  }

  public String toString() {
    if (act == 0)
      return "FETCH";
    if (act == 1)
        return "INSERT";
    if (act == 2)
        return "DELETE";
    if (act == 3)
        return "COMPLETE";
    return "UNKNOWN";
  }

  public static final Actions FETCH = new Actions(0);
  public static final Actions INSERT = new Actions(1);
  public static final Actions DELETE = new Actions(2);
  public static final Actions COMPLETE = new Actions(3);
}
