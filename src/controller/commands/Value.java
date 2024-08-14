package controller.commands;

import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * Value class that implements ICommand and executes the value command.
 * It shows the value of a particular portfolio on a specified date.
 */
public class Value implements ICommand {

  /**
   * Constructs a Value command with the given view.
   *
   * @param view the view component of the program
   */
  public Value(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    view.writeMessage("Enter name of portfolio: ");
    String portfolioName = view.getInput().trim();

    view.writeMessage("Enter the date.");
    String date = view.getDateInput();

    try {
      double portfolioValue = model.getPortfolioValue(portfolioName, date, (stockSymbol, d) -> {
        if (model.doesTickerExist(stockSymbol)) {
          return model.getStockPrice(stockSymbol, d);
        }
        throw new IllegalArgumentException("Stock invalid.");
      }, model.getPortfolios());
      view.writeMessage("Value of portfolio " + portfolioName + " on " + date + " is: $ "
              + portfolioValue + "\n");
    } catch (Exception e) {
      view.writeMessage(e.getMessage() + "\n");
    }
  }
}
