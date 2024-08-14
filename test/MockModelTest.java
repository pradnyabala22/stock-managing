import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import controller.ControllerImpl;
import controller.IController;
import controller.commands.Buy;
import controller.commands.Composition;
import controller.commands.CrossOver;
import controller.commands.Distribution;
import controller.commands.GainOrLoss;
import controller.commands.ICommand;
import controller.commands.MovingAverage;
import controller.commands.Performance;
import controller.commands.PortfolioCommand;
import controller.commands.Rebalance;
import controller.commands.Sell;
import controller.commands.Value;
import model.BetterIModel;
import model.BetterModelImpl;
import model.MockModel;
import view.BetterIView;
import view.BetterViewImpl;
import view.MockView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the controller of our program. Ensures that all methods in the controller work.
 * Uses a mock model and mock view to test the controller.
 */
public class MockModelTest {
  private StringBuilder log;
  private BetterIModel mockModel;
  private MockView mockView;
  private ICommand movingAverageCommand;
  private BetterIModel model;
  private BetterIView view;
  private ByteArrayOutputStream output;
  private ICommand portfolioCommand;
  private ICommand crossOverCommand;
  private ICommand gainOrLossCommand;
  private ICommand buyCommand;
  private ICommand sellCommand;
  private ICommand performanceCommand;
  private ICommand compositionCommand;
  private ICommand distributionCommand;
  private ICommand valueCommand;
  private ICommand rebalanceCommand;
  private ICommand saveCommand;

  @Before
  public void setup() {
    log = new StringBuilder();
    mockModel = new MockModel(log);
    mockView = new MockView(log);
    output = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(output);
    Scanner scanner = new Scanner(System.in);
    view = new BetterViewImpl(printStream, scanner, log);
    model = new BetterModelImpl();

    movingAverageCommand = new MovingAverage(view);
    portfolioCommand = new PortfolioCommand(view);
    crossOverCommand = new CrossOver(view);
    gainOrLossCommand = new GainOrLoss(view);
    buyCommand = new Buy(view);
    sellCommand = new Sell(view);
    performanceCommand = new Performance(view);
    compositionCommand = new Composition(view);
    distributionCommand = new Distribution(view);
    valueCommand = new Value(view);
    rebalanceCommand = new Rebalance(view);
  }

  private void runCommandTest(String input, ICommand command, String expectedOutput)
          throws IOException {
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    command.run(model, view);

    assertEquals(expectedOutput.trim(), output.toString().trim());
  }

  @Test
  public void testControllerWithMockModel() throws IOException {
    IController controller = new ControllerImpl((BetterIView) mockView, mockModel);
    mockView.setInput("q");
    controller.execute(mockModel, (BetterIView) mockView);

    assertEquals("Write Message: Welcome, enter one of the following commands: "
            + "1. gainorloss\n2. movingaverage\n3. crossover\n4. portfolio\nor q to quit.\n"
            + "Get Input: q\n"
            + "Write message: You chose to quit, exiting now.\n", log.toString());
  }

  @Test
  public void testGainOrLoss() throws IOException {
    IController controller = new ControllerImpl((BetterIView) mockView, mockModel);
    mockView.setInput("gainorloss");
    mockView.setInput("GOOG");
    mockView.setInput("2024-04-20");
    mockView.setInput("2024-04-19");
    controller.execute(mockModel, (BetterIView) mockView);

    assertEquals("Write Message: Welcome, enter one of the following commands: "
                    + "1. gainorloss\n2. movingaverage\n3. crossover\n"
                    + "4. portfolio\nor q to quit.\n"
                    + "Get Input: gainorloss\n"
                    + "Get Input: GOOG\n"
                    + "Get Input: 2024-04-20\n"
                    + "Get Input: 2024-04-19\n"
                    + "Write message: Gain or loss calc for GOOG from 2024-04-20 to 2024-04-19.\n",
            log.toString());
  }

