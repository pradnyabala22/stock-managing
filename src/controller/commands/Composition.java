package controller.commands;

import java.util.Map;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * Composition class that implements ICommand and executes the composition command.
 * It shows the composition of a particular portfolio on a specific date.
 */
public class Composition implements ICommand {

  /**
   * Constructs Composition command with given view.
   *
   * @param view represents the view component of the program.
   */
  public Composition(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    BetterIView betterView = view;

    betterView.writeMessage("Enter portfolio name: ");
    String portfolioName = betterView.getInput().trim();

    betterView.writeMessage("Enter the date.");
    String date = betterView.getDateInput();

    try {
      Map<String, Double> composition = model.getPortfolioComposition(portfolioName, date);
      if (composition.isEmpty()) {
        betterView.writeMessage("No composition data found for "
                + portfolioName + " on " + date + ".\n");
      } else {
        betterView.writeMessage("Composition of portfolio "
                + portfolioName + " on " + date + ":\n");
        for (Map.Entry<String, Double> entry : composition.entrySet()) {
          betterView.writeMessage(entry.getKey() + ": "
                  + String.format("%.2f", entry.getValue())
                  + " shares\n");
        }
      }
    } catch (IllegalArgumentException e) {
      betterView.writeMessage("Invalid input: " + e.getMessage() + "\n");
    }
  }
}
