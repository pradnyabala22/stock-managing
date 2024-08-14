package controller.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.BetterIModel;
import model.StockPrice;
import view.BetterIView;

/**
 * Rebalance class that implements ICommand and executes the rebalance command.
 * It rebalances a portfolio based on target weights for stocks on a specified date.
 */
public class Rebalance implements ICommand {

  /**
   * Constructs a Rebalance command with the given view.
   *
   * @param view the view component of the program
   */
  public Rebalance(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    view.writeMessage("Enter name of portfolio to rebalance: ");
    String portfolioName = view.getInput().trim();

    view.writeMessage("Enter the date.");
    String date = view.getDateInput();

    Map<String, Double> weights = getWeights(view);
    if (weights == null) {
      return;
    }

    double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
    if (totalWeight != 1.0) {
      view.writeMessage("Total weights must sum up to 100%, try different values.\n");
      return;
    }

    try {
      String howToBalance = model.rebalance(portfolioName, date, weights, new StockPrice(model));
      view.writeMessage("Portfolio " + portfolioName + " has been rebalanced on " + date + ".\n");
      view.writeMessage(howToBalance);
    } catch (Exception e) {
      view.writeMessage(e.getMessage() + "\n");
    }
  }

  //handles input regarding weights fot stocks.
  private Map<String, Double> getWeights(BetterIView view) {
    Map<String, Double> weights = new HashMap<>();
    while (true) {
      view.writeMessage("Enter stock ticker symbol and " +
              "target weight percentage (like AAPL 25) to sum up " +
              "to 100% or type done to end: ");
      String input = view.getInput().trim();
      if (input.equalsIgnoreCase("done")) {
        break;
      }
      String[] parts = input.split(" ");
      if (parts.length != 2) {
        view.writeMessage("Invalid format.\n");
        continue;
      }
      try {
        String stockSymbol = parts[0];
        double weight = Double.parseDouble(parts[1]);
        if (weight < 0 || weight > 100) {
          view.writeMessage("Weight has to be between 0 and 100.\n");
          continue;
        }
        weights.put(stockSymbol, weight / 100);
      } catch (NumberFormatException e) {
        view.writeMessage("Invalid format.\n");
      }
    }
    return weights;
  }
}
