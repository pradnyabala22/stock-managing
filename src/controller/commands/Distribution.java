package controller.commands;

import java.util.Map;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * Distribution class that implements ICommand and executes the distribution command.
 * It shows the value distribution of a particular portfolio on a specified date.
 */
public class Distribution implements ICommand {

  /**
   * Constructs Distribution command with the given view.
   *
   * @param view represents the view component of the program.
   */
  public Distribution(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    BetterIView betterView = view;

    betterView.writeMessage("Enter name of the portfolio: ");
    String portfolioName = betterView.getInput().trim();

    betterView.writeMessage("Enter the date.");
    String date = betterView.getDateInput();

    try {
      Map<String, Double> distribution = model.getPortfolioValueDistribution(portfolioName,
              date, (stockSymbol, d) -> {
          if (model.doesTickerExist(stockSymbol)) {
            return model.getStockPrice(stockSymbol, d);
          }
          throw new IllegalArgumentException("Stock invalid.");
        }, model.getPortfolios());

      betterView.writeMessage("Distribution of value of portfolio " + portfolioName + " on "
              + date + " is:\n");
      for (Map.Entry<String, Double> entry : distribution.entrySet()) {
        betterView.writeMessage(entry.getKey() + ": $" + String.format("%.2f", entry.getValue())
                + "\n");
      }
    } catch (Exception e) {
      betterView.writeMessage(e.getMessage() + "\n");
    }
  }
}
