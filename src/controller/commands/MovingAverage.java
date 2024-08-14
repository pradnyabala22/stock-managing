package controller.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * MovingAverage class that implements ICommand and executes the moving average command for a
 * given stock. Interacts with model for specific calculations and uses the view to interact
 * with the user.
 */
public class MovingAverage implements ICommand {

  /**
   * Constructs MovingAverage command with given view.
   *
   * @param view represents the view component of the program.
   */
  public MovingAverage(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    while (true) {
      view.writeMessage("Input ticker symbol: ");
      String ticker = view.getInput().trim();
      if (ticker.equalsIgnoreCase("q")) {
        return;
      }

      view.writeMessage("Enter the date.");
      String date = view.getDateInput();
      if (date.equalsIgnoreCase("q")) {
        return;
      }

      view.writeMessage("Enter the period of days you want to check for: ");
      String numDays = view.getInput().trim();
      if (numDays.equalsIgnoreCase("q")) {
        return;
      }

      if (!ticker.isEmpty() && !date.isEmpty() && !numDays.isEmpty()) {
        try {
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          format.setLenient(false);
          format.parse(date);

          String apiKey = "JTQFLMKHAVWUSUC4";
          String destinationFolder = "res/res/stocks";
          model.loadData(ticker, destinationFolder, apiKey);

          if (!model.doesTickerExist(ticker)) {
            throw new IllegalArgumentException("Ticker symbol does not exist.");
          }

          int numDaysInt = Integer.parseInt(numDays);
          double movingAverage = model.getMovingAverage(ticker, date, numDaysInt);
          view.writeMessage(numDaysInt + "-day moving average for " + ticker
                  + " on " + date + " is: "
                  + movingAverage + "\n");
        } catch (NumberFormatException e) {
          view.writeMessage("Invalid format for days.\n");
        } catch (ParseException e) {
          view.writeMessage("Date doesn't exist, you must enter a valid date for this month.\n");
        } catch (IllegalArgumentException e) {
          view.writeMessage(e.getMessage() + "\n");
        } catch (Exception e) {
          view.writeMessage("Error: " + e.getMessage() + "\n");
        }
      } else {
        view.writeMessage("Invalid input.\n");
      }
      break;
    }
  }
}
