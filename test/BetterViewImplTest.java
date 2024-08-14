import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import view.BetterIView;
import view.BetterViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * This tests that the better view implementation works as it should. It has the new method that a
 * serves as helper that allows the date to be asked in a year, month, day format separately. This
 * tests that that new method works properly.
 */

public class BetterViewImplTest {
  private ByteArrayOutputStream output;
  private StringBuilder log;
  private BetterIView view;

  @Before
  public void setup() {
    output = new ByteArrayOutputStream();
    log = new StringBuilder();
    PrintStream printStream = new PrintStream(output);
    Scanner scanner = new Scanner(System.in);
    view = new BetterViewImpl(printStream, scanner, log);
  }

  @Test
  public void testWriteMessage() {
    view.writeMessage("Hello.");
    assertEquals("Hello.\n", output.toString());
  }

  @Test
  public void testGetInput() {
    String input = "Input";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("Input", view.getInput());
  }


  @Test
  public void testGetDateInput() {
    String input = "2024\n05\n15\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("2024-05-15", view.getDateInput());
  }

  @Test
  public void testGetDateInputWithLeadingZeros() {
    String input = "2024\n05\n05\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("2024-05-05", view.getDateInput());
  }

  @Test
  public void testGetDateInputInvalidMonth() {
    String input = "2024\n13\n5\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("2024-13-05", view.getDateInput());
  }

  @Test
  public void testGetDateInputInvalidDay() {
    String input = "2024\n02\n30\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("2024-02-30", view.getDateInput());
  }

  @Test
  public void testGetDateInputInvalidYear() {
    String input = "dars\n02\n15\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    view = new BetterViewImpl(new PrintStream(output), new Scanner(inputStream), log);
    assertEquals("dars-02-15", view.getDateInput());
  }

}