  @Test
  public void testCalculateGainOrLoss() throws IOException {
    String input = "GOOG\n2024-01-02\n2024-01-09\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    gainOrLossCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Gain or loss for GOOG from 2024-01-02 to 2024-01-09 is: 3.0\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidDateFormatGainOrLoss() throws IOException {
    String input = "GOOG\n2024-13-02\n2024-01-09\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    gainOrLossCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Date doesn't exist, you must enter a valid date for this month.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidTickerSymbolGainOrLoss() throws IOException {
    String input = "INVALID\n2024-01-02\n2024-01-09\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    gainOrLossCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Ticker symbol does not exist.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testStartDateAfterEndDate() throws IOException {
    String input = "GOOG\n2024-01-09\n2024-01-02\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    gainOrLossCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Invalid date range. The start date must be before the end date.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testQuitCommandGainOrLoss() throws IOException {
    String input = "q";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    gainOrLossCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testMovingAverageCalc() throws IOException {
    IController controller = new ControllerImpl((BetterIView) mockView, mockModel);
    mockView.setInput("movingaverage");
    mockView.setInput("GOOG");
    mockView.setInput("2024-04-20");
    mockView.setInput("15");

    controller.execute(mockModel, (BetterIView) mockView);

    assertEquals("Write message: Welcome, enter one of the following commands: "
            + "1. gainorloss\n2. movingaverage\n3. crossover\n4. portfolio\nor q to quit.\n"
            + "Get input: movingaverage\n"
            + "Get input: GOOG\n"
            + "Get input: 2024-04-20\n"
            + "Get input: 15\n"
            + "Write message: Moving average calc GOOG on 2024-04-20 with 15\n", log.toString());
  }

  @Test
  public void testCalculateMovingAverage() throws IOException {
    String input = "GOOG\n2024-01-09\n5\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    movingAverageCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the date (yyyy-MM-dd): \n"
            + "Enter the period of days you want to check for: \n"
            + "5-day moving average for GOOG on 2024-01-09 is: 103.0\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidDateFormatMovingAverage() throws IOException {
    String input = "AAPL\n2024-13-10\n5\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    movingAverageCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the date (yyyy-MM-dd): \n"
            + "Enter the period of days you want to check for: \n"
            + "Date doesn't exist, you must enter a valid date for this month.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidTickerSymbolMovingAverage() throws IOException {
    String input = "INVALID\n2024-01-10\n5\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    movingAverageCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n"
            + "Enter the date (yyyy-MM-dd): \n"
            + "Enter the period of days you want to check for: \n"
            + "Ticker symbol does not exist.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidNumberFormatForDaysMovingAverage() throws IOException {
    String input = "AAPL\n2020-01-10\nabc\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    movingAverageCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n" +
            "Enter the date (yyyy-MM-dd): \n" +
            "Enter the period of days you want to check for: \n" +
            "Invalid format for days.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testQuitCommandMovingAverage() throws IOException {
    String input = "q";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    movingAverageCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testGetCrossOver() throws IOException {
    IController controller = new ControllerImpl((BetterIView) mockView, mockModel);
    mockView.setInput("crossover");
    mockView.setInput("GOOG");
    mockView.setInput("2024-04-20");
    mockView.setInput("15");

    controller.execute(mockModel, (BetterIView) mockView);

    assertEquals("Write message: Welcome, enter one of the following commands: " +
            "1. gainorloss\n2. movingaverage\n3. crossover\n4. portfolio\nor q to quit.\n" +
            "Get input: crossover\n" +
            "Get input: GOOG\n" +
            "Get input: 2024-04-20\n" +
            "Get input: 15\n" +
            "Write message: Crossover GOOG on 2024-04-20 with 15 days\n", log.toString());
  }

  @Test
  public void testValidCrossover() throws IOException {
    String input = "GOOG 2024-01-02 2024-01-09 5 ";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    crossOverCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n" +
            "Enter the start date (yyyy-MM-dd): \n" +
            "Enter the end date (yyyy-MM-dd): \n" +
            "Enter the period of days you want to check for: \n" +
            "Crossovers: 2024-01-08\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidDateFormatCrossover() throws IOException {
    String input = "GOOG 2024-13-02 2024-01-09 5 ";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    crossOverCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n" +
            "Enter the start date (yyyy-MM-dd): \n" +
            "Enter the end date (yyyy-MM-dd): \n" +
            "Enter the period of days you want to check for: \n" +
            "Date doesn't exist, you must enter a valid date for this month.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidTickerSymbolCrossover() throws IOException {
    String input = "InvalidTicker 2024-01-02 2024-01-09 5 ";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    crossOverCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n" +
            "Enter the start date (yyyy-MM-dd): \n" +
            "Enter the end date (yyyy-MM-dd): \n" +
            "Enter the period of days you want to check for: \n" +
            "Ticker symbol does not exist.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidNumberFormatForDaysCrossover() throws IOException {
    String input = "GOOG 2024-01-02 2024-01-09 qrs ";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    crossOverCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n" +
            "Enter the start date (yyyy-MM-dd): \n" +
            "Enter the end date (yyyy-MM-dd): \n" +
            "Enter the period of days you want to check for: \n" +
            "Invalid format for number of days.\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testQuitCommandCrossover() throws IOException {
    String input = "q";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    crossOverCommand.run(model, view);

    String expectedOutput = "Input ticker symbol: \n";

    assertEquals(expectedOutput, output.toString());
  }

  //portfolio tests
  @Test
  public void testPortfolio() throws IOException {
    IController controller = new ControllerImpl((BetterIView) mockView, mockModel);
    mockView.setInput("portfolio");
    mockView.setInput("DarshiniPortfolio");
    mockView.setInput("2024-04-20");

    controller.execute(mockModel, (BetterIView) mockView);

    assertEquals("Write message: Welcome, enter one of the following commands: "
                    + "1. gainorloss\n"
                    + "2. movingaverage\n3. crossover\n4. portfolio\nor q to quit.\n"
                    + "Get input: portfolio\n"
                    + "Get input: DarshiniPortfolio\n"
                    + "Get input: 2024-04-20\n"
                    + "Write message: The value of the portfolio DarshiniPortfolio "
                     + "on 2024-04-20 is: 0.0\n", log.toString());

  }

  @Test
  public void testCreateNewPortfolio() throws IOException {
    String input = "yes NewPortfolio AAPL10NGOOG15 Yes no no";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    portfolioCommand.run(model, view);

    String expectedOutput = "Do you want to create a new portfolio? (yes or no): \n"
            + "Enter the name of the new portfolio: \n"
            + "Do you want to add stocks to this portfolio? (yes or no): \n"
            + "Enter ticker symbol: \n"
            + "Enter quantity: \n"
            + "Do you want to add stocks to this portfolio? (yes or no): \n"
            + "Enter ticker symbol: \n"
            + "Enter quantity: \n"
            + "Given stock data has been added.\n"
            + "Do you want to add stocks to this portfolio? (yes or no): \n"
            + "Do you want to check the value of a portfolio? (yes or no): \n"
            + "Thank you, you are now exiting program!\n";

    assertEquals(expectedOutput, output.toString());

    List<String> portfolioNames = model.getPortfolioNames();
    assertEquals(1, portfolioNames.size());
    assertEquals("NewPortfolio", portfolioNames.get(0));
  }

  @Test
  public void testAddStocksToExistingPortfolio() throws IOException {
    model.createPortfolio("ExistingPortfolio", "AAPL", "10");
    String input = "no no yes ExistingPortfolio GOOG15 no no";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    portfolioCommand.run(model, view);

    String expectedOutput = "Do you want to create a new portfolio? (yes or no): \n"
            + "Want to add stocks to an existing portfolio? (yes or no): \n"
            + "Enter name of the existing portfolio: \n"
            + "Do you want to add stocks to this portfolio? (yes or no): \n"
            + "Enter ticker symbol: \n"
            + "Enter quantity: \n"
            + "Given stock data has been added.\n"
            + "Do you want to add stocks to this portfolio? (yes or no): \n"
            + "Do you want to check the value of a portfolio? (yes or no): \n"
            + "Thank you, you are now exiting program!\n";

    assertEquals(expectedOutput, output.toString());

    List<String> portfolioNames = model.getPortfolioNames();
    assertEquals(1, portfolioNames.size());
    assertEquals("ExistingPortfolio", portfolioNames.get(0));
  }

  @Test
  public void testCheckPortfolioValue() throws IOException {
    model.createPortfolio("TestPortfolio", "AAPL", "10");
    String input = "no no yes TestPortfolio 2020-01-10 no no";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    portfolioCommand.run(model, view);

    String expectedOutput = "Do you want to create a new portfolio? (yes or no): \n"
            + "Want to add stocks to an existing portfolio? (yes or no): \n"
            + "Do you want to check the value of a portfolio? (yes or no): \n"
            + "Enter the name of the portfolio: \n"
            + "Enter the date (YYYY-MM-DD): \n"
            + "Value of portfolio TestPortfolio on 2020-01-10 is: 0.0\n"
            + "Do you want to create another portfolio or add stocks to an existing one? "
            + "(yes or no): Existing portfolio/portfolios: [TestPortfolio]\n"
            + "Thank you, you are now exiting program!\n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidInputForPortfolioValue() throws IOException {
    String input = "yes no";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    view = new BetterViewImpl(new PrintStream(output), scanner, new StringBuilder());

    portfolioCommand.run(model, view);

    String expectedOutput = "Do you want to create a new portfolio? (yes or no): \n"
            + "Invalid input.\n"
            + "Thank you, you are now exiting program!\n";

    assertEquals(expectedOutput, output.toString());
  }

  //Buy
  @Test
  public void testBuyStock() throws IOException {
    String input = "Port\nAAPL\n10\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Successfully bought 10 shares of AAPL on 2024-01-02 in portfolio Port.\n";

    runCommandTest(input, buyCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testInvalidDateFormatBuyStock() throws IOException {
    String input = "Port\nAAPL\n10\n2024-13-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid date format, put it in yyyy-MM-dd format.\n";

    runCommandTest(input, buyCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }


  @Test
  public void testInvalidStockSymbolBuyStock() throws IOException {
    String input = "Port\nINVALID\n10\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid ticker symbol.\n";

    runCommandTest(input, buyCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testNegativeSharesBuyStock() throws IOException {
    String input = "Port\nAAPL\n-5\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Number of shares cannot be negative.\n";

    runCommandTest(input, buyCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testSellStock() throws IOException {
    String input = "Port\nAAPL\n5\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Successfully sold 5 shares of AAPL on 2024-01-02 in portfolio MyPortfolio.\n";

    runCommandTest(input, sellCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testInvalidDateFormatSellStock() throws IOException {
    String input = "Port\nAAPL\n5\n2024-13-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid date format, put it in yyyy-MM-dd format.\n";

    runCommandTest(input, sellCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testNegativeSharesSellStock() throws IOException {
    String input = "Port\nAAPL\n-5\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Number of shares cannot be negative.\n";

    runCommandTest(input, sellCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testWeekendDateSellStock() throws IOException {
    String input = "Port\nAAPL\n10\n2024-01-06\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter stock symbol: \n"
            + "Enter number of shares: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "You cannot buy or sell stock on weekends.\n";

    runCommandTest(input, sellCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  // Value Command Tests
  @Test
  public void testPortfolioValue() throws IOException {
    String input = "Port\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "The value of the portfolio Port on 2024-01-02 is: 0.0\n";

    runCommandTest(input, valueCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testInvalidDateFormatPortfolioValue() throws IOException {
    String input = "Port\n2024-13-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid date format, put it in yyyy-MM-dd format.\n";

    runCommandTest(input, valueCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  // Composition Command Tests
  @Test
  public void testPortfolioComposition() throws IOException {
    String input = "Port\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Portfolio composition on 2024-01-02 for Port:\n";

    runCommandTest(input, compositionCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testInvalidDateFormatPortfolioComposition() throws IOException {
    String input = "Port\n2024-13-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid date format, put it in yyyy-MM-dd format.\n";

    runCommandTest(input, compositionCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  // Distribution
  @Test
  public void testPortfolioDistribution() throws IOException {
    String input = "Port\n2024-01-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Portfolio value distribution on 2024-01-02 for Port:\n";

    runCommandTest(input, distributionCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testInvalidDateFormatPortfolioDistribution() throws IOException {
    String input = "Port\n2024-13-02\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Invalid date format, put it in yyyy-MM-dd format.\n";

    runCommandTest(input, distributionCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  // Rebalance
  @Test
  public void testRebalancePortfolio() throws IOException {
    String input = "Port\n2024-01-02\nAAPL=0.5,GOOG=0.5\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Enter target weights in format Ticker = Weight: \n"
            + "Rebalance actions for MyPortfolio on 2024-01-02:\n";

    runCommandTest(input, rebalanceCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testRebalancePortfolioInvalidWeights() throws IOException {
    String input = "Port\n2024-01-02\nAAPL=0.6,GOOG=0.7\n";
    String expectedOutput = "Enter portfolio name: \n"
            + "Enter date (yyyy-MM-dd): \n"
            + "Enter target weights in format Ticker = Weight: \n"
            + "The sum of target weights must be 1.\n";

    runCommandTest(input, rebalanceCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  // Performance Command Tests
  @Test
  public void testPerformance() throws IOException {
    String input = "Port\n2024-01-01\n2024-02-01\ndaily\n";
    String expectedOutput = "Enter the name of the portfolio: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Enter the interval (daily, monthly, yearly): \n"
            + "Performance of portfolio MyPortfolio from 2024-01-01 to 2024-02-01\n";

    runCommandTest(input, performanceCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testPerformanceInvalidInterval() throws IOException {
    String input = "Port\n2024-01-01\n2024-02-01\nweekly\n";
    String expectedOutput = "Enter the name of the portfolio: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Enter the interval (daily, monthly, yearly): \n"
            + "Invalid interval. Choose between daily, monthly, or yearly.\n";

    runCommandTest(input, performanceCommand, expectedOutput);
    assertEquals(expectedOutput, input);
  }

  @Test
  public void testPerformanceStartDateAfterEndDate() throws IOException {
    String input = "Port\n2024-02-01\n2024-01-01\ndaily\n";
    String expectedOutput = "Enter the name of the portfolio: \n"
            + "Enter the start date (yyyy-MM-dd): \n"
            + "Enter the end date (yyyy-MM-dd): \n"
            + "Enter the interval (daily, monthly, yearly): \n"
            + "Invalid date range. The start date must be before the end date.\n";

    runCommandTest(input, performanceCommand, expectedOutput);
    assertEquals(expectedOutput, input);

  }

}