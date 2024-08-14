import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.commands.CrossOver;
import controller.commands.ICommand;
import controller.commands.MovingAverage;
import model.IModel;
import model.ModelImpl;
import model.Stock;
import view.BetterIView;
import view.BetterViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is the test class for the methods in model impl. This ensures that all the methods found in
 * model implementations are working as they should as well as all the commands. It tests the
 * functionality of all the public methods we have made in order for the model to implement
 * all the code from the model interface.
 */

public class ModelImplTest {
  private Map<String, Double> closingPrices;
  private IModel model;

  @Before
  public void setup() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(output);
    Scanner scanner = new Scanner(System.in);
    StringBuilder log = new StringBuilder();
    BetterIView view = new BetterViewImpl(printStream, scanner, log);
    model = new ModelImpl();

    closingPrices = new HashMap<>();
    closingPrices.put("2024-01-02", 99.0);
    closingPrices.put("2024-01-03", 100.0);
    closingPrices.put("2024-01-04", 101.0);
    closingPrices.put("2024-01-05", 102.0);
    closingPrices.put("2024-01-06", 103.0); //saturday
    closingPrices.put("2024-01-07", 104.0); //sunday
    closingPrices.put("2024-01-08", 105.0);
    closingPrices.put("2024-01-09", 102.0);
    closingPrices.put("2024-12-25", 107.0);
    closingPrices.put("2024-12-31", 110.0);
    closingPrices.put("2024-01-01", 115.0);
    closingPrices.put("2024-02-28", 120.0);
    closingPrices.put("2024-02-29", 125.0); //Leap Year

