import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import model.BetterIModel;
import model.BetterModelImpl;
import model.MockStockPrice;
import model.PriceOfStock;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This ensures that the new methods that we added in our better model impl works as it should. It
 * has the tests for the new model functionality, meaning it ensure that the buy, sell, value,
 * composition, distribution, save, load, rebalance, and the bar chart all work as they should.
 */

public class BetterModelImplTest {
  private BetterIModel model;
  private PriceOfStock mockStockPrice;


  @Before
  public void setup() throws IOException {
    model = new BetterModelImpl();
    mockStockPrice = new MockStockPrice();
  }


  @Test
  public void testBuyingStocksOne() {
    model.buyStock("Darshini", "GOOG", 23, "2024-05-15");
    Map<String, Double> portfolioComp = model.getPortfolioComposition(
            "Darshini", "2024-05-15");
    assertEquals(1, portfolioComp.size());
    assertEquals(Double.valueOf(23), portfolioComp.get("GOOG"));
  }

  @Test
  public void testBuyingMultipleStocksSameDateSameTicker() {
    model.buyStock("Port1", "GOOG", 20, "2024-05-15");
    model.buyStock("Port1", "GOOG", 15, "2024-05-15");
    Map<String, Double> portfolioComp = model.getPortfolioComposition("Port1",
            "2024-05-15");
    assertEquals(1, portfolioComp.size());
    assertEquals(Double.valueOf(35), portfolioComp.get("GOOG"));
  }

  @Test
  public void testBuyingMultipleStocksSameDateDiffTicker() {
    model.buyStock("Port1", "GOOG", 4, "2024-05-15");
    model.buyStock("Port1", "WMT", 8, "2024-05-15");
    Map<String, Double> portfolioComp = model.getPortfolioComposition("Port1",
            "2024-05-15");
    assertEquals(2, portfolioComp.size());
    assertEquals(Double.valueOf(4), portfolioComp.get("GOOG"));
    assertEquals(Double.valueOf(8), portfolioComp.get("WMT"));
  }


  @Test
  public void testBuyingMultipleStocksDiffDateSameTicker() {
    model.buyStock("Port1", "GOOG", 20, "2024-05-15");
    model.buyStock("Port1", "GOOG", 15, "2024-05-16");
    Map<String, Double> portfolioComp1 = model.getPortfolioComposition("Port1",
            "2024-05-15");
    Map<String, Double> portfolioComp2 = model.getPortfolioComposition("Port1",
            "2024-05-16");
    assertEquals(1, portfolioComp1.size());
    assertEquals(Double.valueOf(20), portfolioComp1.get("GOOG"));
    assertEquals(1, portfolioComp2.size());
    assertEquals(Double.valueOf(35), portfolioComp2.get("GOOG"));
  }

