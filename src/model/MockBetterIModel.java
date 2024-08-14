package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the mock we made of the BetterIModel, and it implements the original BetterIModel
 * interface. This is made in order to test the GUIControllerImpl. This serves as a copy of or a
 * replacement of the normal BetterIModel but is made for the purpose of testing so the user does
 * not have direct access parts of the program that they should not have access to.
 */

public class MockBetterIModel implements BetterIModel {
  private final List<String> executeMethod = new ArrayList<>();
  private final List<Portfolio> portfolios = new ArrayList<>();

  @Override
  public void buyStock(String portfolioName, String stockSymbol, int shares, String date) {
    executeMethod.add("buyStock: " + portfolioName + ", " + stockSymbol + ", " + shares + "," +
            " " + date);
  }

  @Override
  public void sellStock(String portfolioName, String stockSymbol, int shares, String date) {
    executeMethod.add("sellStock: " + portfolioName + ", " + stockSymbol + ", " + shares + "," +
            " " + date);
  }

  @Override
  public Map<String, Double> getPortfolioComposition(String portfolioName, String date) {
    executeMethod.add("getPortfolioComposition: " + portfolioName + ", " + date);
    for (Portfolio portfolio : portfolios) {
      if (portfolio.getName().equals(portfolioName)) {
        return portfolio.getComposition(date, portfolios);
      }
    }
    return new HashMap<>();
  }

  @Override
  public double getPortfolioValue(String portfolioName, String date, PriceOfStock stockPrice,
                                  List<Portfolio> listOfPortfolios) throws IOException {
    executeMethod.add("getPortfolioValue: " + portfolioName + ", " + date);
    for (Portfolio portfolio : portfolios) {
      if (portfolio.getName().equals(portfolioName)) {
        return portfolio.getValue(date, stockPrice, portfolios);
      }
    }
    return 0.0;
  }

  @Override
  public double getPortfolioValue() {
    return 0;
  }

  @Override
  public Map<String, Double> getPortfolioValueDistribution(String portfolioName, String date,
                                                           PriceOfStock stockPrice,
                                                           List<Portfolio> listOfPortfolios)
                                                           throws IOException {
    executeMethod.add("getPortfolioValueDistribution: " + portfolioName + ", " + date);
    for (Portfolio portfolio : portfolios) {
      if (portfolio.getName().equals(portfolioName)) {
        return portfolio.getValueDistribution(date, stockPrice, portfolios);
      }
    }
    return new HashMap<>();
  }

  @Override
  public double getStockPrice(String tickerSymbol, String date) {
    executeMethod.add("getStockPrice: " + tickerSymbol + ", " + date);
    return 200.0;
  }

  @Override
  public void savePortfolio(String portfolioName) throws IOException {
    executeMethod.add("savePortfolio: " + portfolioName);
  }

  @Override
  public void loadPortfolio(String filePath) throws IOException {
    executeMethod.add("loadPortfolio: " + filePath);
  }

  @Override
  public String rebalance(String portfolioName, String date, Map<String, Double> targetWeights,
                          PriceOfStock stockPrice) {
    executeMethod.add("rebalance: " + portfolioName + ", " + date);
    return "Rebalanced Successfully";
  }

  @Override
  public Map<String, Double> portfolioPerformance(String portfolioName,
                                                  String startingDate, String endingDate,
                                                  String timeInterval) {
    executeMethod.add("portfolioPerformance: " + portfolioName + ", " +
            "" + startingDate + ", " + endingDate + ", " + timeInterval);
    Map<String, Double> performance = new HashMap<>();
    performance.put("2023-05-04", 2000.0);
    performance.put("2023-05-05", 2500.0);
    return performance;
  }

  @Override
  public List<Portfolio> getPortfolios() {
    return portfolios;
  }

  @Override
  public void addStock(IStock stock) {
    executeMethod.add("addStock: " + stock);
  }

  @Override
  public ArrayList<Portfolio> createPortfolio(String name, String tickerSymbol, String shares) {
    executeMethod.add("createPortfolio: " + name + ", " + tickerSymbol + ", " + shares);
    HashMap<String, Double> stocks = new HashMap<>();
    stocks.put(tickerSymbol, Double.parseDouble(shares));
    Portfolio portfolio = new Portfolio(name, stocks);
    portfolios.add(portfolio);
    return new ArrayList<>(portfolios);
  }

  @Override
  public void portfolioValue(String portfolioName, String date) {
    executeMethod.add("portfolioValue: " + portfolioName + ", " + date);
  }

  @Override
  public List<String> getPortfolioNames() {
    List<String> names = new ArrayList<>();
    for (Portfolio p : portfolios) {
      names.add(p.getName());
    }
    return names;
  }

  @Override
  public double getGainOrLoss(String tickerSymbol, String startDate, String endDate) {
    executeMethod.add("getGainOrLoss: " + tickerSymbol + ", " + startDate + ", " + endDate);
    return 100.0;
  }

  @Override
  public double getMovingAverage(String tickerSymbol, String date, int xDays) {
    executeMethod.add("getMovingAverage: " + tickerSymbol + ", " + date + ", " + xDays);
    return 200.0;
  }

  @Override
  public List<String> getCrossOver(String tickerSymbol, String startDate, String endDate,
                                   int xDays) {
    executeMethod.add("getCrossOver: " + tickerSymbol + ", " + startDate + ", " + endDate + "," +
            " " + xDays);
    return List.of("2023-05-04", "2023-05-05");
  }

  @Override
  public void loadData(String tickerSymbol, String destinationFolder, String apiKey)
          throws IOException {
    executeMethod.add("loadData: " + tickerSymbol + ", " + destinationFolder + ", " + apiKey);
  }

  @Override
  public boolean doesTickerExist(String ticker) {
    executeMethod.add("doesTickerExist: " + ticker);
    return true;
  }

  @Override
  public boolean hasDate(String tickerSymbol, String date) {
    executeMethod.add("hasDate: " + tickerSymbol + ", " + date);
    return true;
  }

  @Override
  public void loadFromFile(String filePath) throws IOException {
    executeMethod.add("loadFromFile: " + filePath);
  }

  public List<String> getExecuteMethod() {
    return executeMethod;
  }
}
