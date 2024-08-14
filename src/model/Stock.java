package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Stock class that implements IStock that represents a stock, provides methods for commands
 * (gainOrLoss,
 * movingAverage, crossovers) and accesses closing prices and tickerSymbols.
 */

public class Stock implements IStock {
  private final String ticker;
  private final Map<String, Double> closingPrice;

  /**
   * Constructs stock object.
   *
   * @param ticker       represents the ticker symbol for stock.
   * @param closingPrice a map of dates and closing prices.
   */

  public Stock(String ticker, Map<String, Double> closingPrice) {
    this.ticker = ticker;
    this.closingPrice = closingPrice;
  }

  @Override
  public double gainOrLoss(String startDate, String endDate) {
    if (closingPrice.containsKey(startDate) && closingPrice.containsKey(endDate)) {
      return closingPrice.get(endDate) - closingPrice.get(startDate);
    }
    return 0;
  }

  @Override
  public double movingAverage(String date, int xDays) {
    List<String> dates = new ArrayList<>(closingPrice.keySet());
    Collections.sort(dates);
    List<Double> prices = new ArrayList<>();
    for (String d : dates) {
      if (d.compareTo(date) <= 0) {
        prices.add(closingPrice.get(d));
      }
    }

    if (prices.size() < xDays) {
      throw new IllegalArgumentException("Cannot calculate moving average.");
    }

    double sum = 0;
    for (int i = prices.size() - xDays; i < prices.size(); i++) {
      sum += prices.get(i);
    }
    return sum / xDays;
  }

  @Override
  public List<String> crossOver(String startDate, String endDate, int xDays) {
    Date startParse = parsingDate(startDate);
    Date endParse = parsingDate(endDate);

    getDates(startParse, endParse);
    getXDays(xDays);

    List<String> dates = new ArrayList<>(closingPrice.keySet());
    Collections.sort(dates);
    List<String> crossover = new ArrayList<>();

    for (int i = 0; i < dates.size(); i++) {
      String currentDate = dates.get(i);
      Date currentDateParse = parsingDate(currentDate);

      if (!currentDateParse.before(startParse) && !currentDateParse.after(endParse)) {
        if (i >= xDays - 1) {
          if (isCrossover(currentDate, xDays)) {
            crossover.add(currentDate);
          }
        }
      }
    }
    return crossover;
  }

  private Date parsingDate(String dateStr) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return format.parse(dateStr);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Error with input: enter in yyyy-MM-dd format.");
    }
  }

  private void getDates(Date startDate, Date endDate) {
    if (startDate.after(endDate)) {
      throw new IllegalArgumentException("Invalid start date, the start date must be "
              + "before the end date.");
    }
  }

  private void getXDays(int xDays) {
    if (xDays < 1) {
      throw new IllegalArgumentException("The number of xDays must be positive.");
    }
  }

  private boolean isCrossover(String currentDate, int xDays) {
    double currentMovingAverage = movingAverage(currentDate, xDays);
    double currentPrice = closingPrice.get(currentDate);
    return currentPrice >= currentMovingAverage;
  }


  @Override
  public double closingPrice(String date) {
    if (!closingPrice.containsKey(date)) {
      //throw new IllegalArgumentException("Date " + date + "
      // is not available for stock " + ticker);
      return 0;
    }
    return closingPrice.get(date);
  }

  @Override
  public String ticker() {
    return this.ticker;
  }

  @Override
  public boolean hasDate(String date) {
    return closingPrice.containsKey(date);
  }

}
