package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import controller.commands.Buy;
import controller.commands.Composition;
import controller.commands.CrossOver;
import controller.commands.Distribution;
import controller.commands.GainOrLoss;
import controller.commands.ICommand;
import controller.commands.LoadPortfolio;
import controller.commands.MovingAverage;
import controller.commands.Performance;
import controller.commands.PortfolioCommand;
import controller.commands.Rebalance;
import controller.commands.SavePortfolio;
import controller.commands.Sell;
import controller.commands.Value;
import model.BetterIModel;
import view.BetterIView;
import view.IView;

/**
 * ControllerImpl class implements IController and represents the implementations of the
 * methods in the controller interface. This is where it connects the model and view together
 * essentially as allows for different commands to be implemented and do the necessary command
 * based on user input.
 */

public class ControllerImpl implements IController {
  private final Map<String, ICommand> commandMap;

  /**
   * Create ControllerImpl object and takes in an object of IView. The command map is initialized
   * with all the commands that the user can put into the program.
   *
   * @param view this is the view that display what is outputted by the program to the user.
   * @throws NullPointerException this is thrown if the view is null.
   */

  public ControllerImpl(BetterIView view, BetterIModel model) {
    IView view1 = Objects.requireNonNull(view);
    BetterIModel model1 = Objects.requireNonNull(model);
    commandMap = new HashMap<>();
    commandMap.put("1", new GainOrLoss(view));
    commandMap.put("2", new MovingAverage(view));
    commandMap.put("3", new CrossOver(view));
    commandMap.put("4", new PortfolioCommand(view));
    commandMap.put("5", new Buy(view));
    commandMap.put("6", new Sell(view));
    commandMap.put("7", new Composition(view));
    commandMap.put("8", new Value(view));
    commandMap.put("9", new Distribution(view));
    commandMap.put("10", new SavePortfolio(view));
    commandMap.put("11", new LoadPortfolio(view));
    commandMap.put("12", new Rebalance(view));
    commandMap.put("13", new Performance(view));
  }

  @Override
  public void execute(BetterIModel model, BetterIView view) throws IOException {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      displayMenu(view);

      String commands = scanner.nextLine().trim();

      if (commands.equalsIgnoreCase("q")) {
        view.writeMessage("Thank you, you are exiting the program!\n");
        break;
      }

      ICommand commandExecute = commandMap.get(commands.toLowerCase());
      if (commandExecute != null) {
        commandExecute.run(model, view);
      } else {
        view.writeMessage("Invalid command, please input one of the valid command numbers.\n");
      }
    }
  }

  /**
   * The display message for the user.
   *
   * @param view represents the view component of the program.
   */

  private void displayMenu(IView view) {
    view.writeMessage("Welcome, enter one of the following numbers " +
            "for the corresponding commands to begin:\n" +
            " 1. Gain or Loss\n" +
            " 2. Moving Average\n" +
            " 3. Crossover\n" +
            " 4. Portfolio\n" +
            " 5. Buy\n" +
            " 6. Sell\n" +
            " 7. Composition of Portfolio\n" +
            " 8. Value of Portfolio\n" +
            " 9. Distribution of Value of Portfolio\n" +
            " 10. Save Portfolio\n" +
            " 11. Load Portfolio\n" +
            " 12. Rebalance Portfolio\n" +
            " 13. Portfolio Performance\n" +
            " or q to quit the program.");
  }
}
