package controller.commands;

import java.io.IOException;

import model.BetterIModel;
import view.BetterIView;

/**
 * ICommand interface that represents a command. It has a method to execute the command,
 * using the model and view components of the program.
 */

public interface ICommand {

  /**
   * Runs the command with the model and view.
   *
   * @param model represents the model component of the program.
   * @param view  represents the view component of the program.
   */

  void run(BetterIModel model, BetterIView view) throws IOException;

}
