package model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * BetterIModel interface extends IModel with additional functionalities for portfolios (buy stocks,
 * sel stocks, compositions, values, saving, loading, rebalancing, and assessing performance).
 */
public interface BetterIModel extends IModel {

  /**
   * Buys shares of stock for a particular portfolio on given date.
   *
   * @param portfolioName represents the name of the portfolio.
   * @param stockSymbol   represents the stock symbol.
   * @param shares        represents the number of shares to buy.
   * @param date          represents the date.
   */
  void buyStock(String portfolioName, String stockSymbol, int shares, String date);

  /**
   * Sells shares of a stock from a particular portfolio on given date.
   *
   * @param portfolioName represents the name of the portfolio.
   * @param stockSymbol   represents the stock symbol.
   * @param shares        represents the number of shares to buy.
   * @param date          represents the date.
   */
  void sellStock(String portfolioName, String stockSymbol, int shares, String date);

  /**
   * Gets composition of a particular portfolio on given date.
   *
   * @param portfolioName represents  name of the portfolio.
   * @param date          represents the date for which composition needs to be found.
   * @return a map of stock symbols and shares.
   */
  Map<String, Double> getPortfolioComposition(String portfolioName, String date);

  /**
   * Gets the value of a particular portfolio on a given date.
   *
   * @param portfolioName    represents name of the portfolio.
   * @param date             represents the date for which value needs to be found.
   * @param stockPrice       represents the stock price.
   * @param listOfPortfolios represents the list of portfolios.
   * @return the value of the portfolio on given date.
   */
  double getPortfolioValue(String portfolioName, String date,
                           PriceOfStock stockPrice, List<Portfolio> listOfPortfolios)
                           throws IOException;

  /**
   * Gets the value distribution of a particular portfolio on a given date.
   *
   * @param portfolioName    represents name of the portfolio.
   * @param date             represents the date for which value distribution needs to be found.
   * @param stockPrice       represents the stock price.
   * @param listOfPortfolios represents the list of portfolios.
   * @return a map of stock symbols and values
   */
  Map<String, Double> getPortfolioValueDistribution(String portfolioName,
                                                    String date, PriceOfStock stockPrice,
                                                    List<Portfolio> listOfPortfolios)
                                                    throws IOException;

  /**
   * Gets price of a stock on  given date.
   *
   * @param tickerSymbol represents stock symbol.
   * @param date         represents the date for which price needs to be found.
   * @return price of the stock.
   */
  double getStockPrice(String tickerSymbol, String date) throws IOException;

  /**
   * Saves portfolio to file.
   *
   * @param portfolioName represents name of portfolio.
   * @throws IOException if input/output error occurs.
   */
  void savePortfolio(String portfolioName) throws IOException;

  /**
   * Loads portfolio from  file.
   *
   * @param filePath represents path of the file.
   * @throws IOException if input/output error occurs.
   */
  void loadPortfolio(String filePath) throws IOException;

  /**
   * Rebalances portfolio to certain weights on  given date.
   *
   * @param portfolioName represents name of portfolio.
   * @param date          represents the date for which rebalance needs to be done.
   * @param targetWeights represents target weights for stocks for rebalance.
   * @param stockPrice    represents stock price.
   * @return a string saying how to rebalance.
   */
  String rebalance(String portfolioName, String date, Map<String, Double> targetWeights,
                   PriceOfStock stockPrice) throws IOException;

  /**
   * Gets performance of portfolio over a given date range and interval.
   *
   * @param portfolioName represents name of the portfolio.
   * @param startingDate  represents start date of performance timeframe.
   * @param endingDate    represents end date of the performance timeframe.
   * @param timeInterval  represents the interval (daily, monthly, yearly).
   * @return a map of dates and portfolio values
   */
  Map<String, Double> portfolioPerformance(String portfolioName, String startingDate,
                                           String endingDate, String timeInterval)
                                           throws IOException;

  /**
   * Gets list of portfolios.
   *
   * @return a list of all portfolios.
   */
  List<Portfolio> getPortfolios();
}
