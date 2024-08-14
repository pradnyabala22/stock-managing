package controller.commands;

import java.io.IOException;
import java.util.Objects;

import model.BetterIModel;
import view.BetterIView;

/**
 * LoadPortfolio class that implements ICommand and executes the load portfolio command.
 * This loads a portfolio from a file.
 */
public class LoadPortfolio implements ICommand {

  /**
   * Constructs a LoadPortfolio command with the given view.
   *
   * @param view the view component of the program
   */
  public LoadPortfolio(BetterIView view) {
    BetterIView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    view.writeMessage("Enter file name to load portfolio: ");
    String fileName = view.getInput().trim();

    if (!fileName.isEmpty()) {
      try {
        model.loadPortfolio(fileName); // Implement this method in the model
        view.writeMessage("Portfolio has been loaded from " + fileName + ".\n");
      } catch (IOException e) {
        view.writeMessage("Couldn't load portfolio: " + e.getMessage() + "\n");
      } catch (IllegalArgumentException e) {
        view.writeMessage(e.getMessage() + "\n");
      }
    } else {
      view.writeMessage("Invalid input.\n");
    }
  }
}
