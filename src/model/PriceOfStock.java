package model;

import java.io.IOException;

/**
 * PriceOfStock interface that provides stock prices.
 */
public interface PriceOfStock {

  /**
   * Gets price of stock on given date.
   *
   * @param stockSymbol represents stock symbol.
   * @param date        represents the date.
   * @return price of the stock on given date
   */
  double getPrice(String stockSymbol, String date) throws IOException;
}
