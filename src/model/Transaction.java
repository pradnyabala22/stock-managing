package model;

/**
 * Transaction class that represents transaction withing portfolio, storing
 * details about a stock transaction.
 */
public class Transaction {
  private final String stockSymbol;
  private final double shares;
  private final String date;
  private final TypeOfTransaction type;

  /**
   * Constructs Transaction object.
   *
   * @param stockSymbol represents stock symbol.
   * @param shares      represents shares in transaction.
   * @param date        represents date of transaction.
   * @param type        the type of transaction, whether it be buy or sell.
   */
  public Transaction(String stockSymbol, double shares, String date, TypeOfTransaction type) {
    this.stockSymbol = stockSymbol;
    this.shares = shares;
    this.date = date;
    this.type = type;
  }

  /**
   * Gets the stock symbol of transaction.
   *
   * @return the stock symbol.
   */
  public String getStockSymbol() {
    return stockSymbol;
  }

  /**
   * Gets shares within transaction.
   *
   * @return the shares.
   */
  public double getShares() {
    return shares;
  }

  /**
   * Gets date of transaction.
   *
   * @return date of transaction.
   */
  public String getDate() {
    return date;
  }

  /**
   * Gets type of transaction, whether it buy or sell.
   *
   * @return type of transaction.
   */
  public TypeOfTransaction getType() {
    return type;
  }
}

