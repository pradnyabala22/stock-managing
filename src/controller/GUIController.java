package controller;

import java.io.IOException;
import java.util.Map;

/**
 * GUIController interface that extends the original IController from
 * the text-based interface. This is where the new methods in GUIController is defined and will
 * later be implemented in the GUIControllerImpl class.
 */
public interface GUIController extends IController {

  /**
   * Sets the portfolio names in GUIView. Gets the names from the model and
   * updates view to display names in the dropdown.
   */

  void portfolioSet();

  /**
   * Gets the composition of a portfolio at a particular date.
   * @param portfolioName represents the name of the portfolio.
   * @param date represents the date for which composition is found.
   * @return a map that has the stock symbols and their shares.
   */

  Map<String, Double> portfolioComposition(String portfolioName, String date);

  /**
   * Gets value of portfolio at a particular date.
   * @param portfolioName represents the name of the portfolio.
   * @param date represents the date for which the value is found.
   * @return value of portfolio at that date.
   * @throws IOException input/output error if there is an error getting the data.
   */

  double portfolioValue(String portfolioName, String date) throws IOException;

}
