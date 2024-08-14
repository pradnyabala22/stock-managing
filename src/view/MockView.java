package view;

/**
 * This MockView is a mock of the original view (IView) interface to help test the
 * controller and practically does the same as the view except it serves as a replacement so
 * accessing this does not give the user access to methods they should not have access to.
 */

public class MockView implements IView {
  private final StringBuilder log;
  private String input;

  /**
   * Makes an MockView object and initializes the log.
   *
   * @param log represents a StringBuilder that holds all the messages.
   */
  public MockView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void writeMessage(String message) {
    log.append("Write message: ").append(message).append("\n");
  }

  @Override
  public String getInput() {
    log.append("Get Input: ").append("q").append('\n');
    return input;
  }

  /**
   * Sets input given by user.
   *
   * @param input represents the input to be set.
   */
  public void setInput(String input) {
    this.input = input;
  }
}