  @Test
  public void testBuyingMultipleStocksDiffDateDiffTicker() {
    model.buyStock("Port1", "GOOG", 4, "2024-05-15");
    model.buyStock("Port1", "WMT", 8, "2024-05-16");
    Map<String, Double> portfolioComposition1 = model.getPortfolioComposition("Port1",
            "2024-05-15");
    Map<String, Double> portfolioComposition2 = model.getPortfolioComposition("Port1",
            "2024-05-16");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(4), portfolioComposition1.get("GOOG"));
    assertEquals(2, portfolioComposition2.size());
    assertEquals(Double.valueOf(4), portfolioComposition2.get("GOOG"));
    assertEquals(Double.valueOf(8), portfolioComposition2.get("WMT"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksNothing() {
    model.buyStock("Port1", "GOOG",
            0, "2024-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksNegativeStock() {
    model.buyStock("Port1", "GOOG",
            -3, "2024-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidDateFormat() {
    model.buyStock("Port1", "GOOG",
            7, "2024/05/15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidTicker() {
    model.buyStock("Port1", "cre",
            7, "2024-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksWeekend() {
    model.buyStock("Port1", "GOOG",
            7, "2024-05-18");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksHolidayChristmas() {
    model.buyStock("Port1", "GOOG",
            7, "2024-12-25");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidYearFuture() {
    model.buyStock("Port1", "GOOG",
            7, "2026-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidMonth() {
    model.buyStock("Port1", "GOOG",
            7, "2024-14-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidDay() {
    model.buyStock("Port1", "GOOG",
            7, "2024-05-32");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidDateFuture() {
    model.buyStock("Port1",
            "GOOG", 7, "2027-02-03");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidPortfolio() {
    model.buyStock("Non-existent",
            "GOOG", 7, "2024-05-15");
  }

  @Test
  public void testBuyingStocksLeapYear() {
    model.buyStock("Port1",
            "GOOG", 7, "2024-02-29");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("Port1",
            "2024-02-29");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(7), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testBuyingStocksNewPortfolio() {
    model.buyStock("Pradhu",
            "GOOG", 7, "2024-05-15");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("Pradhu",
            "2024-05-15");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(7), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testBuyingStocksSamePortfolioSameTickerSameDate() {
    model.buyStock("Same", "GOOG",
            5, "2024-05-15");
    model.buyStock("Same", "GOOG",
            10, "2024-05-15");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("Same",
            "2024-05-15");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(15), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testBuyingStocksMultipleInNewPortfolioDiffDates() {
    model.buyStock("Darshu",
            "GOOG", 7, "2024-05-15");
    model.buyStock("Darshu",
            "GOOG", 4, "2024-05-16");
    Map<String, Double> portfolioComposition1 = model.getPortfolioComposition("Darshu",
            "2024-05-15");
    Map<String, Double> portfolioComposition2 = model.getPortfolioComposition("Darshu",
            "2024-05-16");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(7), portfolioComposition1.get("GOOG"));
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(11), portfolioComposition2.get("GOOG"));
  }

  @Test
  public void testBuyingStocksMultipleInNewPortfolioDiffTickerSameDate() {
    model.buyStock("Pradhu",
            "GOOG", 7, "2024-05-15");
    model.buyStock("Pradhu",
            "WMT", 4, "2024-05-15");
    Map<String, Double> portfolioComposition1 = model.getPortfolioComposition("Pradhu",
            "2024-05-15");
    assertEquals(2, portfolioComposition1.size());
    assertEquals(Double.valueOf(7), portfolioComposition1.get("GOOG"));
    assertEquals(Double.valueOf(4), portfolioComposition1.get("WMT"));
  }

  @Test
  public void testBuyingStocksMultipleInNewPortfolioSameTicker() {
    model.buyStock("Port2", "GOOG",
            5, "2024-05-15");
    model.buyStock("Port2", "GOOG",
            6, "2024-05-16");
    Map<String, Double> portfolioComposition1 = model.getPortfolioComposition("Port2",
            "2024-05-15");
    Map<String, Double> portfolioComposition2 = model.getPortfolioComposition("Port2",
            "2024-05-16");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(5), portfolioComposition1.get("GOOG"));
    assertEquals(1, portfolioComposition2.size());
    assertEquals(Double.valueOf(11), portfolioComposition2.get("GOOG"));
  }

  @Test
  public void testBuyingStocksMultipleInNewPortfolioDiffTickerDiffDate() {
    model.buyStock("Pradhu",
            "GOOG", 7, "2024-05-15");
    model.buyStock("Pradhu",
            "WMT", 4, "2024-05-16");
    Map<String, Double> portfolioComposition1 = model.getPortfolioComposition("Pradhu",
            "2024-05-15");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(7), portfolioComposition1.get("GOOG"));
    Map<String, Double> portfolioComposition2 = model.getPortfolioComposition("Pradhu",
            "2024-05-16");
    assertEquals(2, portfolioComposition2.size());
    assertEquals(Double.valueOf(7), portfolioComposition2.get("GOOG"));
    assertEquals(Double.valueOf(4), portfolioComposition2.get("WMT"));
  }

  @Test
  public void testBuyingStocksExistingPortfolioDiffDatesSameTicker() {
    model.buyStock("Exists",
            "GOOG", 7, "2024-05-15");
    model.buyStock("Exists",
            "GOOG", 5, "2024-05-16");
    Map<String, Double> portfolioComposition1 =
            model.getPortfolioComposition("Exists", "2024-05-15");
    Map<String, Double> portfolioComposition2 =
            model.getPortfolioComposition("Exists", "2024-05-16");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(7), portfolioComposition1.get("GOOG"));
    assertEquals(1, portfolioComposition2.size());
    assertEquals(Double.valueOf(12), portfolioComposition2.get("GOOG"));
  }

  @Test
  public void testBuyingStocksExistingPortfolioDiffDatesDiffTicker() {
    model.buyStock("OOD",
            "GOOG", 4, "2024-05-14");
    model.buyStock("OOD",
            "AAPL", 8, "2024-05-15");
    Map<String, Double> portfolioComposition1 =
            model.getPortfolioComposition("OOD", "2024-05-14");
    Map<String, Double> portfolioComposition2 =
            model.getPortfolioComposition("OOD", "2024-05-15");
    assertEquals(1, portfolioComposition1.size());
    assertEquals(Double.valueOf(4), portfolioComposition1.get("GOOG"));
    assertEquals(2, portfolioComposition2.size());
    assertEquals(Double.valueOf(4), portfolioComposition2.get("GOOG"));
    assertEquals(Double.valueOf(8), portfolioComposition2.get("AAPL"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidPortfolioNameEmpty() {
    model.buyStock("",
            "GOOG", 7, "2024-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidTickerEmpty() {
    model.buyStock("Port1", "",
            7, "2024-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidStockTickerNull() {
    model.buyStock("Port1",
            null, 7, "2024-05-15");
  }

  @Test
  public void testBuyingStocksValidLeapDay() {
    model.buyStock(
            "LD", "GOOG", 7, "2024-02-29");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("LD", "2024-02-29");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(7), portfolioComposition.get("GOOG"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyingStocksInvalidDate2() {
    model.buyStock("Port1", "GOOG",
            7, "2024-05-3s");
  }

  //sell stocks tests
  @Test
  public void testSellingStocksOne() {
    model.buyStock("New", "GOOG", 25, "2024-05-15");
    model.sellStock("New", "GOOG", 5, "2024-05-16");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("New",
            "2024-05-16");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(20), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellingStocksMany() {
    model.buyStock("Many", "GOOG", 15, "2024-05-15");
    model.sellStock("Many", "GOOG", 10, "2024-05-16");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("Many",
            "2024-05-16");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(5), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellingStocksAll() {
    model.buyStock("All", "GOOG", 15, "2024-05-15");
    model.sellStock("All", "GOOG", 15, "2024-05-16");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition("All",
            "2024-05-16");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(0), portfolioComposition.get("GOOG"));
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellingMoreThanOwned() {
    model.buyStock("More", "GOOG", 10, "2024-05-15");
    model.sellStock("More", "GOOG", 15, "2024-05-16");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksNotExistent() {
    model.sellStock("Fake",
            "GOOG", 7, "2024-05-15");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidYear() {
    model.sellStock("Year",
            "GOOG", 7, "2028-05-15");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidMonth() {
    model.sellStock("Month",
            "GOOG", 7, "2024-13-15");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellingStocksInvalidDay() {
    model.sellStock("Year",
            "GOOG", 7, "2024-05-33");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidDate() {
    model.sellStock("Year",
            "GOOG", 7, "2028-02-29");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidTicker() {
    model.buyStock("Ticker", "GOOG", 10, "2024-05-15");
    model.sellStock("Ticker", "cre", 5, "2024-05-16");
  }

  //assertion error instead of IAE
  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidInput() {
    model.buyStock("Ticker", "GOOG", 10, "2024-05-15");
    model.sellStock("Ticker", "GOOG", -8, "2024-05-0s");
  }

  @Test
  public void testSellStocksDiffTickers() {
    model.buyStock(
            "DiffTickers", "GOOG", 15, "2024-05-15");
    model.buyStock(
            "DiffTickers", "WMT", 10, "2024-05-15");
    model.sellStock(
            "DiffTickers", "GOOG", 4, "2024-05-16");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("DiffTickers",
                    "2024-05-16");
    assertEquals(2, portfolioComposition.size());
    assertEquals(Double.valueOf(11), portfolioComposition.get("GOOG"));
    assertEquals(Double.valueOf(10), portfolioComposition.get("WMT"));
  }

  @Test
  public void testSellStocksAllManyTickers() {
    model.buyStock(
            "AllMany", "GOOG", 15, "2024-05-15");
    model.buyStock(
            "AllMany", "AAPL", 10, "2024-05-15");
    model.sellStock(
            "AllMany", "GOOG", 15, "2024-05-16");
    model.sellStock(
            "AllMany", "AAPL", 10, "2024-05-16");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("AllMany",
                    "2024-05-16");
    assertEquals(2, portfolioComposition.size());
    assertEquals(Double.valueOf(0), portfolioComposition.get("GOOG"));
    assertEquals(Double.valueOf(0), portfolioComposition.get("AAPL"));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidTicker2() {
    model.buyStock(
            "Empty", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Empty", "", 10, "2024-05-16");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksDateEmpty() {
    model.buyStock(
            "EmptyDate", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "EmptyDate", "GOOG", 10, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksInvalidDateFormat() {
    model.buyStock(
            "DateFormat", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "DateFormat", "GOOG", 10, "2024/05/16");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFuture() {
    model.buyStock(
            "Future", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Future", "GOOG", 10, "2028-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksWeekend() {
    model.buyStock(
            "Weekend", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Weekend", "GOOG", 10, "2024-05-18");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksHoliday() {
    model.buyStock(
            "Christmas", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Christmas", "GOOG", 10, "2024-12-25");
  }

  @Test
  public void testSellStocksLeapYear() {
    model.buyStock(
            "Leap", "GOOG", 15, "2024-02-15");
    model.sellStock(
            "Leap", "GOOG", 7, "2024-02-29");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("Leap",
                    "2024-02-29");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(8), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellStocksNewPort() {
    model.buyStock(
            "New", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "New", "GOOG", 10, "2024-05-16");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("New",
                    "2024-05-16");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(5), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellStocksManyNewPortSameTicker() {
    model.buyStock("Sell",
            "GOOG", 17, "2024-05-15");
    model.sellStock("Sell",
            "GOOG", 6, "2024-05-16");
    model.sellStock("Sell",
            "GOOG", 3, "2024-05-17");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition(
            "Sell",
            "2024-05-17");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(8), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellStockManyNewPortDifTickerSameDate() {
    model.buyStock("Many",
            "GOOG", 15, "2024-05-15");
    model.buyStock("Many",
            "WMT", 10, "2024-05-15");
    model.sellStock("Many",
            "GOOG", 5, "2024-05-16");
    model.sellStock("Many",
            "WMT", 4, "2024-05-16");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("Many",
                    "2024-05-16");
    assertEquals(2, portfolioComposition.size());
    assertEquals(Double.valueOf(10), portfolioComposition.get("GOOG"));
    assertEquals(Double.valueOf(6), portfolioComposition.get("WMT"));
  }

  @Test
  public void testSellStocksManyNewPortDiffTickerDiffDate() {
    model.buyStock(
            "DiffAll", "GOOG", 15, "2024-05-15");
    model.buyStock(
            "DiffAll", "WMT", 10, "2024-05-15");
    model.sellStock(
            "DiffAll", "GOOG", 5, "2024-05-16");
    model.sellStock(
            "DiffALl", "WMT", 4, "2024-05-17");
    Map<String, Double> portfolioComposition1 =
            model.getPortfolioComposition("DiffAll",
                    "2024-05-16");
    Map<String, Double> portfolioComposition2 =
            model.getPortfolioComposition("DiffAll",
                    "2024-05-17");
    assertEquals(2, portfolioComposition1.size());
    assertEquals(Double.valueOf(10), portfolioComposition1.get("GOOG"));
    assertEquals(Double.valueOf(10), portfolioComposition1.get("WMT"));
    assertEquals(2, portfolioComposition2.size());
    assertEquals(Double.valueOf(10), portfolioComposition2.get("GOOG"));
    assertEquals(Double.valueOf(10), portfolioComposition2.get("WMT"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellingStocksInvalidNegative() {
    model.buyStock(
            "Negative", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Negative", "GOOG", -9, "2024-05-16");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellingStocksInvalidZero() {
    model.buyStock(
            "Zero", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "Zero", "GOOG", 0, "2024-05-16");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellingStocksInvalidTickerEmpty() {
    model.buyStock(
            "TickerEmpty", "GOOG", 15, "2024-05-15");
    model.sellStock(
            "TickerEmpty", "", 10, "2024-05-16");
  }

  @Test
  public void testSellStocksExistingPortSameTicker() {
    model.buyStock("Exists",
            "GOOG", 17, "2024-05-15");
    model.sellStock("Exists",
            "GOOG", 10, "2024-05-16");
    Map<String, Double> portfolioComposition = model.getPortfolioComposition(
            "Exists",
            "2024-05-16");
    assertEquals(1, portfolioComposition.size());
    assertEquals(Double.valueOf(7), portfolioComposition.get("GOOG"));
  }

  @Test
  public void testSellStocksExistingPortDiffTicker() {
    model.buyStock(
            "Diff", "GOOG", 15, "2024-05-15");
    model.buyStock(
            "Diff", "AAPL", 10, "2024-05-15");
    model.sellStock(
            "Diff", "GOOG", 5, "2024-05-16");
    model.sellStock(
            "Diff", "AAPL", 4, "2024-05-16");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("Diff", "2024-05-16");
    assertEquals(2, portfolioComposition.size());
    assertEquals(Double.valueOf(10), portfolioComposition.get("GOOG"));
    assertEquals(Double.valueOf(6), portfolioComposition.get("AAPL"));
  }

  @Test
  public void testSellStocksMultipleTransactions() {
    model.buyStock(
            "Trans", "GOOG", 10, "2024-05-15");
    model.buyStock(
            "Trans", "AAPL", 15, "2024-05-15");
    model.sellStock(
            "Trans", "GOOG", 5, "2024-05-16");
    model.sellStock(
            "Trans", "AAPL", 7, "2024-05-16");
    model.sellStock("Trans",
            "AAPL", 2, "2024-05-17");
    Map<String, Double> portfolioComposition =
            model.getPortfolioComposition("Trans", "2024-05-17");
    assertEquals(2, portfolioComposition.size());
    assertEquals(Double.valueOf(5), portfolioComposition.get("GOOG"));
    assertEquals(Double.valueOf(6), portfolioComposition.get("AAPL"));
  }


  //composition tests
  @Test
  public void testCompositionOne() {
    model.buyStock("OneStock", "GOOG", 15, "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition(
            "OneStock", "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(15), composition.get("GOOG"));
  }

  @Test
  public void testCompositionMultipleStocksSameDate() {
    model.buyStock("MultipleSame", "GOOG", 20,
            "2024-05-15");
    model.buyStock("MultipleSame", "AAPL", 40,
            "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition(
            "MultipleSame", "2024-05-15");
    assertEquals(2, composition.size());
    assertEquals(Double.valueOf(20), composition.get("GOOG"));
    assertEquals(Double.valueOf(40), composition.get("AAPL"));
  }

  @Test
  public void testCompositionSameTickerDifferentDates() {
    model.buyStock("SameTicker", "GOOG", 5, "2024-05-15");
    model.buyStock("SameTicker", "GOOG", 10, "2024-05-16");

    Map<String, Double> composition = model.getPortfolioComposition("SameTicker",
            "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(5), composition.get("GOOG"));

    composition = model.getPortfolioComposition("SameTicker", "2024-05-16");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(15), composition.get("GOOG"));
  }


  @Test
  public void testCompositionNewPortfolio() {
    model.buyStock("NewPort", "GOOG", 10, "2024-01-01");

    Map<String, Double> composition = model.getPortfolioComposition("NewPort",
            "2024-01-01");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(10), composition.get("GOOG"));
  }

  @Test
  public void testCompositionMultipleStocksDiffTickerDiffDates() {
    model.buyStock("Comp1", "GOOG",
            15, "2024-05-15");
    model.buyStock("Comp1", "AAPL",
            20, "2024-05-16");
    model.buyStock("Comp1", "WMT",
            25, "2024-05-17");

    Map<String, Double> comp1 = model.getPortfolioComposition("Comp1",
            "2024-05-16");
    assertEquals(2, comp1.size());
    assertEquals(Double.valueOf(15), comp1.get("GOOG"));
    assertEquals(Double.valueOf(20), comp1.get("AAPL"));
  }

  @Test
  public void testCompositionBeforeTrans() {
    model.buyStock("BeforeTrans", "GOOG", 10, "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition("BeforeTrans",
            "2024-05-14");
    assertTrue(composition.isEmpty());
  }

  @Test
  public void testCompositionAfterSellingOneSameStock() {
    model.buyStock("After", "GOOG", 20, "2024-05-15");
    model.sellStock("After", "GOOG", 10, "2024-05-16");

    Map<String, Double> composition = model.getPortfolioComposition("After",
            "2024-05-16");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(10), composition.get("GOOG"));
  }

  @Test
  public void testCompositionAfterSellingMultipleDiffStock() {
    model.buyStock("Diff", "GOOG", 20, "2024-05-15");
    model.sellStock("Diff", "GOOG", 10, "2024-05-16");
    model.buyStock("Diff", "WMT", 15, "2024-05-15");
    model.sellStock("Diff", "WMT", 3, "2024-05-16");

    Map<String, Double> composition = model.getPortfolioComposition("Diff",
            "2024-05-16");
    assertEquals(2, composition.size());
    assertEquals(Double.valueOf(10), composition.get("GOOG"));
    assertEquals(Double.valueOf(12), composition.get("WMT"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionAfterSellingAll() {
    model.buyStock("All", "GOOG", 15, "2024-05-15");
    model.sellStock("All", "GOOG", 15, "2024-05-16");

    Map<String, Double> composition = model.getPortfolioComposition(
            "All", "2024-05-16");
  }

  @Test
  public void testCompositionMultipleBothBuySell() {
    model.buyStock("Multiple", "GOOG", 10, "2024-05-13");
    model.sellStock("Multiple", "GOOG", 5, "2024-05-14");
    model.buyStock("Multiple", "AAPL", 15, "2024-05-15");
    model.sellStock("Multiple", "AAPL", 10, "2024-05-16");

    Map<String, Double> expected = Map.of("GOOG", 5.0, "AAPL", 5.0);
    Map<String, Double> actual = model.getPortfolioComposition(
            "Multiple", "2024-05-16");
    assertEquals(expected, actual);
  }

  @Test
  public void testCompositionLeapYear() {
    model.buyStock("Leap", "GOOG", 7, "2024-02-29");

    Map<String, Double> composition = model.getPortfolioComposition(
            "Leap", "2024-02-29");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(7), composition.get("GOOG"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionInvalidDate() {
    model.buyStock("Invalid", "GOOG", 7, "2024-01-01");
    model.getPortfolioComposition("Invalid", "2024-14-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionFutureDate() {
    model.buyStock("Future", "GOOG", 10, "2024-05-15");
    model.getPortfolioComposition("Future", "2028-05-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionInvalidPortfolioName() {
    model.getPortfolioComposition("InvalidPort", "2024-01-01");
  }

  @Test
  public void testCompositionWithNoTrans() {
    model.buyStock("NoTrans", "GOOG", 10, "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition(
            "NoTrans", "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(10), composition.get("GOOG"));
  }

  @Test
  public void testCompositionJustCreate() {
    model.createPortfolio("Create", "GOOG", "2");
    Map<String, Double> composition = model.getPortfolioComposition("Create",
            "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(2), composition.get("GOOG"));
  }

  @Test
  public void testCompositionCreateThenBuy() {
    model.createPortfolio("Create", "GOOG", "2");
    model.buyStock("Create", "GOOG", 3, "2024-05-15");
    Map<String, Double> composition = model.getPortfolioComposition("Create",
            "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(5), composition.get("GOOG"));
  }

  @Test
  public void testCompositionCreateThenSell() {
    model.createPortfolio("Sell", "GOOG", "4");
    model.sellStock("Sell", "GOOG", 3, "2024-05-15");
    Map<String, Double> composition = model.getPortfolioComposition("Sell",
            "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(1), composition.get("GOOG"));
  }

  @Test
  public void testCompositionCreateThenBuyAndSell() {
    model.createPortfolio("Create", "GOOG", "2");
    model.buyStock("Create", "GOOG", 3, "2024-05-15");
    model.sellStock("Create", "GOOG", 3, "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition("Create",
            "2024-05-15");
    assertEquals(1, composition.size());
    assertEquals(Double.valueOf(2), composition.get("GOOG"));
  }

  @Test
  public void testCompositionCreateThenBuyMultiple() {
    model.createPortfolio("Mult", "GOOG", "2");
    model.buyStock("Mult", "AAPL", 3, "2024-05-15");
    model.buyStock("Mult", "WMT", 4, "2024-05-15");

    Map<String, Double> composition = model.getPortfolioComposition("Mult",
            "2024-05-15");
    assertEquals(3, composition.size());
    assertEquals(Double.valueOf(2), composition.get("GOOG"));
    assertEquals(Double.valueOf(3), composition.get("AAPL"));
    assertEquals(Double.valueOf(4), composition.get("WMT"));
  }

  @Test
  public void testCompositionDiffDates() {
    model.createPortfolio("Diff", "GOOG", "5");
    model.buyStock("Diff", "AAPL", 4, "2024-05-15");
    model.sellStock("Diff", "GOOG", 2, "2024-05-16");
    model.buyStock("Diff", "WMT", 7, "2024-05-17");

    Map<String, Double> composition1 = model.getPortfolioComposition("Diff",
            "2024-05-15");
    Map<String, Double> composition2 = model.getPortfolioComposition("Diff",
            "2024-05-16");
    Map<String, Double> composition3 = model.getPortfolioComposition("Diff",
            "2024-05-17");

    assertEquals(2, composition1.size());
    assertEquals(Double.valueOf(5), composition1.get("GOOG"));
    assertEquals(Double.valueOf(4), composition1.get("AAPL"));

    assertEquals(2, composition2.size());
    assertEquals(Double.valueOf(3), composition2.get("GOOG"));
    assertEquals(Double.valueOf(4), composition2.get("AAPL"));

    assertEquals(3, composition3.size());
    assertEquals(Double.valueOf(3), composition3.get("GOOG"));
    assertEquals(Double.valueOf(4), composition3.get("AAPL"));
    assertEquals(Double.valueOf(7), composition3.get("WMT"));
  }

  @Test
  public void testCompositionBetweenTwoDates() {
    model.createPortfolio("Between", "GOOG", "2");
    model.buyStock("Between", "GOOG", 3, "2024-05-14");
    model.buyStock("Between", "GOOG", 4, "2024-05-16");
    Map<String, Double> composition =
            model.getPortfolioComposition("Between", "2024-05-15");
    assertEquals(Double.valueOf(5), composition.get("GOOG"));
  }

  @Test
  public void testCompositionBuyAndSellMultiple() {
    model.createPortfolio("BS", "GOOG", "5");
    model.buyStock("BS", "AAPL", 10,
            "2024-05-14");
    model.sellStock("BS", "GOOG", 2,
            "2024-05-15");
    model.buyStock("BS", "AMZN", 8,
            "2024-05-16");
    model.sellStock("BS", "AAPL", 4,
            "2024-05-17");

    Map<String, Double> composition = model.getPortfolioComposition("BS",
            "2024-05-17");
    assertEquals(3, composition.size());
    assertEquals(Double.valueOf(3), composition.get("GOOG"));
    assertEquals(Double.valueOf(6), composition.get("AAPL"));
    assertEquals(Double.valueOf(8), composition.get("AMZN"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatePortfolioNegativeShares() {
    model.createPortfolio("Negative", "GOOG", "-5");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatePortfolioNoTicker() {

    model.createPortfolio("No", "", "5");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatePortfolioWithNullTicker() {

    model.createPortfolio("Null", null, "5");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreatePortfolioWithEmptyName() {

    model.createPortfolio("", "GOOG", "5");
  }

  //Value
  @Test
  public void testValueBeforePurchase() throws IOException {
    model.createPortfolio("Zero", "GOOG", "0");
    double value = model.getPortfolioValue("Empty", "2024-05-10",
            mockStockPrice, model.getPortfolios());
    assertEquals(0.0, value, 0.01);
  }

  @Test
  public void testValueOnePurchase() throws IOException {
    model.createPortfolio("One", "GOOG", "5");
    double value = model.getPortfolioValue("One", "2024-05-15",
            mockStockPrice, model.getPortfolios());
    assertEquals(5 * mockStockPrice.getPrice("GOOG", "2024-05-15"), value,
            0.01);
  }


  @Test
  public void testValueManyPurchases() throws IOException {
    model.createPortfolio("Multi", "GOOG", "5");
    model.buyStock("Multi", "AAPL", 7, "2024-05-16");
    double value = model.getPortfolioValue("Multi", "2024-05-16",
            mockStockPrice, model.getPortfolios());
    assertEquals(
            5 * mockStockPrice.getPrice("GOOG", "2024-05-16")
                    + 7 * mockStockPrice.getPrice("AAPL", "2024-05-16"),
            value,
            0.01
    );
  }

  @Test
  public void testValueBuyingAndSelling() throws IOException {
    model.createPortfolio("BuySell", "GOOG", "5");
    model.buyStock("BuySell", "AAPL", 10, "2024-05-15");
    model.sellStock("BuySell", "GOOG", 4, "2024-05-16");
    double value = model.getPortfolioValue("BuySell", "2024-05-16",
            mockStockPrice, model.getPortfolios());
    assertEquals(
            1 * mockStockPrice.getPrice("GOOG", "2024-05-16")
                    + 10 * mockStockPrice.getPrice("AAPL", "2024-05-16"),
            value,
            0.01
    );
  }

  @Test
  public void testValueWithNoTrans() throws IOException {
    model.createPortfolio("Empty", "", "0");
    double value = model.getPortfolioValue("Empty", "2024-05-14", mockStockPrice,
            model.getPortfolios());
    assertEquals(0.0, value, 0.01);
  }

  @Test
  public void testValueCreatingAndBuyingDifftDates() throws IOException {
    model.createPortfolio("Hi", "GOOG", "5");
    model.buyStock("Hi", "WMT", 10, "2024-05-15");
    double value = model.getPortfolioValue("Hi", "2024-05-17",
            mockStockPrice, model.getPortfolios());
    assertEquals(
            5 * mockStockPrice.getPrice("GOOG", "2024-05-15")
                    + 10 * mockStockPrice.getPrice("WMT", "2024-05-17"),
            value,
            0.01
    );
  }

  @Test
  public void testValueSellAll() throws IOException {
    model.createPortfolio("SellAll", "GOOG", "5");
    model.buyStock("SellAll", "AAPL", 8, "2024-05-15");
    model.sellStock("SellAll", "GOOG", 5, "2024-05-16");
    model.sellStock("SellAll", "AAPL", 8, "2024-05-17");
    double value = model.getPortfolioValue("SellAll", "2024-05-17",
            mockStockPrice, model.getPortfolios());
    assertEquals(0.0, value, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidDateFormat() throws IOException {
    model.createPortfolio("InvalidDate", "GOOG", "5");
    model.getPortfolioValue("InvalidDate", "2024/05/15", mockStockPrice,
            model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithFuture() throws IOException {
    model.createPortfolio("Future", "GOOG", "5");
    model.getPortfolioValue("FutureDate", "2028-05-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithNonExistentPortfolio() throws IOException {
    model.getPortfolioValue("NonExistent", "2024-05-15",
            mockStockPrice, model.getPortfolios());
  }

  //distribution tests
  @Test
  public void testDistribution() throws IOException {
    model.createPortfolio("CreatePort", "GOOG", "10");
    Map<String, Double> distribution = model.getPortfolioValueDistribution("CreatePort",
            "2024-05-14", mockStockPrice, model.getPortfolios());
    assertEquals(1, distribution.size());
    assertEquals(10 * mockStockPrice.getPrice("GOOG", "2024-05-14"),
            distribution.get("GOOG"), 0.01);
  }

  @Test
  public void testDistributionBuyStocksOnce() throws IOException {
    model.createPortfolio("Buy", "GOOG", "10");
    model.buyStock("Buy", "AAPL", 5, "2024-05-15");
    Map<String, Double> distribution = model.getPortfolioValueDistribution("Buy",
            "2024-05-15", mockStockPrice, model.getPortfolios());
    assertEquals(2, distribution.size());
    assertEquals(10 * mockStockPrice.getPrice("GOOG", "2024-05-15"),
            distribution.get("GOOG"), 0.01);
    assertEquals(5 * mockStockPrice.getPrice("AAPL", "2024-05-15"),
            distribution.get("AAPL"), 0.01);
  }

  @Test
  public void testDistributionSellStocksOnce() throws IOException {
    model.createPortfolio("Sell", "GOOG", "10");
    model.buyStock("Sell", "AAPL", 5, "2024-05-15");
    model.sellStock("Sell", "AAPL", 4, "2024-05-15");
    Map<String, Double> distribution = model.getPortfolioValueDistribution("Sell",
            "2024-05-15", mockStockPrice, model.getPortfolios());
    double expectedG = 10 * mockStockPrice.getPrice("GOOG", "2024-05-15");
    double expectedA = 1 * mockStockPrice.getPrice("AAPL", "2024-05-15");
    assertEquals(2, distribution.size());
    assertEquals(expectedG, distribution.get("GOOG"), 0.01);
    assertEquals(expectedA, distribution.get("AAPL"), 0.01);
  }

  @Test
  public void testDistributionBuyMultiple() throws IOException {
    model.createPortfolio("BuyMult", "GOOG", "10");
    model.buyStock("BuyMult", "AAPL", 5, "2024-05-15");
    model.buyStock("BuyMult", "WMT", 3, "2024-05-16");

    Map<String, Double> distribution = model.getPortfolioValueDistribution("BuyMult",
            "2024-05-16", mockStockPrice, model.getPortfolios());

    assertEquals(3, distribution.size());
    assertEquals(10 * mockStockPrice.getPrice("GOOG", "2024-05-16"),
            distribution.get("GOOG"), 0.01);
    assertEquals(5 * mockStockPrice.getPrice("AAPL", "2024-05-16"),
            distribution.get("AAPL"), 0.01);
    assertEquals(3 * mockStockPrice.getPrice("WMT", "2024-05-16"),
            distribution.get("WMT"), 0.01);
  }

  @Test
  public void testDistributionSellMultiple() throws IOException {
    model.createPortfolio("SellMult", "GOOG", "10");
    model.buyStock("SellMult", "AAPL", 5, "2024-05-15");
    model.buyStock("SellMult", "WMT", 3, "2024-05-16");
    model.sellStock("SellMult", "GOOG", 4, "2024-05-17");
    model.sellStock("SellMult", "AAPL", 2, "2024-05-17");

    Map<String, Double> distribution = model.getPortfolioValueDistribution("SellMult",
            "2024-05-17", mockStockPrice, model.getPortfolios());

    assertEquals(3, distribution.size());
    assertEquals(6 * mockStockPrice.getPrice("GOOG", "2024-05-16"),
            distribution.get("GOOG"), 0.01);
    assertEquals(3 * mockStockPrice.getPrice("AAPL", "2024-05-16"),
            distribution.get("AAPL"), 0.01);
    assertEquals(3 * mockStockPrice.getPrice("WMT", "2024-05-16"),
            distribution.get("WMT"), 0.01);
  }

  @Test
  public void testDistributionInitial() throws IOException {
    model.createPortfolio("Init.", "GOOG", "10");
    Map<String, Double> distribution = model.getPortfolioValueDistribution("Init.",
            "2024-05-15", mockStockPrice, model.getPortfolios());
    double expected = 10 * mockStockPrice.getPrice("GOOG", "2024-05-15");

    assertEquals(1, distribution.size());
    assertEquals(expected, distribution.get("GOOG"), 0.01);
  }

  @Test
  public void testDistributionBuyDiff() throws IOException {
    model.createPortfolio("BuyDiff", "GOOG", "10");
    model.buyStock("BuyDiff", "AAPL", 5, "2024-05-15");

    Map<String, Double> distribution = model.getPortfolioValueDistribution("BuyDiff",
            "2024-05-15", mockStockPrice, model.getPortfolios());

    double expectedG = 10 * mockStockPrice.getPrice("GOOG", "2024-05-15");
    double expectedA = 5 * mockStockPrice.getPrice("AAPL", "2024-05-15");
    assertEquals(2, distribution.size());
    assertEquals(expectedG, distribution.get("GOOG"), 0.01);
    assertEquals(expectedA, distribution.get("AAPL"), 0.01);
  }

  @Test
  public void testDistributionBuySell() throws IOException {
    model.createPortfolio("BuySell", "GOOG", "10");
    model.buyStock("BuySell", "AAPL", 5, "2024-05-15");
    model.sellStock("BuySell", "GOOG", 4, "2024-05-16");

    Map<String, Double> distribution = model.getPortfolioValueDistribution(
            "BuySell", "2024-05-16", mockStockPrice, model.getPortfolios());

    double expectedG = 6 * mockStockPrice.getPrice("GOOG", "2024-05-16");
    double expectedA = 5 * mockStockPrice.getPrice("AAPL", "2024-05-16");

    assertEquals(2, distribution.size());
    assertEquals(expectedG, distribution.get("GOOG"), 0.01);
    assertEquals(expectedA, distribution.get("AAPL"), 0.01);
  }

  @Test
  public void testDistributionAfterRebalance() throws IOException {
    model.createPortfolio("Rebalance", "GOOG", "10");
    model.buyStock("Rebalance", "AAPL", 5, "2024-05-15");

    Map<String, Double> weights = Map.of(
            "GOOG", 0.5,
            "AAPL", 0.5
    );

    MockStockPrice mockStockPrice = new MockStockPrice();
    model.rebalance("Rebalance", "2024-05-16", weights, mockStockPrice);
    Map<String, Double> distribution = model.getPortfolioValueDistribution(
            "Rebalance", "2024-05-16", mockStockPrice, model.getPortfolios());
    double totalValue = 10 * mockStockPrice.getPrice("GOOG", "2024-05-16")
            + 5 * mockStockPrice.getPrice("AAPL", "2024-05-16");

    double expectedG = 0.5 * totalValue;
    double expectedA = 0.5 * totalValue;

    assertEquals(2, distribution.size());
    assertEquals(expectedG, distribution.get("GOOG"), 0.01);
    assertEquals(expectedA, distribution.get("AAPL"), 0.01);
  }

  @Test
  public void testDistributionNoTrans() throws IOException {
    model.createPortfolio("NoTrans", "GOOG", "0");
    Map<String, Double> distribution = model.getPortfolioValueDistribution(
            "NoTrans", "2024-05-10", mockStockPrice, model.getPortfolios());

    assertEquals(0, distribution.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionWeekend() throws IOException {
    model.createPortfolio("Weekend", "GOOG", "10");
    model.buyStock("Weekend", "GOOG", 5, "2024-06-08");

    Map<String, Double> distribution = model.getPortfolioValueDistribution(
            "Weekend", "2024-06-09", mockStockPrice, model.getPortfolios());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testDistributionOnHoliday() throws IOException {
    model.createPortfolio("Holiday", "GOOG", "10");
    model.buyStock("Holiday", "AAPL", 5, "2023-07-03");

    model.getPortfolioValueDistribution("Holiday", "2023-07-04",
            mockStockPrice, model.getPortfolios());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidFormat() throws IOException {
    model.createPortfolio("InvalidFormat", "GOOG", "10");
    model.getPortfolioValueDistribution("InvalidFormat", "2024/05/15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidFuture() throws IOException {
    model.createPortfolio("Future", "GOOG", "10");
    model.getPortfolioValueDistribution("Future", "2029-05-15",
            new MockStockPrice(), model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidTicker() throws IOException {
    model.createPortfolio("Ticker", "invalid", "10");
    model.getPortfolioValueDistribution("Ticker", "2024-05-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidRange() throws IOException {
    model.createPortfolio("Range", "GOOG", "10");
    model.getPortfolioValueDistribution("Range", "2024-05-32",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidNegative() throws IOException {
    model.createPortfolio("Negative", "GOOG", "-5");
    model.getPortfolioValueDistribution("Negative", "2024-05-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionNoTicker() throws IOException {
    model.createPortfolio("NoTick", "", "10");
    model.getPortfolioValueDistribution("NoTick", "2024-05-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidDate2() throws IOException {
    model.createPortfolio("NegativeD", "GOOG", "10");
    model.getPortfolioValueDistribution("NegativeD", "-2024-05-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidMonth() throws IOException {
    model.createPortfolio("Month", "GOOG", "10");
    model.getPortfolioValueDistribution("Month", "2024-13-15",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionInvalidDay() throws IOException {
    model.createPortfolio("Day", "GOOG", "10");
    model.getPortfolioValueDistribution("Day", "2024-05-32",
            mockStockPrice, model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistributionLeapYear() throws IOException {
    model.createPortfolio("LeapYear", "GOOG", "10");
    model.buyStock("LeapYear", "GOOG", 5, "2024-02-28");
    model.getPortfolioValueDistribution("LeapYear", "2024-02-29",
            mockStockPrice, model.getPortfolios());
  }

  //save portfolio tests
  @Test
  public void testSavePortfolioNoStocks() throws IOException {
    String portfolioName = "Empty";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "", "0");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertEquals("Ticker Symbol: , Shares: 0.0", lines.get(1));
      assertEquals("no transactions", lines.get(2));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testSavePortfolioOneStock() throws IOException {
    String portfolioName = "One";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "GOOG", "15");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertEquals("Ticker Symbol: GOOG, Shares: 15.0", lines.get(1));
      assertEquals("no transactions", lines.get(2));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testSavePortfolioMultipleStocks() throws IOException {
    String portfolioName = "Multiple";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "GOOG", "10");
      model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
      model.buyStock(portfolioName, "WMT", 7, "2024-05-15");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertTrue(lines.contains("Ticker Symbol: GOOG, Amount of Stock: 10.0"));
      assertTrue(lines.contains("Ticker Symbol: AAPL, Amount of Stock: 5.0"));
      assertTrue(lines.contains("Ticker Symbol: WMT, Amount of Stock: 7.0"));
      assertTrue(lines.contains("no transactions"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testSavePortfolioWithTrans() throws IOException {
    String portfolioName = "Transactions";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "GOOG", "20");
      model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
      model.sellStock(portfolioName, "GOOG", 10, "2024-05-16");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertTrue(lines.contains("Ticker Symbol: GOOG, Amount of Stock: 10.0"));
      assertTrue(lines.contains("Ticker Symbol: AAPL, Amount of Stock: 5.0"));
      assertTrue(lines.contains("Ticker Symbol: GOOG, Shares: 10.0, "
              + "Date: 2024-05-16, Buy or Sell: SELL"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }


  @Test
  public void testSavePortfolioMultipleTransDiffDates() throws IOException {
    String portfolioName = "Diff";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "GOOG", "10");
      model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
      model.sellStock(portfolioName, "GOOG", 2, "2024-05-16");
      model.buyStock(portfolioName, "WMT", 7, "2024-05-17");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertTrue(lines.contains("Ticker Symbol: GOOG, Amount of Stock: 8.0"));
      assertTrue(lines.contains("Ticker Symbol: AAPL, Amount of Stock: 5.0"));
      assertTrue(lines.contains("Ticker Symbol: WMT, Amount of Stock: 7.0"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testSavePortfolioEmpty() throws IOException {
    String portfolioName = "Empty";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "", "0");
      model.savePortfolio(portfolioName);

      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());
      assertEquals("Portfolio Name: " + portfolioName, lines.get(0));
      assertEquals("Ticker Symbol: , Shares: 0.0", lines.get(1));
      assertEquals("no transactions", lines.get(2));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testSavePortfolioAfterPartialTransaction() throws IOException {
    String portfolioName = "PartialTransaction";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      model.createPortfolio(portfolioName, "GOOG", "20");
      model.sellStock(portfolioName, "GOOG", 5, "2024-05-15");
      model.buyStock(portfolioName, "AAPL", 10, "2024-05-16");
      model.savePortfolio(portfolioName);
      File file = new File(filePath);
      assertTrue(file.exists());

      List<String> lines = Files.readAllLines(file.toPath());

      String expected = String.join(System.lineSeparator(),
              "Portfolio Name: " + portfolioName,
              "Ticker Symbol: GOOG, Shares: 20.0",
              "Ticker Symbol: GOOG, Shares: 5.0, Date: 2024-05-15, Buy or Sell: SELL",
              "Ticker Symbol: AAPL, Shares: 10.0, Date: 2024-05-16, Buy or Sell: BUY",
              "");

      String actual = String.join(System.lineSeparator(), lines) + System.lineSeparator();

      assertEquals(expected, actual);
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSavePortfolioInvalidPath() throws IOException {
    String portfolioName = "Path";
    String filePath = "invalid\0path/" + portfolioName + ".txt";

    model.createPortfolio(portfolioName, "GOOG", "10");
    model.savePortfolio(filePath);
  }


  //load portfolio tests
  @Test
  public void testLoadPortfolio() throws IOException {
    String portfolioName = "Temp";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      File file = new File(filePath);

      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Shares: 10\n"
              + "Ticker Symbol: AAPL, Shares: 5\n"
              + "Ticker Symbol: WMT, Shares: 7\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);

      Map<String, Double> composition =
              model.getPortfolioComposition(portfolioName, "2024-05-15");
      assertEquals(3, composition.size());
      assertEquals(Double.valueOf(10), composition.get("GOOG"));
      assertEquals(Double.valueOf(5), composition.get("AAPL"));
      assertEquals(Double.valueOf(7), composition.get("WMT"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testLoadPortfolioNoStocks() throws IOException {
    String portfolioName = "Empty";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);

      Map<String, Double> composition =
              model.getPortfolioComposition(portfolioName, "2024-05-15");
      assertTrue(composition.isEmpty());
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testLoadPortfolioOneStock() throws IOException {
    String portfolioName = "One";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Amount of Stock: 15\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);

      Map<String, Double> composition = model.getPortfolioComposition(portfolioName,
              "2024-05-15");
      assertEquals(1, composition.size());
      assertEquals(Double.valueOf(15), composition.get("GOOG"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioInvalidFormat() throws IOException {
    String portfolioName = "Invalid";
    File temp = new File("temp");
    if (!temp.exists()) {
      temp.mkdir();
    }
    String filePath = temp.getPath() + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Invalid Data";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(filePath);
    } finally {
      new File(filePath).delete();
      temp.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioInvalidTickerForm() throws IOException {
    String portfolioName = "Ticker";
    File temp = new File("temp");
    if (!temp.exists()) {
      temp.mkdir();
    }
    String filePath = temp.getPath() + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Amount of Stock: ten\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(filePath);
    } finally {
      new File(filePath).delete();
      temp.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioNoName() throws IOException {
    String portfolioName = "Nothing";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Ticker Symbol: GOOG, Amount of Stock: 10\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test
  public void testLoadPortfolioAddMore() throws IOException {
    String portfolioName = "More";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Amount of Stock: 10\n"
              + "Extra Data: This should be ignored\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);

      Map<String, Double> composition = model.getPortfolioComposition(portfolioName,
              "2024-05-15");
      assertEquals(1, composition.size());
      assertEquals(Double.valueOf(10), composition.get("GOOG"));
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioNoFile() throws IOException {
    String portfolioName = "Nothing";
    model.loadPortfolio(portfolioName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioMissingInfo() throws IOException {
    String portfolioName = "Missing";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioInvalid() throws IOException {
    String portfolioName = "Invalid";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Invalid info\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadPortfolioInvalid2() throws IOException {
    String portfolioName = "Invalid2";
    String directoryPath = "res/portfolios";
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    String filePath = directoryPath + "/" + portfolioName + ".txt";

    try {
      String fileContent = "Portfolio Name: " + portfolioName + "\n"
              + "Ticker Symbol: GOOG, Amount of Stock: ten\n";

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(fileContent);
      }

      model.loadPortfolio(portfolioName);
    } finally {
      new File(filePath).delete();
      directory.delete();
    }
  }

  //rebalance test
  @Test
  public void testRebalanceEqual() throws IOException {
    String portfolioName = "EqualD";
    model.createPortfolio(portfolioName, "GOOG", "10");
    model.buyStock(portfolioName, "AAPL", 5, "2024-01-01");
    model.buyStock(portfolioName, "WMT", 7, "2024-01-01");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.3333,
            "AAPL", 0.3333,
            "WMT", 0.3333
    );

    String rebalance = model.rebalance(portfolioName,
            "2024-05-15", weights, mockStockPrice);

    assertTrue(rebalance.contains("Sell"));
    assertTrue(rebalance.contains("Buy"));

    Map<String, Double> newComposition = model.getPortfolioComposition(portfolioName,
            "2024-05-15");

    double totalValue = 10 * 100.0 + 5 * 150.0 + 7 * 200.0;
    double expectedValuePerStock = totalValue * 0.3333;

    assertEquals(expectedValuePerStock
                    / mockStockPrice.getPrice("GOOG", "2024-05-15"),
            newComposition.get("GOOG"), 0.01);
    assertEquals(expectedValuePerStock
                    / mockStockPrice.getPrice("AAPL", "2024-05-15"),
            newComposition.get("AAPL"), 0.01);
    assertEquals(expectedValuePerStock
                    / mockStockPrice.getPrice("WMT", "2024-05-15"),
            newComposition.get("WMT"), 0.01);
  }

  @Test
  public void testRebalanceUnequal() throws IOException {
    String portfolioName = "Unequal";
    model.createPortfolio(portfolioName, "GOOG", "10");
    model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
    model.buyStock(portfolioName, "WMT", 7, "2024-05-15");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.5,
            "AAPL", 0.25,
            "WMT", 0.25
    );

    String rebalanceTrack = model.rebalance(portfolioName, "2024-05-15", weights,
            mockStockPrice);

    assertTrue(rebalanceTrack.contains("Sell"));
    assertTrue(rebalanceTrack.contains("Buy"));

    Map<String, Double> newComposition = model.getPortfolioComposition(portfolioName,
            "2024-05-15");

    double totalValue = 10 * 100.0 + 5 * 150.0 + 7 * 200.0;
    double expectedValueGOOG = totalValue * 0.5;
    double expectedValueAAPL = totalValue * 0.25;
    double expectedValueWMT = totalValue * 0.25;

    assertEquals(expectedValueGOOG / mockStockPrice.getPrice("GOOG",
            "2024-05-15"), newComposition.get("GOOG"), 0.01);
    assertEquals(expectedValueAAPL / mockStockPrice.getPrice("AAPL",
            "2024-05-15"), newComposition.get("AAPL"), 0.01);
    assertEquals(expectedValueWMT / mockStockPrice.getPrice("WMT",
            "2024-05-15"), newComposition.get("WMT"), 0.01);
  }


  @Test
  public void testRebalanceNo() throws IOException {
    String portfolioName = "Empty";
    model.createPortfolio(portfolioName, "GOOG", "0");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.0,
            "AAPL", 0.0,
            "WMT", 0.0
    );

    String rebalance = model.rebalance(portfolioName, "2024-05-15", weights, mockStockPrice);

    assertTrue(rebalance.isEmpty());

    Map<String, Double> newComposition =
            model.getPortfolioComposition(portfolioName, "2024-05-15");
    assertTrue(newComposition.isEmpty());
  }


  @Test
  public void testRebalanceBalanced() throws IOException {
    String portfolioName = "Balanced";
    model.createPortfolio(portfolioName, "GOOG", "2");
    model.buyStock(portfolioName, "AAPL", 2, "2024-05-15");
    model.buyStock(portfolioName, "WMT", 1, "2024-05-15");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.3333,
            "AAPL", 0.3333,
            "WMT", 0.3333
    );

    String rebalance =
            model.rebalance(portfolioName, "2024-05-15", weights, mockStockPrice);

    assertTrue(rebalance.isEmpty());

    Map<String, Double> newComposition =
            model.getPortfolioComposition(portfolioName, "2024-05-15");
    assertEquals(Double.valueOf(2), newComposition.get("GOOG"));
    assertEquals(Double.valueOf(2), newComposition.get("AAPL"));
    assertEquals(Double.valueOf(1), newComposition.get("WMT"));
  }

  @Test
  public void testRebalanceAddNew() throws IOException {
    String portfolioName = "AddNew";
    model.createPortfolio(portfolioName, "GOOG", "10");
    model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.4,
            "AAPL", 0.4,
            "WMT", 0.2
    );

    String rebalance = model.rebalance(portfolioName, "2024-05-15", weights, mockStockPrice);

    assertTrue(rebalance.contains("Sell"));
    assertTrue(rebalance.contains("Buy"));

    Map<String, Double> newComposition =
            model.getPortfolioComposition(portfolioName, "2024-05-15");
    double totalValue = 1750.0;
    double expectedValueGOOG = totalValue * 0.4;
    double expectedValueAAPL = totalValue * 0.4;
    double expectedValueWMT = totalValue * 0.2;

    assertEquals(expectedValueGOOG
                    / mockStockPrice.getPrice("GOOG", "2024-05-15"),
            newComposition.get("GOOG"), 0.01);
    assertEquals(expectedValueAAPL
                    / mockStockPrice.getPrice("AAPL", "2024-05-15"),
            newComposition.get("AAPL"), 0.01);
    assertEquals(expectedValueWMT
                    / mockStockPrice.getPrice("WMT", "2024-05-15"),
            newComposition.get("WMT"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalanceInvalidWeights() throws IOException {
    String portfolioName = "Invalid";
    model.createPortfolio(portfolioName, "GOOG", "10");
    model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
    model.buyStock(portfolioName, "WMT", 7, "2024-05-15");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.5,
            "AAPL", 0.3,
            "WMT", 0.3
    );

    model.rebalance(portfolioName, "2024-05-15", weights, mockStockPrice);
  }


  @Test
  public void testRebalancePartialSell() throws IOException {
    String portfolioName = "Partial";
    model.createPortfolio(portfolioName, "GOOG", "20");
    model.buyStock(portfolioName, "AAPL", 5, "2024-05-15");
    model.buyStock(portfolioName, "WMT", 10, "2024-05-15");

    MockStockPrice mockStockPrice = new MockStockPrice();

    Map<String, Double> weights = Map.of(
            "GOOG", 0.3,
            "AAPL", 0.4,
            "WMT", 0.3
    );

    String rebalance = model.rebalance(portfolioName, "2024-05-15", weights, mockStockPrice);

    assertTrue(rebalance.contains("Sell"));
    assertTrue(rebalance.contains("Buy"));

    Map<String, Double> newComposition =
            model.getPortfolioComposition(portfolioName, "2024-05-15");

    double totalValue = 20 * 100.0 + 5 * 150.0 + 10 * 200.0;
    double expectedValueGOOG = totalValue * 0.3;
    double expectedValueAAPL = totalValue * 0.4;
    double expectedValueWMT = totalValue * 0.3;

    assertEquals(expectedValueGOOG
                    / mockStockPrice.getPrice("GOOG", "2024-05-15"),
            newComposition.get("GOOG"), 0.01);
    assertEquals(expectedValueAAPL
                    / mockStockPrice.getPrice("AAPL", "2024-05-15"),
            newComposition.get("AAPL"), 0.01);
    assertEquals(expectedValueWMT
                    / mockStockPrice.getPrice("WMT", "2024-05-15"),
            newComposition.get("WMT"), 0.01);
  }

  //performance test

  @Test
  public void testPortfolioPerformanceDaily() throws IOException {
    model.createPortfolio("byDay", "GOOG", "10");
    model.buyStock("byDay", "AAPL", 5, "2024-01-01");
    model.buyStock("byDay", "WMT", 7, "2024-01-02");

    Map<String, Double> performance = model.portfolioPerformance("byDay",
            "2024-01-01", "2024-01-05", "daily");

    assertEquals(5, performance.size());
    assertTrue(performance.containsKey("2024-01-01"));
    assertTrue(performance.containsKey("2024-01-02"));
    assertTrue(performance.containsKey("2024-01-03"));
    assertTrue(performance.containsKey("2024-01-04"));
    assertTrue(performance.containsKey("2024-01-05"));
  }

  @Test
  public void testPortfolioPerformanceMonthly() throws IOException {
    model.createPortfolio("byMonth", "GOOG", "10");
    model.buyStock("byMonth", "AAPL", 5, "2024-01-01");
    model.buyStock("byMonth", "WMT", 7, "2024-02-01");

    Map<String, Double> performance = model.portfolioPerformance("byMonth",
            "2024-01-01", "2024-03-01", "monthly");

    assertEquals(3, performance.size());
    assertTrue(performance.containsKey("2024-01-01"));
    assertTrue(performance.containsKey("2024-02-01"));
    assertTrue(performance.containsKey("2024-03-01"));
  }

  @Test
  public void testPortfolioPerformanceYearly() throws IOException {
    model.createPortfolio("byYear", "GOOG", "10");
    model.buyStock("byYear", "AAPL", 5, "2024-01-01");
    model.buyStock("byYear", "WMT", 7, "2025-01-01");

    Map<String, Double> performance = model.portfolioPerformance("byYear",
            "2024-01-01", "2026-01-01", "yearly");

    assertEquals(3, performance.size());
    assertTrue(performance.containsKey("2024-01-01"));
    assertTrue(performance.containsKey("2025-01-01"));
    assertTrue(performance.containsKey("2026-01-01"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceInvalidInterval() throws IOException {
    model.createPortfolio("noInterval", "GOOG", "10");
    model.buyStock("noInterval", "AAPL", 5, "2024-01-01");

    model.portfolioPerformance("noInterval", "2024-01-01",
            "2024-01-05", "weekly");
  }


}