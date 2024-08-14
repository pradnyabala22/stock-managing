package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BetterModelImpl class that extends ModelImpl and implements BetterIModel. Implements additional
 * functionality methods for portfolios.
 */
public class BetterModelImpl extends ModelImpl implements BetterIModel {
  protected ArrayList<Portfolio> listOfPortfolios = portfolioList;

  //TA Ankit, TA Yuhao, and TA Ronak were all big helps in finding our errors for composition,
  //distribution, and performance.
  private void buySellStockHelper(String portfolioName, String stockSymbol, int shares,
                                  String date) {
    if (portfolioName == null || portfolioName.trim().isEmpty()) {
      throw new IllegalArgumentException("You must input a name for the portfolio.");
    }
    if (stockSymbol == null || stockSymbol.trim().isEmpty()) {
      throw new IllegalArgumentException("You must input a ticker symbol to get data from.");
    }
    if (shares <= 0) {
      throw new IllegalArgumentException("Number of shares cannot be negative");
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date parseDate;
    try {
      parseDate = format.parse(date);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format, put it in yyyy-MM-dd format.");
    }

    String[] dateParts = date.split("-");
    if (isANumber(dateParts[0]) || isANumber(dateParts[1]) || isANumber(dateParts[2])) {
      throw new IllegalArgumentException("Year, month, and day must be numeric.");
    }

    if (parseDate.after(new Date())) {
      throw new IllegalArgumentException("Data for this stock cannot be retrieved for "
              + "future dates.");
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parseDate);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
      throw new IllegalArgumentException("You cannot buy or sell stock on weekends.");
    }
  }

  private boolean isANumber(String string) {
    if (string == null || string.isEmpty()) {
      return true;
    }
    for (char c : string.toCharArray()) {
      if (!Character.isDigit(c)) {
        return true;
      }
    }
    return false;
  }

  private boolean isLeapYear(int year) {
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
  }

  @Override
  public void buyStock(String portfolioName, String stockSymbol, int shares, String date) {
    buySellStockHelper(portfolioName, stockSymbol, shares, date);
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    portfolio.buyStock(stockSymbol, shares, date);
  }

  @Override
  public void sellStock(String portfolioName, String stockSymbol, int shares, String date) {
    buySellStockHelper(portfolioName, stockSymbol, shares, date);
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    portfolio.sellStock(stockSymbol, shares, date);
  }

  @Override
  public Map<String, Double> getPortfolioComposition(String portfolioName, String date) {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    return portfolio.getComposition(date, listOfPortfolios);
  }

  @Override
  public double getPortfolioValue(String portfolioName, String date, PriceOfStock stockPrice,
                                  List<Portfolio> listOfPortfolios) throws IOException {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    return portfolio.getValue(date, stockPrice, listOfPortfolios);
  }

  @Override
  public Map<String, Double> getPortfolioValueDistribution(String portfolioName,
                                                           String date, PriceOfStock stockPrice,
                                                           List<Portfolio> listOfPortfolios)
                                                           throws IOException {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    Date parseDate = handleParseAndDates(date);

    String adjustedDate = dayBeforeWeekday(date);

    Map<String, Double> dist = new LinkedHashMap<>();
    Map<String, Double> stocks = portfolio.getStocks();

    if (stocks.isEmpty()) {
      return dist;
    }

    double value = 0.0;
    for (Map.Entry<String, Double> mapStock : stocks.entrySet()) {
      String ticker = mapStock.getKey();
      double shares = mapStock.getValue();
      double price = accessPrice(stockPrice, ticker, adjustedDate);

      double stockValue = shares * price;
      dist.put(ticker, stockValue);
      value += stockValue;
    }

    return portfolio.getValueDistribution(date, stockPrice, listOfPortfolios);
  }

  private Date handleParseAndDates(String date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    try {
      Date parsedDate = format.parse(date);
      if (parsedDate.after(new Date())) {
        throw new IllegalArgumentException("Cannot get value for a future date.");
      }

      Calendar cal = Calendar.getInstance();
      cal.setTime(parsedDate);
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.get(Calendar.DAY_OF_MONTH);

      if (month == 2 && day == 29 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) {
        throw new IllegalArgumentException("Invalid date: " + date + " is not valid"
                + " in a normal year.");
      }

      return parsedDate;
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format or non-existent date.");
    }
  }

  private double accessPrice(PriceOfStock stockPrice, String ticker, String date) {
    double price;
    try {
      price = stockPrice.getPrice(ticker, date);
      if (price == 0.0) {
        throw new IllegalArgumentException("Invalid ticker or unable to get the stock price.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid ticker or unable to get the stock price.");
    }
    return price;
  }

  private String dayBeforeWeekday(String date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    try {
      Date parseDate = format.parse(date);
      calendar.setTime(parseDate);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

      if (dayOfWeek == Calendar.SATURDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
      } else if (dayOfWeek == Calendar.SUNDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, -2);
      }
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format, put it in yyyy-MM-dd format.");
    }
    return format.format(calendar.getTime());
  }


  private Portfolio findOrCreatePortfolio(String portfolioName) {
    Portfolio portfolio = null;
    for (Portfolio port : listOfPortfolios) {
      if (portfolioName.equals(port.getName())) {
        portfolio = port;
      }
    }
    if (portfolio == null) {
      portfolio = new Portfolio(portfolioName, new HashMap<>());
      listOfPortfolios.add(portfolio);
    }
    return portfolio;
  }

