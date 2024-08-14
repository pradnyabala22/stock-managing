package view;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * ViewImpl class that implements IView and represents the implementations of the methods in it.
 * This is where the methods that allows the user to see the result will be implemented. The view
 * displays all the messages that the user will see depending on their input.
 */
public class ViewImpl implements IView {
  private final PrintStream out;
  private final Scanner in;
  private final StringBuilder log;

  /**
   * Makes a ViewImpl object and initializes the PrintStream, scanner, and
   * StringBuilder object this takes in.
   *
   * @param out output stream where the write messages go to.
   * @param in  input scanner that is used to read the user inputs from.
   * @param log StringBuilder that keeps track of all the messages the program outputs.
   */
  public ViewImpl(PrintStream out, Scanner in, StringBuilder log) {
    this.out = out;
    this.in = in;
    this.log = log;
  }

  @Override
  public String getInput() {
    if (in.hasNextLine()) {
      return in.nextLine();
    }
    return "";
  }

  @Override
  public void writeMessage(String message) {
    log.append(message).append("\n");
    out.println(message);
  }
}
