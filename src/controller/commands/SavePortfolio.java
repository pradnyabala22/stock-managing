package controller.commands;

import java.io.IOException;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * SavePortfolio class that implements ICommand and executes the save portfolio command.
 * It saves a portfolio to a specific portfolios folder in res.
 */
public class SavePortfolio implements ICommand {

  /**
   * Constructs a SavePortfolio command with the given view.
   *
   * @param view the view component of the program
   */
  public SavePortfolio(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    view.writeMessage("Enter name of portfolio to save: ");
    String portfolioName = view.getInput().trim();

    if (!portfolioName.isEmpty()) {
      try {
        model.savePortfolio(portfolioName);
        view.writeMessage("Portfolio " + portfolioName + " has been saved.\n");
      } catch (IOException e) {
        view.writeMessage("Couldn't save portfolio: " + e.getMessage() + "\n");
      } catch (IllegalArgumentException e) {
        view.writeMessage(e.getMessage() + "\n");
      }
    } else {
      view.writeMessage("Invalid input.\n");
    }
  }
}
