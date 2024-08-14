package controller.commands;

import java.util.Objects;
import java.util.Scanner;

import model.BetterIModel;
import view.BetterIView;

/**
 * Buy class that implements ICommand and executes the buy command for a stock.
 * It works with buying stocks in a portfolio.
 */
public class Buy implements ICommand {

  /**
   * Constructs Buy command with given view.
   *
   * @param view represents the view component of the program.
   */
  public Buy(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    Scanner scanner = new Scanner(System.in);

    view.writeMessage("Enter portfolio name: ");
    String portfolioName = view.getInput().trim();
    view.writeMessage("Enter stock symbol: ");
    String stockSymbol = view.getInput().trim();
    view.writeMessage("Enter number of shares: ");
    int shares;
    try {
      shares = Integer.parseInt(view.getInput().trim());
      if (shares <= 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      view.writeMessage("Invalid number of shares. Shares must be a positive integer.\n");
      return;
    }

    view.writeMessage("Enter the date:");
    String date = view.getDateInput();

    try {
      model.buyStock(portfolioName, stockSymbol, shares, date);
      view.writeMessage("Bought " + shares + " shares of " + stockSymbol + " in "
              + portfolioName + " on " + date + ".\n");
    } catch (IllegalArgumentException e) {
      view.writeMessage("Invalid input: " + e.getMessage() + "\n");
    }
  }
}
