package model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IModel interface represents the model of the program, where all implementation of the
 * methods used throughout the program is done. This handles calculations of the program
 * and manages all the stocks and portfolios inputted and/or kept.
 */

public interface IModel {

  /**
   * Adds a stock to model.
   *
   * @param stock stock that is added.
   */

  void addStock(IStock stock);

  /**
   * Creates a new portfolio.
   *
   * @param name         represents the name of the portfolio.
   * @param tickerSymbol represents the tickersymbol of the stock.
   * @param shares       represents the number of shares/quantity.
   * @return an Arraylist of Portfolios.
   */

  ArrayList<Portfolio> createPortfolio(String name, String tickerSymbol, String shares);

  /**
   * Calculate the value of a portfolio at a given date.
   *
   * @param portfolioName represents the name of the portfolio.
   * @param date          represents the date for which it is being calculated.
   */

  void portfolioValue(String portfolioName, String date);

  /**
   * Gets the value of the portfolio.
   *
   * @return a double that represents the portfolio value.
   */

  double getPortfolioValue();

  /**
   * Gets the portfolio names.
   *
   * @return a list of portfolio names.
   */

  List<String> getPortfolioNames();

  /**
   * Calculates gain or loss of a stock within a timeframe.
   *
   * @param tickerSymbol represents the ticker symbol.
   * @param startDate    represents the starting date for calculation.
   * @param endDate      represents the ending date for calculation.
   * @return a double that represents the gain or loss.
   */

  double getGainOrLoss(String tickerSymbol, String startDate, String endDate);

  /**
   * Calculates moving average of a stock over a period of days up to given date.
   *
   * @param tickerSymbol represents the ticker symbol.
   * @param date         represents the ending date for calculation.
   * @param xDays        represents the number of days over which to calculate.
   * @return a double that represents the moving average.
   */

  double getMovingAverage(String tickerSymbol, String date, int xDays);

  /**
   * Establishes crossovers of a stocks moving averages within a timeframe.
   *
   * @param tickerSymbol represents the ticker symbol.
   * @param startDate    represents the starting date for calculation.
   * @param endDate      represents the ending date for calculation.
   * @param xDays        represents the number of days over which to calculate.
   * @return a double that represents the crossover.
   */

  List<String> getCrossOver(String tickerSymbol, String startDate, String endDate, int xDays);

  /**
   * Loads the stock data for program.
   *
   * @param tickerSymbol      represents the ticker symbol.
   * @param destinationFolder represents the folder where the data will be stored.
   * @param apiKey            represents the API key for accessing data source.
   * @throws IOException if an Input/Output error occurs.
   */

  void loadData(String tickerSymbol, String destinationFolder, String apiKey) throws IOException;

  /**
   * Checks if ticker symbol exists.
   *
   * @param ticker represents the ticker symbol to check.
   * @return a boolean(true/false) that represents if the ticker symbol exists or not.
   */

  boolean doesTickerExist(String ticker);

  /**
   * Checks if a given date exists for a particular tickerSymbol.
   *
   * @param tickerSymbol represents the tickerSymbol.
   * @param date         represents the date to check.
   * @return a boolean(true/false) that represents if a date exists for a tickerSymbol.
   */

  boolean hasDate(String tickerSymbol, String date);

  /**
   * Loads CSV file.
   *
   * @param filePath represents the file.
   * @throws IOException if unable to process.
   */
  void loadFromFile(String filePath) throws IOException;

}






