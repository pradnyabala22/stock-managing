package controller;

import java.io.IOException;

import model.BetterIModel;
import view.BetterIView;

/**
 * IController interface is the controller of the program. This connects
 * both the model and view together and tells the model what
 * methods to implement and the view what to display to the user.
 */
public interface IController {
  /**
   * For the controller- it starts the controller so that it can conduct interactions between
   * the model and the view.
   *
   * @param model represents the model component of the program.
   * @param view  represents the view component of the program.
   */
  void execute(BetterIModel model, BetterIView view) throws IOException;
}