  @Override
  public double getStockPrice(String tickerSymbol, String date) throws IOException {
    IStock stock = stocks.get(tickerSymbol);
    if (stock == null) {
      try {
        loadData(tickerSymbol, "res/res/stocks", "JTQFLMKHAVWUSUC4");
        stock = stocks.get(tickerSymbol);
      } catch (IOException e) {
        throw new RuntimeException("Error loading data for " + tickerSymbol, e);
      }
    }
    if (stock == null) {
      throw new IllegalArgumentException("Stock not found for " + tickerSymbol);
    }

    return stock.closingPrice(date);
  }

  @Override
  public void savePortfolio(String portfolioName) throws IOException {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);

    File directory = new File("res/portfolios");
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Unable to create this directory: " + directory);
    }

    String fileName = portfolioName + ".txt";
    File file = new File(directory, fileName);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("Portfolio Name: " + portfolioName + "\n");

      Map<String, Double> stocks = portfolio.getStocks();
      if (stocks.isEmpty()) {
        writer.write("no stocks\n");
      } else {
        for (Map.Entry<String, Double> stock : stocks.entrySet()) {
          writer.write("Ticker Symbol: " + stock.getKey() + ", Shares: "
                  + stock.getValue() + "\n");
        }
      }

      List<Transaction> transactions = portfolio.getTransactions();
      if (transactions.isEmpty()) {
        writer.write("no transactions\n");
      } else {
        for (Transaction transaction : transactions) {
          writer.write("Ticker Symbol: " + transaction.getStockSymbol() + ", Shares: " +
                  transaction.getShares()
                  + ", Date: " + transaction.getDate() + ", Buy or Sell: "
                  + transaction.getType() + "\n");
        }
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid input, try again", e);
    }
  }

  @Override
  public void loadPortfolio(String filePath) throws IOException {
    File file = new File("res/portfolios/" + filePath + ".txt");
    if (!file.exists()) {
      throw new IllegalArgumentException("File not found: " + filePath);
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      String portfolioName = "";
      HashMap<String, Double> stocks = new HashMap<>();
      List<Transaction> ts = new ArrayList<>();

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Portfolio Name: ")) {
          portfolioName = line.substring("Portfolio Name: ".length()).trim();
        } else if (line.startsWith("Ticker Symbol: ")) {
          String[] parts = line.split(",");

          String ticker = parts[0].substring("Ticker Symbol: ".length()).trim();
          String qtyStr = parts[1].substring("Shares: ".length()).trim();
          double quantity = Double.parseDouble(qtyStr);
          stocks.put(ticker, quantity);

          if (parts.length > 2) {
            String date = parts[2].substring("Date: ".length()).trim();
            String type = parts[3].substring("Buy or sell: ".length()).trim();
            TypeOfTransaction transaction = TypeOfTransaction.valueOf(type);
            ts.add(new Transaction(ticker, quantity, date, transaction));
          }
        }
        if (portfolioName.isEmpty()) {
          throw new IOException("Please enter a name.");
        }
      }
      Portfolio portfolio = new Portfolio(portfolioName, stocks);
      for (Transaction trans : ts) {
        portfolio.addTransaction(trans);
      }
      listOfPortfolios.add(portfolio);
    }
  }

  @Override
  public String rebalance(String portfolioName, String date, Map<String, Double> weights,
                          PriceOfStock stockPrice) throws IOException {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    double value = portfolio.getValue(date, stockPrice, listOfPortfolios);

    Map<String, Double> currentDistribution =
            portfolio.getValueDistribution(date, stockPrice, listOfPortfolios);
    StringBuilder howToRebalance = new StringBuilder();
    for (Map.Entry<String, Double> port : weights.entrySet()) {
      String stockSymbol = port.getKey();
      double target = value * port.getValue();
      double current = currentDistribution.getOrDefault(stockSymbol, 0.0);
      double price = stockPrice.getPrice(stockSymbol, date);

      if (current > target) {
        double valMore = current - target;
        double sharesSell = valMore / price;
        if (sharesSell > 0) {
          portfolio.sellStock(stockSymbol, sharesSell, date);
          howToRebalance.append(String.format("Sell %.2f shares of %s to rebalance\n",
                  sharesSell, stockSymbol));
        }
      } else if (current < target) {
        double valLess = target - current;
        double sharesBuy = valLess / price;
        if (sharesBuy > 0) {
          portfolio.buyStock(stockSymbol, sharesBuy, date);
          howToRebalance.append(String.format("Buy %.2f shares of %s to rebalance\n",
                  sharesBuy, stockSymbol));
        }
      }
    }
    return howToRebalance.toString();
  }

  @Override
  public Map<String, Double> portfolioPerformance(String portfolioName, String start, String end,
                                                  String timeInterval) throws IOException {
    Portfolio portfolio = findOrCreatePortfolio(portfolioName);
    Map<String, Double> performance = new LinkedHashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar starting = Calendar.getInstance();
    Calendar ending = Calendar.getInstance();
    try {
      starting.setTime(dateFormat.parse(start));
      ending.setTime(dateFormat.parse(end));
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid format.");
    }

    while (!starting.after(ending)) {
      String date = dateFormat.format(starting.getTime());
      double value = portfolio.getValue(date, new StockPrice(this), listOfPortfolios);
      performance.put(date, value);

      performanceHelper(starting, timeInterval);
    }
    return performance;
  }


  //helper for performance method
  private void performanceHelper(Calendar calendar, String interval) {
    switch (interval.toLowerCase()) {
      case "daily":
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        break;
      case "monthly":
        calendar.add(Calendar.MONTH, 1);
        break;
      case "yearly":
        calendar.add(Calendar.YEAR, 1);
        break;
      default:
        throw new IllegalArgumentException("Invalid interval: " + interval);
    }
  }

  @Override
  public List<Portfolio> getPortfolios() {
    return new ArrayList<>(listOfPortfolios);
  }

}



