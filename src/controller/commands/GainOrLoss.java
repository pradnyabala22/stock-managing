package controller.commands;

import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * GainOrLoss class that implements ICommand and executes the gain or loss command for a
 * given stock. Interacts with model for specific calculations and uses the view to interact
 * with the user.
 */

public class GainOrLoss implements ICommand {

  /**
   * Constructs GainOrLoss command with given view.
   *
   * @param view represents the view component of the program.
   */
  public GainOrLoss(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    String ticker;
    String startDate;
    String endDate;
    String apiKey = "JTQFLMKHAVWUSUC4";
    String destinationFolder = "res/res/stocks";

    view.writeMessage("Input ticker symbol:");
    ticker = view.getInput().trim();

    view.writeMessage("Enter the start date.");
    startDate = view.getDateInput();

    view.writeMessage("Enter the end date.");
    endDate = view.getDateInput();

    if (!ticker.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
      try {
        model.loadData(ticker, destinationFolder, apiKey);
        double gainOrLoss = model.getGainOrLoss(ticker, startDate, endDate);
        view.writeMessage("Gain or Loss for " + ticker
                + " from " + startDate + " to " + endDate
                + " is " + gainOrLoss);
      } catch (IllegalArgumentException e) {
        view.writeMessage(e.getMessage());
      } catch (Exception e) {
        view.writeMessage("Try again.");
      }
    } else {
      view.writeMessage("Invalid input.");
    }
  }
}
