import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.Scanner;

import view.IView;
import view.ViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * This test tests the methods in view implementation which implements the IView interface.
 * It tests it with the real, original version of the view by taking in an input and then displaying
 * an output to the user. These tests ensure that the view works as it should and the user is able
 * to see everything they should be, meaning that the view is handling the input and output
 * properly.
 */

public class ViewImplTest {

  private IView view;
  private StringBuilder log;


  @Before
  public void setup() {
    log = new StringBuilder();
    view = new ViewImpl(new PrintStream(System.out), new Scanner(System.in), log);
  }

  @Test
  public void testWriteMessage() {
    String message = "Input your command:";
    view.writeMessage(message);
    assertEquals("Input your command:\n", log.toString());
  }

  @Test
  public void testWriteMessageMultiple() {
    String message = "Input your command:";
    String message2 = "What is the stock symbol:";
    view.writeMessage(message);
    view.writeMessage(message2);
    assertEquals("Input your command:\nWhat is the stock symbol:\n", log.toString());
  }

  @Test
  public void testWriteMessageEmpty() {
    String message = "";
    view.writeMessage(message);
    assertEquals("\n", log.toString());
  }

  @Test
  public void testGetInput() {
    String input = "Your input:";
    ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
    view = new ViewImpl(new PrintStream(System.out), new Scanner(bais), log);
    assertEquals("Your input:", view.getInput());
  }

  @Test
  public void testGetInputMultiple() {
    String input = "Your input:\nWhat is the stock symbol:";
    ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
    view = new ViewImpl(new PrintStream(System.out), new Scanner(bais), log);
    assertEquals("Your input:", view.getInput());
    assertEquals("What is the stock symbol:", view.getInput());
  }

  @Test
  public void testGetInputEmpty() {
    String input = "";
    ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
    view = new ViewImpl(new PrintStream(System.out), new Scanner(bais), log);
    assertEquals("", view.getInput());
  }

}