    Stock stock = new Stock("GOOG", closingPrices);
    model.addStock(stock);
    ICommand movingAverageCommand = new MovingAverage(view);
    //ICommand portfolioCommand = new Portfolio(view);
    ICommand crossOverCommand = new CrossOver(view);
  }

  @Test
  public void testGainOrLoss() {
    assertEquals(3, model.getGainOrLoss("GOOG", "2024-01-02", "2024-01-09"), 0.001);
  }

  @Test
  public void testGain() {
    assertEquals(4, model.getGainOrLoss("GOOG", "2024-01-02",
            "2024-01-06"), 0.001);
  }

  @Test
  public void testLoss() {
    assertEquals(-3.0, model.getGainOrLoss("GOOG", "2024-01-08",
            "2024-01-09"), 0.001);
  }

  @Test
  public void testGainOrLossNoChange() {
    assertEquals(1.0, model.getGainOrLoss("GOOG", "2024-01-05",
            "2024-01-06"), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossNewYears() {
    model.getGainOrLoss("GOOG", "2023-12-31",
            "2024-01-01");
  }

  @Test
  public void testGainOrLossLeapYear() {
    assertEquals(5.0, model.getGainOrLoss("GOOG",
            "2024-02-28", "2024-02-29"), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidStartDay() {
    model.getGainOrLoss("GOOG", "2024-01-32", "2024-02-13");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidEndDay() {
    model.getGainOrLoss("GOOG", "2024-01-11", "2024-02-31");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidStartMonth() {
    model.getGainOrLoss("GOOG", "2024-13-02", "2024-04-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidEndMonth() {
    model.getGainOrLoss("GOOG", "2024-12-02", "2024-14-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidStartYear() {
    model.getGainOrLoss("GOOG", "2026-12-02", "2024-14-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidEndYear() {
    model.getGainOrLoss("GOOG", "2024-12-02", "2038-14-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossWeekend() {
    model.getGainOrLoss("GOOG", "2024-01-05", "2038-01-06");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossStartDateAfterEndDate() {
    model.getGainOrLoss("GOOG", "2024-01-08", "2024-01-04");
  }

  @Test
  public void testGainOrLossSameDates() {
    assertEquals(0, model.getGainOrLoss("GOOG",
            "2024-01-03", "2024-01-03"), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLossInvalidTicker() {
    model.getGainOrLoss("darshini", "2024-01-03", "2024-01-03");
  }

  //hasDate method tests
  @Test
  public void testHasDateExisting() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertTrue(stock.hasDate("2024-01-03"));
  }

  @Test
  public void testHasDateNot() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate("2022-01-03"));
  }

  @Test
  public void testHasDateExistingNotBcFuture() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate("2029-01-03"));
  }

  @Test
  public void testHasDateExistingDayMonthYear() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate("01-05-2024"));
  }

  @Test
  public void testHasDateExistingDayInvalidDateFormate() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate("2024/01/05"));
  }

  @Test
  public void testHasDateNothing() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate(null));
  }

  @Test
  public void testHasDateEmpty() {
    Stock stock = new Stock("GOOG", closingPrices);
    assertFalse(stock.hasDate(""));
  }

  @Test
  public void testCalculateMovingAverage() {
    assertEquals(103.2, model.getMovingAverage("GOOG", "2024-01-09",
            5), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidMonth() {
    model.getMovingAverage("GOOG", "2024-32-09", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidDay() {
    model.getMovingAverage("GOOG", "2024-03-32", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidYear() {
    model.getMovingAverage("GOOG", "20-03-12", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidFuture() {
    model.getMovingAverage("GOOG", "2028-03-08", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculatingMovingAverageInvalidFormat() {
    model.getMovingAverage("GOOG", "2024/01/09", 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidTickerSybmol() {
    model.getMovingAverage("pradhu", "2024-01-08", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidNegativeXDays() {
    model.getMovingAverage("GOOG", "2024-01-08", -4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageInvalidXDays() {
    model.getMovingAverage("GOOG", "2024-01-02", 13);
  }

  @Test
  public void testCalculateMovingAverageOneDay() {
    assertEquals(103.66666666666667, model.getMovingAverage("GOOG",
            "2024-01-09", 3), 0.001);
  }

  @Test
  public void testCalculateMovingAverageLeapYear() {
    assertEquals(113.0, model.getMovingAverage("GOOG", "2024-02-29",
            4), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateMovingAverageHoliday() {
    model.getMovingAverage("GOOG", "2024-01-01",
            2);
  }

  @Test
  public void testCalculateMovingAverageRolloverBetweenMonths() {
    assertEquals(113.0, model.getMovingAverage("GOOG", "2024-02-29",
            4), 0.001);
  }

  //crossover tests
  @Test
  public void testValidCrossover() {
    List<String> expected = Arrays.asList("2024-01-06", "2024-01-07", "2024-01-08");
    List<String> actual = model.getCrossOver("GOOG", "2024-01-02",
            "2024-01-09", 5);
    assertEquals(expected, actual);
  }

  @Test
  public void testCrossoverCrossovers2() {
    List<String> expected = Arrays.asList("2024-01-04", "2024-01-05");
    List<String> actual = model.getCrossOver("GOOG", "2024-01-02",
            "2024-01-05", 3);
    assertEquals(expected, actual);
  }

  @Test
  public void testCrossoverLeapYear() {
    List<String> expected = Arrays.asList("2024-02-28", "2024-02-29");
    List<String> actual = model.getCrossOver("GOOG", "2024-02-27",
            "2024-02-29", 3);
    assertEquals(expected, actual);
  }

  @Test
  public void testNoCrossovers() {
    List<String> expected = List.of();
    List<String> actual = model.getCrossOver("GOOG", "2024-01-02",
            "2024-01-04", 5);
    assertEquals(expected, actual);
  }

  @Test
  public void testMultipleCrossoversSmallRange() {
    List<String> expected = Arrays.asList("2024-01-05", "2024-01-06", "2024-01-07");
    List<String> actual = model.getCrossOver("GOOG", "2024-01-05",
            "2024-01-07", 2);
    assertEquals(expected, actual);
  }

  @Test
  public void testCrossoverXDaysOne() {
    List<String> expected = Arrays.asList("2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05",
            "2024-01-06", "2024-01-07", "2024-01-08", "2024-01-09");
    List<String> actual = model.getCrossOver("GOOG", "2024-01-02",
            "2024-01-09", 1);
    assertEquals(expected, actual);
  }

  @Test
  public void testCrossoverOnWeekend() {
    List<String> expected = Arrays.asList("2024-01-05", "2024-01-06", "2024-01-07");
    List<String> actual = model.getCrossOver("GOOG", "2024-01-05",
            "2024-01-07", 2);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateNewPortfolio() {
    String portfolioName = "OOD";
    model.createPortfolio(portfolioName, "GOOG", "5");

    List<String> portfolioNames = model.getPortfolioNames();
    assertTrue(portfolioNames.contains(portfolioName));
  }

  @Test
  public void testAddStocksToNewPortfolio() {
    String portfolioName = "New";
    model.createPortfolio(portfolioName, "GOOG", "5");
    model.createPortfolio(portfolioName, "AAPL", "7");
    model.createPortfolio(portfolioName, "WMT", "3");


    model.portfolioValue(portfolioName, "2024-01-05");
    assertEquals(510.0, model.getPortfolioValue(), 0.001);
  }

  @Test
  public void testAddStocksToExistingPortfolio() {
    String portfolioName = "Adding";
    model.createPortfolio(portfolioName, "GOOG", "4");

    model.createPortfolio(portfolioName, "AAPL", "24");

    model.portfolioValue(portfolioName, "2024-01-05");
    assertEquals(408.0, model.getPortfolioValue(), 0.001);
  }

  @Test
  public void testCreatePortfoliosMultiple() {
    String portfolioName1 = "Darshini";
    String portfolioName2 = "Pradhu";

    model.createPortfolio(portfolioName1, "GOOG", "7");
    model.createPortfolio(portfolioName2, "AAPL", "5");

    List<String> portfolioNames = model.getPortfolioNames();
    assertEquals(Arrays.asList(portfolioName1, portfolioName2), portfolioNames);
  }

  @Test
  public void testPortfolioValueCalc() {
    String portfolioName = "Calc";
    model.createPortfolio(portfolioName, "GOOG", "2");
    model.createPortfolio(portfolioName, "AAPL", "3");

    model.portfolioValue(portfolioName, "2024-01-05");
    assertEquals(204.0, model.getPortfolioValue(), 0.001);
  }


  @Test
  public void testPortfolioWithDiffStocks() {
    String portfolioName = "DiffStocks";
    model.createPortfolio(portfolioName, "GOOG", "4");
    model.createPortfolio(portfolioName, "AAPL", "7");
    model.createPortfolio(portfolioName, "WMT", "3");

    model.portfolioValue(portfolioName, "2024-01-05");
    assertEquals(408.0, model.getPortfolioValue(), 0.001);
  }
}
