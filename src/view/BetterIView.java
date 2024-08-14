package view;

/**
 * BetterIView extends the original IView interfaces and adds this helper method onto the other
 * methods that already exist in there.
 */
public interface BetterIView extends IView {

  /**
   * Gets the date from the user, in terms of year, month, and day separately to avoid
   * user input error as much as possible.
   *
   * @return return the date in the same, yyyy-MM-dd format.
   */
  String getDateInput();
}
