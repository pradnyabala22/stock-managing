package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Portfolio class that represents a Portfolio to create them and find values of them.
 */

public class Portfolio {
  private final String name;
  private final HashMap<String, Double> stocks;

  private final List<Transaction> transactions;

  /**
   * Creates Portfolio object.
   *
   * @param name   represents the name of the portfolio.
   * @param stocks represents the stocks in the portfolio.
   */
  public Portfolio(String name, HashMap<String, Double> stocks) {
    this.name = name;
    this.stocks = new HashMap<>(stocks);
    this.transactions = new ArrayList<>();

  }

  /**
   * Gets the name of the portfolio.
   *
   * @return a String that represents the name of the portfolio.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the stocks in a portfolio.
   *
   * @return a map that represents stocks.
   */
  public HashMap<String, Double> getStocks() {
    return stocks;
  }

  /**
   * Gets the transactions in a portfolio.
   *
   * @return a List that represents transactions.
   */
  public List<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Adds a transaction.
   *
   * @param transaction represents the transaction to be added.
   */
  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }

  /**
   * Buys shares of stock.
   *
   * @param stockSymbol represents stock symbol.
   * @param shares      represents number of shares.
   * @param date        represents date of buying.
   */
  public void buyStock(String stockSymbol, double shares, String date) {
    if (shares <= 0) {
      throw new IllegalArgumentException("Number of shares must be greater than zero.");
    }
    transactions.add(new Transaction(stockSymbol, shares, date, TypeOfTransaction.BUY));
  }

  /**
   * Sells shares of stock.
   *
   * @param stockSymbol represents stock symbol.
   * @param shares      represents number of shares.
   * @param date        represents date of selling.
   */
  public void sellStock(String stockSymbol, double shares, String date) {
    if (shares <= 0) {
      throw new IllegalArgumentException("Number of shares must be greater than zero.");
    }
    transactions.add(new Transaction(stockSymbol, shares, date, TypeOfTransaction.SELL));
  }

  /**
   * Gets composition of portfolio on given date.
   *
   * @param date             represents date to get the composition for.
   * @param listOfPortfolios represents the list of portfolios.
   * @return a map of stock symbols and shares.
   */
  public Map<String, Double> getComposition(String date, List<Portfolio> listOfPortfolios) {
    Map<String, Double> comp = new HashMap<>(stocks);

    for (Transaction ts : transactions) {
      if (ts.getDate().compareTo(date) <= 0) {
        String stockSymbol = ts.getStockSymbol();
        double currShares = ts.getShares();
        if (ts.getType() == TypeOfTransaction.BUY) {
          comp.put(stockSymbol, comp.getOrDefault(stockSymbol, 0.0) + currShares);
        } else if (ts.getType() == TypeOfTransaction.SELL) {
          double updateShares = comp.getOrDefault(stockSymbol, 0.0) - currShares;
          if (updateShares < 0) {
            throw new IllegalStateException("Composition of stock "
                    + stockSymbol + " cannot be negative.");
          }
          comp.put(stockSymbol, updateShares);
        }
      }
    }

    if (transactions.isEmpty() && listOfPortfolios != null) {
      for (Portfolio p : listOfPortfolios) {
        if (p.getName().equals(this.name)) {
          comp.putAll(p.getStocks());
        }
      }
    }
    return comp;
  }

  /**
   * Gets value of portfolio on given date.
   *
   * @param date             represents date to find value for.
   * @param priceProvider    represents what gets the price.
   * @param listOfPortfolios represents list of portfolios.
   * @return value of the portfolio on given date.
   */
  public double getValue(String date, PriceOfStock priceProvider,
                         List<Portfolio> listOfPortfolios) throws IOException {
    double num = 0.0;
    Map<String, Double> comp = getComposition(date, listOfPortfolios);
    for (Map.Entry<String, Double> composition : comp.entrySet()) {
      if (composition.getValue() > 0) {
        num += composition.getValue() * priceProvider.getPrice(composition.getKey(), date);
      }
    }
    return num;
  }

  /**
   * Gets value distribution of portfolio on given date.
   *
   * @param date             represents date to find value for.
   * @param stockPrice       represents what gets the price.
   * @param listOfPortfolios represents list of portfolios.
   * @return a map of stock symbols and values
   */
  public Map<String, Double> getValueDistribution(String date,
                                                  PriceOfStock stockPrice,
                                                  List<Portfolio> listOfPortfolios)
                                                  throws IOException {
    Map<String, Double> dist = new HashMap<>();
    Map<String, Double> composition = getComposition(date, listOfPortfolios);
    for (Map.Entry<String, Double> comp : composition.entrySet()) {
      if (comp.getValue() > 0) {
        dist.put(comp.getKey(), comp.getValue() * stockPrice.getPrice(comp.getKey(), date));
      }
    }
    return dist;
  }

}