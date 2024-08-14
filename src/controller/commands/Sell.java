package controller.commands;

import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * Sell class that implements ICommand and executes the sell stock command.
 * It sells shares of a stock from a portfolio on a specified date.
 */
public class Sell implements ICommand {

  /**
   * Constructs a Sell command with the given view.
   *
   * @param view the view component of the program
   */
  public Sell(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    view.writeMessage("Enter portfolio name: ");
    String portfolioName = view.getInput().trim();
    view.writeMessage("Enter stock symbol: ");
    String stockSymbol = view.getInput().trim();
    view.writeMessage("Enter number of shares: ");
    int shares = Integer.parseInt(view.getInput().trim());
    view.writeMessage("Enter the date.");
    String date = view.getDateInput();

    try {
      model.sellStock(portfolioName, stockSymbol, shares, date);
      view.writeMessage("Sold " + shares + " shares of " + stockSymbol + " from "
              + portfolioName + " on " + date + ".\n");
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    }
  }
}
