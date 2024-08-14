package view;

/**
 * IView interface that represents the interface for the view. This is used to display messages and
 * any other output to the user, ensuring that depending on the command input, the right
 * message is shown to the user.
 */
public interface IView {

  /**
   * Displays message to user.
   *
   * @param message represents the message to be displayed.
   */
  void writeMessage(String message);

  /**
   * Gets the input given by the user.
   *
   * @return represents the user input.
   */
  String getInput();

}
