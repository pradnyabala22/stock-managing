package model;

import java.io.IOException;

/**
 * StockPrice class that implements the PriceOfStock interface that gets
 * stock price on a given date.
 */
public class StockPrice implements PriceOfStock {
  private final BetterIModel model;

  /**
   * Constructs a StockPrice object.
   *
   * @param model represents the model of the program.
   */
  public StockPrice(BetterIModel model) {
    this.model = model;
  }

  @Override
  public double getPrice(String stockSymbol, String date) throws IOException {
    return model.getStockPrice(stockSymbol, date);
  }
}

