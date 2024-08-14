package view;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * This is created to implement the new method that takes the date in a more separated
 * format in order to make it easier for the user.
 */

public class BetterViewImpl implements BetterIView {
  private final PrintStream out;
  private final Scanner scanner;
  private final StringBuilder log;

  /**
   * This initializes all the parameters taken in.
   *
   * @param out     a PrintStream object.
   * @param scanner a Scanner object.
   * @param log     a StringBuilder object.
   */

  public BetterViewImpl(PrintStream out, Scanner scanner, StringBuilder log) {
    this.out = out;
    this.scanner = scanner;
    this.log = log;
  }

  @Override
  public void writeMessage(String message) {
    out.println(message);
    log.append(message).append(System.lineSeparator());
  }

  @Override
  public String getInput() {
    String input = scanner.nextLine();
    log.append(input).append(System.lineSeparator());
    return input;
  }

  @Override
  public String getDateInput() {
    writeMessage("Enter the year (yyyy): ");
    String year = getInput();
    writeMessage("Enter the month (mm): ");
    String month = getInput();
    writeMessage("Enter the day (dd): ");
    String day = getInput();
    return year + "-" + String.format("%02d", Integer.parseInt(month)) + "-"
            + String.format("%02d", Integer.parseInt(day));
  }


}
