package model;

import java.util.List;

/**
 * IStock interface that represents operations that can be done with stocks. It
 * defines methods for executing certain commands (gain or loss, moving average, crossover).
 */

public interface IStock {
  /**
   * Calculates the gain or loss of a stock within a timeframe.
   *
   * @param startDate represents the starting date for calculation.
   * @param endDate   represents the ending date for calculation.
   * @return a double that represents the gain or loss.
   */

  double gainOrLoss(String startDate, String endDate);

  /**
   * Calculates the moving average of a stock for a number of days up to given date.
   *
   * @param date  represents the ending date for the calculation.
   * @param xDays represents the number of days over which to calculate.
   * @return a double that represents the moving average.
   */

  double movingAverage(String date, int xDays);

  /**
   * Establishes crossovers of a stocks moving averages within a timeframe.
   *
   * @param startDate represents the starting date for calculation.
   * @param endDate   represents the ending date for calculation.
   * @param xDays     represents the number of days over which to calculate.
   * @return a double that represents the crossover.
   */

  List<String> crossOver(String startDate, String endDate, int xDays);

  /**
   * Gets the closing price of a stock at a given date.
   *
   * @param date represents the date to get closing price.
   * @return a double that represents the closing price.
   */

  double closingPrice(String date);

  /**
   * Gets the tickerSymbol of the stock.
   *
   * @return a String that represents the ticker symbol.
   */

  String ticker();

  /**
   * Checks if a given date exists.
   *
   * @param startDate represents the starting date to check.
   * @return a boolean(true/false) that represents if a date exists.
   */

  boolean hasDate(String startDate);
}


