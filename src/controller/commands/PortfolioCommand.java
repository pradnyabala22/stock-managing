package controller.commands;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * PortfolioCommand class that implements ICommand and
 * executes the portfolio commands (creating portfolio and finding
 * value of portfolio) for a given stock. Interacts with model for specific calculations
 * and uses the view to interact with the user.
 */
public class PortfolioCommand implements ICommand {

  /**
   * Constructs PortfolioCommand with given view.
   *
   * @param view represents the view component of the program.
   */
  public PortfolioCommand(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) throws IOException {
    String apiKey = "JTQFLMKHAVWUSUC4";
    String destinationFolder = "res/res/stocks";
    boolean continueGoing = true;

    while (continueGoing) {
      view.writeMessage("Do you want to create a new portfolio? (yes or no): ");
      String answer = view.getInput().trim();

      List<String> portfolioNames;
      if ("yes".equalsIgnoreCase(answer)) {
        view.writeMessage("Enter the name of the new portfolio: ");
        String portfolioName = view.getInput().trim();

        addStocks(model, view, apiKey, destinationFolder, portfolioName);
        view.writeMessage("Portfolio has been created with the given stocks.\n");
      } else {
        portfolioNames = getPortfolioNames(model);
        if (portfolioNames.isEmpty()) {
          view.writeMessage("There are no portfolios existing with this name, " +
                  "please create a new one.");
          continue;
        }

        view.writeMessage("Want to add stocks to an existing portfolio? (yes or no): ");
        answer = view.getInput().trim();

        if ("yes".equalsIgnoreCase(answer)) {
          view.writeMessage("Enter name of the existing portfolio: ");
          String portfolioName = view.getInput().trim();

          addStocks(model, view, apiKey, destinationFolder, portfolioName);
          view.writeMessage("Given stock data has been added.\n");
        } else {
          view.writeMessage("Invalid input.\n");
          continue;
        }
      }

      view.writeMessage("Do you want to check the value of a portfolio? (yes or no): ");
      answer = view.getInput().trim();

      if ("yes".equalsIgnoreCase(answer)) {
        view.writeMessage("Enter the name of the portfolio: ");
        String portfolioName = view.getInput().trim();

        view.writeMessage("Enter the date.");
        String date = view.getDateInput();

        if (!portfolioName.isEmpty() && !date.isEmpty()) {
          double portfolioValue = model.getPortfolioValue(portfolioName, date, (stockSymbol, d) -> {
            if (model.doesTickerExist(stockSymbol)) {
              return model.getStockPrice(stockSymbol, d);
            }
            throw new IllegalArgumentException("Stock invalid.");
          }, model.getPortfolios());
          view.writeMessage("Value of portfolio " + portfolioName + " on " + date
                  + " is: $ " + portfolioValue + "\n");
        } else {
          view.writeMessage("Invalid input.\n");
        }
      }

      portfolioNames = getPortfolioNames(model);
      view.writeMessage("Do you want to create another portfolio " +
              "or add stocks to an existing one? " +
              "(yes or no): " + "Existing portfolio/portfolios: " + portfolioNames);
      answer = view.getInput().trim();
      continueGoing = "yes".equalsIgnoreCase(answer);
    }
    view.writeMessage("Thank you, you are now exiting program!");
  }

  private void addStocks(BetterIModel model, BetterIView view, String apiKey,
                         String destinationFolder, String portfolioName) {
    boolean addingStocks = true;

    while (addingStocks) {
      view.writeMessage("Do you want to add stocks to this portfolio? (yes or no): ");
      String answer = view.getInput().trim();

      if ("yes".equalsIgnoreCase(answer)) {
        view.writeMessage("Enter ticker symbol: ");
        String stockSymbol = view.getInput().trim();

        view.writeMessage("Enter quantity: ");
        String quantity = view.getInput().trim();

        try {
          model.loadData(stockSymbol, destinationFolder, apiKey);
          model.createPortfolio(portfolioName, stockSymbol, quantity);
          view.writeMessage("Given stock data has been added.\n");
        } catch (Exception e) {
          view.writeMessage("Failed to get the stock data for " + stockSymbol + ".\n");
        }
      } else {
        addingStocks = false;
      }
    }
  }

  //gets the portfolio names
  private List<String> getPortfolioNames(BetterIModel model) {
    return model.getPortfolioNames();
  }
}
