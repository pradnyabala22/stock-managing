package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MockModel class that implements BetterIModel and is a mock implementation for testing.
 */

public class MockModel implements BetterIModel {
  private final StringBuilder log;

  /**
   * Constructors a MockModel.
   *
   * @param log a StringBuilder that logs method calls and parameters.
   */

  public MockModel(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
    List<HashMap<String, Integer>> portfolios = new ArrayList<>();
  }

  private void log(String message) {
    log.append(message).append(System.lineSeparator());
  }

  @Override
  public void addStock(IStock stock) {
    log("Add stock ");
  }

  @Override
  public ArrayList<Portfolio> createPortfolio(String name, String tickerSymbol, String share) {
    log("Create portfolio " + name + " with ticker symbol " + tickerSymbol + " and share " + share);
    return new ArrayList<>();
  }

  @Override
  public void portfolioValue(String name, String date) {
    log("Portfolio " + name + " value on " + date);
  }

  @Override
  public double getPortfolioValue() {
    return 0;
  }

  @Override
  public double getPortfolioValue(String portfolioName, String date, PriceOfStock stockPrice,
                                  List<Portfolio> listOfPortfolios) {
    log("Get value of the portfolio for " + portfolioName + " on " + date);
    return 0;
  }

  @Override
  public double getGainOrLoss(String tickerSymbol, String startDate, String endDate) {
    log("Gain or loss calc " + tickerSymbol + " from " + startDate + " to " + endDate);
    return 0;
  }

  @Override
  public double getMovingAverage(String tickerSymbol, String date, int xDays) {
    log("Moving average calc " + tickerSymbol + " on " + date + " with " + xDays);
    return 0;
  }

  @Override
  public List<String> getCrossOver(String tickerSymbol, String startDate, String endDate,
                                   int xDays) {
    log("Crossover " + tickerSymbol + " from " + startDate + " to " + endDate + " with "
            + xDays + " days");
    return List.of();
  }

  @Override
  public void loadData(String tickerSymbol, String destinationFolder, String apiKey) {
    log("Load data for " + tickerSymbol + " into " + destinationFolder + " using " + apiKey);
  }

  @Override
  public List<String> getPortfolioNames() {
    log("Get the name of the portfolio:");
    return List.of("Port1");
  }

  @Override
  public boolean doesTickerExist(String ticker) {
    log("Check if ticker exists: " + ticker);
    return true;
  }

  @Override
  public boolean hasDate(String ticker, String date) {
    log("Check if it has date " + ticker + " and " + date);
    return true;
  }

  @Override
  public void loadFromFile(String filePath) throws IOException {
    log("Load data from the file " + filePath);
  }

  @Override
  public void buyStock(String portfolioName, String stockSymbol, int shares, String date) {
    log("Buy stock in the portfolio " + portfolioName + " of " + stockSymbol + " shares "
            + shares + " on " + date);
  }

  @Override
  public void sellStock(String portfolioName, String stockSymbol, int shares, String date) {
    log("Sell stock from the portfolio " + portfolioName + " of " + stockSymbol + " shares "
            + shares + " on " + date);
  }

  @Override
  public Map<String, Double> getPortfolioComposition(String portfolioName, String date) {
    log("Get the composition of the portfolio for " + portfolioName + " on " + date);
    return new HashMap<>();
  }


  @Override
  public Map<String, Double> getPortfolioValueDistribution(String portfolioName, String date,
                                                           PriceOfStock stockPrice,
                                                           List<Portfolio> listOfPortfolios) {
    log("Get portfolio value distribution for " + portfolioName + " on " + date);
    return new HashMap<>();
  }

  @Override
  public double getStockPrice(String tickerSymbol, String date) {
    log("Get price of stock: " + tickerSymbol + " on " + date);
    return 0;
  }

  @Override
  public void savePortfolio(String portfolioName) throws IOException {
    log("Save portfolio: " + portfolioName);
  }

  @Override
  public void loadPortfolio(String filePath) throws IOException {
    log("Load portfolio from file: " + filePath);
  }

  @Override
  public String rebalance(String portfolioName, String date, Map<String, Double> targetWeights,
                          PriceOfStock stockPrice) {
    log("Rebalance portfolio " + portfolioName + " on " + date + " with weights " + targetWeights);
    return "";
  }

  @Override
  public Map<String, Double> portfolioPerformance(String portfolioName,
                                                  String startingDate, String endingDate,
                                                  String timeInterval) {
    log("Get the portfolio performance for " + portfolioName + " from " + startingDate + " to "
            + endingDate + " with interval " + timeInterval);
    return new HashMap<>();
  }

  @Override
  public List<Portfolio> getPortfolios() {
    log("Get portfolios");
    return new ArrayList<>();
  }

}
