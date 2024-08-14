package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This represents the model implementation of all the method in the model interface (IModel).
 * This is where all the methods
 */

public class ModelImpl implements IModel {
  protected final Map<String, IStock> stocks;
  /**
   * portfolioList representing an arraylist of generated portfolios.
   */
  protected ArrayList<Portfolio> portfolioList;
  private double portfolioValueCalc;

  /**
   * Constructs a ModelImpl.
   */

  public ModelImpl() {
    this.stocks = new HashMap<>();
    this.portfolioList = new ArrayList<>();
  }

  public void addStock(IStock stock) {
    stocks.put(stock.ticker(), stock);
  }

  @Override
  public boolean doesTickerExist(String ticker) {
    return stocks.containsKey(ticker);
  }

  @Override
  public ArrayList<Portfolio> createPortfolio(String name, String tickerSymbol, String shares) {
    Portfolio portfolio = null;
    for (Portfolio p : portfolioList) {
      if (p.getName().equals(name)) {
        portfolio = p;
        break;
      }
    }
    if (portfolio == null) {
      HashMap<String, Double> portfolioStocks = new HashMap<>();
      portfolioStocks.put(tickerSymbol, Double.parseDouble(shares));
      portfolio = new Portfolio(name, portfolioStocks);
      portfolioList.add(portfolio);
    } else {
      double currentShares = portfolio.getStocks().getOrDefault(tickerSymbol, 0.0);
      portfolio.getStocks().put(tickerSymbol, (int) currentShares + Double.parseDouble(shares));
    }
    return portfolioList;
  }


  @Override
  public void portfolioValue(String portfolioName, String date) {
    double value = 0;
    for (Portfolio portfolio : portfolioList) {
      if (portfolio.getName().equals(portfolioName)) {
        for (Map.Entry<String, Double> stockQuantity : portfolio.getStocks().entrySet()) {
          IStock stock = stocks.get(stockQuantity.getKey());
          if (stock != null) {
            value += stock.closingPrice(date) * stockQuantity.getValue();
          }
        }
      }
    }
    portfolioValueCalc = value;
  }

  @Override
  public double getPortfolioValue() {
    return portfolioValueCalc;
  }

  @Override
  public List<String> getPortfolioNames() {
    List<String> portfolioNames = new ArrayList<>();
    for (Portfolio portfolio : portfolioList) {
      portfolioNames.add(portfolio.getName());
    }
    return portfolioNames;
  }

  @Override
  public double getGainOrLoss(String tickerSymbol, String startDate, String endDate) {
    IStock stock = stocks.get(tickerSymbol);
    if (stock == null) {
      throw new IllegalArgumentException("Ticker symbol does not exist.");
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date startParse;
    Date endParse;
    try {
      startParse = format.parse(startDate);
      endParse = format.parse(endDate);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
    }

    if (startParse.after(endParse)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }

    if (!stock.hasDate(startDate)) {
      throw new IllegalArgumentException("Start date is either holiday or weekend.");
    }

    if (!stock.hasDate(endDate)) {
      throw new IllegalArgumentException("End date is either a holiday or weekend.");
    }

    return stock.gainOrLoss(startDate, endDate);
  }

  @Override
  public double getMovingAverage(String tickerSymbol, String date, int xDays) {
    IStock stock = stocks.get(tickerSymbol);
    if (stock == null) {
      throw new IllegalArgumentException("Ticker symbol does not exist.");
    }
    if (xDays <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than zero.");
    }
    if (!stock.hasDate(date)) {
      throw new IllegalArgumentException("Date is either a holiday or weekend.");
    }
    return stock.movingAverage(date, xDays);
  }

  @Override
  public List<String> getCrossOver(String tickerSymbol,
                                   String startDate, String endDate, int xDays) {
    IStock stock = stocks.get(tickerSymbol);
    if (stock == null) {
      throw new IllegalArgumentException("This stock does not exist in folder.");
    }
    return stock.crossOver(startDate, endDate, xDays);
  }

  //checks for the existence of the CSV file. If it exists, it reads the data from the file.
  //if not, it calls the API to fetch the data, saves it to the file, and then reads the data from
  // the file.
  //Wrote this with TA Omar
  @Override
  public void loadData(String tickerSymbol, String destinationFolder, String apiKey)
          throws IOException {
    Path destinationPath = Paths.get(destinationFolder, tickerSymbol + ".csv");
    if (Files.exists(destinationPath)) {
      loadFromFile(destinationPath.toString());
    } else {
      loadFromAPI(tickerSymbol, destinationFolder, apiKey);
      loadFromFile(destinationPath.toString());
    }
  }

  @Override
  public boolean hasDate(String tickerSymbol, String date) {
    IStock stock = stocks.get(tickerSymbol);
    return stock != null && stock.hasDate(date);
  }

  //Wrote this with TA Omar

  /**
   * Loading data from file.
   *
   * @param filePath represents the file.
   * @throws IOException input/output error.
   */
  public void loadFromFile(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      Map<String, Double> closingPrices = new HashMap<>();
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        if (values.length < 5) {
          throw new IllegalArgumentException("Invalid data format in file.");
        }
        String date = values[0];
        double closingPrice = Double.parseDouble(values[4]);
        closingPrices.put(date, closingPrice);
      }
      IStock stock = new Stock(filePath.substring(filePath.lastIndexOf("/") + 1,
              filePath.indexOf(".csv")), closingPrices);
      addStock(stock);
    } catch (Exception e) {
      // Handle invalid or missing file data
      throw new IllegalArgumentException("This stock does not exist in folder.");
    }
  }

  //gets data from the AlphaVantage API and save it to a CSV file.
  //Wrote this with TA Omar
  private void loadFromAPI(String stockSymbol, String destinationFolder, String apiKey)
          throws IOException {
    URL url;

    try {
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol=" + stockSymbol
              + "&apikey=" + "JTQFLMKHAVWUSUC4"
              + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API doesn't work", e);
    }

    try (InputStream in = url.openStream()) {
      Path destinationPath = Paths.get(destinationFolder, stockSymbol + ".csv");
      Files.createDirectories(destinationPath.getParent());
      Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol, e);
    }
  }


}
