package model;


import java.util.Map;

/**
 * MockStockPrice class for StockPrice for testing purposes.
 */
public class MockStockPrice implements PriceOfStock {
  private final Map<String, Double> prices = Map.of(
          "GOOG", 100.0,
          "AAPL", 150.0,
          "WMT", 200.0
  );

  @Override
  public double getPrice(String tickerSymbol, String date) {
    return prices.getOrDefault(tickerSymbol, 0.0);
  }
}
