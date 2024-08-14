import org.junit.Before;
import org.junit.Test;
import controller.MockGUIControllerImpl;
import model.MockBetterIModel;
import view.MockGUIView;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is the test class for the GUI Controller, and it ensures that all the methods we made in
 * order ot get our GUI controller ot work works as it should. The GUI Controller connects to the
 * view in order to works as it should and these tests ensures that all the methods work as they
 * should be.
 */

public class GUIControllerImplTest {
  private MockBetterIModel model;
  private MockGUIView view;
  private MockGUIControllerImpl controller;

  @Before
  public void setup() {
    model = new MockBetterIModel();
    view = new MockGUIView();
    controller = new MockGUIControllerImpl(model, view);
  }

  @Test
  public void testPortfolioSet() {
    controller.portfolioSet();
    assertTrue(controller.getExecuteMethod().containsKey("portfolioSet"));
  }

  @Test
  public void testHandlePortfolioComposition() {
    Map<String, Double> composition =
            controller.portfolioComposition("Darshini", "2024-05-15");
    assertEquals(50.0, composition.get("GOOG"), 0.01);
    assertEquals(50.0, composition.get("WMT"), 0.01);
    assertTrue(controller.getExecuteMethod().containsKey("portfolioComposition"));
  }

  @Test
  public void testHandlePortfolioValue() {
    double value = controller.portfolioValue("Darshini", "2024-05-15");
    assertEquals(1500.0, value, 0.01);
    assertTrue(controller.getExecuteMethod().containsKey("portfolioValue"));
  }

  @Test
  public void testExecute() {
    controller.execute(model, view);
    assertTrue(controller.getExecuteMethod().containsKey("execute"));
  }

  @Test
  public void testBuyStock() {
    view.setTickerInput("GOOG");
    view.setShareInput("20");
    view.setSelectedPortfolio("Darshini");
    controller.handleBuyAction();

    assertTrue(controller.getExecuteMethod().containsKey("handleBuyAction"));
    Object[] buyArgs = (Object[]) controller.getExecuteMethod().get("handleBuyAction");
    assertEquals("Darshini", buyArgs[0]);
    assertEquals("GOOG", buyArgs[1]);
    assertEquals("20", buyArgs[2]);
    assertEquals("2024-05-15", buyArgs[3]);

    assertTrue(model.getExecuteMethod().contains("buyStock: Darshini, GOOG, 20, 2024-05-15"));
    assertTrue(view.getExecuteMethods().contains("setCompositionPortfolio"));
    assertTrue(view.getExecuteMethods().contains("setValuePortfolio"));
  }

  @Test
  public void testSellStock() {
    view.setTickerSellInput("GOOG");
    view.setShareSellInput("10");
    view.setSelectedPortfolio("Darshini");
    controller.handleSellAction();

    assertTrue(controller.getExecuteMethod().containsKey("handleSellAction"));
    Object[] sellArgs = (Object[]) controller.getExecuteMethod().get("handleSellAction");
    assertEquals("Darshini", sellArgs[0]);
    assertEquals("GOOG", sellArgs[1]);
    assertEquals("10", sellArgs[2]);
    assertEquals("2024-05-15", sellArgs[3]);

    assertTrue(model.getExecuteMethod().contains("sellStock: Darshini, GOOG, 10, 2024-05-15"));
    assertTrue(view.getExecuteMethods().contains("setCompositionPortfolio"));
    assertTrue(view.getExecuteMethods().contains("setValuePortfolio"));
  }

  @Test
  public void testCreatePortfolio() {
    view.setInputResponse("Pradhu");
    controller.handleCreatePortfolioAction();
    assertTrue(controller.getExecuteMethod().containsKey("handleCreatePortfolioAction"));
    String portName = (String) controller.getExecuteMethod().get("handleCreatePortfolioAction");
    assertEquals("Pradhu", portName);
  }

  @Test
  public void testLoadPortfolio() {
    view.setInputResponse("pradhu.txt");
    controller.handleLoadPortfolioAction();
    assertTrue(controller.getExecuteMethod().containsKey("handleLoadPortfolioAction"));
    String filePath = (String) controller.getExecuteMethod().get("handleLoadPortfolioAction");
    assertEquals("pradhu.txt", filePath);
  }

  @Test
  public void testSavePortfolio() {
    view.setSelectedPortfolio("Darshini");
    controller.handleSavePortfolioAction();

    assertTrue(controller.getExecuteMethod().containsKey("handleSavePortfolioAction"));
    String portName = (String) controller.getExecuteMethod().get("handleSavePortfolioAction");
    assertEquals("Darshini", portName);
  }

  @Test
  public void testBuyStockEmpty() {
    view.setTickerInput("");
    view.setShareInput("");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("All fields must be filled out to buy shares.",
              e.getMessage());
    }
  }

  @Test
  public void testSellStockEmpty() {
    view.setTickerSellInput("");
    view.setShareSellInput("");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("All fields must be filled out to sell shares.",
              e.getMessage());
    }
  }

  @Test
  public void testCreatePortfolioEmptyName() {
    String emptyPortfolioName = "";
    view.setInputResponse(emptyPortfolioName);
    try {
      controller.handleCreatePortfolioAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Portfolio name cannot be empty.", e.getMessage());
    }
  }

  @Test
  public void testLoadPortfolioEmptyName() {
    String emptyFileName = "";
    view.setInputResponse(emptyFileName);
    try {
      controller.handleLoadPortfolioAction();
    } catch (IllegalArgumentException e) {
      assertEquals("File name cannot be empty.", e.getMessage());
    }
  }

  @Test
  public void testBuyStockPartial() {
    view.setTickerInput("GOOG");
    view.setShareInput("40.5");
    view.setSelectedPortfolio("Darshini");
    controller.handleBuyAction();

    assertTrue(controller.getExecuteMethod().containsKey("handleBuyAction"));
    Object[] buyArgs = (Object[]) controller.getExecuteMethod().get("handleBuyAction");
    assertEquals("Darshini", buyArgs[0]);
    assertEquals("GOOG", buyArgs[1]);
    assertEquals("40.5", buyArgs[2].toString());
    assertEquals("2024-05-15", buyArgs[3]);

    assertTrue(model.getExecuteMethod().contains("buyStock: Darshini, GOOG, 40.5, 2024-05-15"));
    assertTrue(view.getExecuteMethods().contains("setCompositionPortfolio"));
    assertTrue(view.getExecuteMethods().contains("setValuePortfolio"));
  }

  @Test
  public void testBuyStockNegativeShares() {
    view.setTickerInput("GOOG");
    view.setShareInput("-5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of shares cannot be a number less than zero.", e.getMessage());
    }
  }

  @Test
  public void testSellPartial() {
    view.setTickerSellInput("GOOG");
    view.setShareSellInput("7.75");
    view.setSelectedPortfolio("Darshini");
    controller.handleSellAction();

    assertTrue(controller.getExecuteMethod().containsKey("handleSellAction"));
    Object[] sellArgs = (Object[]) controller.getExecuteMethod().get("handleSellAction");
    assertEquals("Darshini", sellArgs[0]);
    assertEquals("GOOG", sellArgs[1]);
    assertEquals("7.75", sellArgs[2].toString());
    assertEquals("2024-05-15", sellArgs[3]);

    assertTrue(model.getExecuteMethod().contains("sellStock: Darshini, GOOG, 7.75, 2024-05-15"));
    assertTrue(view.getExecuteMethods().contains("setCompositionPortfolio"));
    assertTrue(view.getExecuteMethods().contains("setValuePortfolio"));
  }

  @Test
  public void testSellStockNegativeShares() {
    view.setTickerSellInput("GOOG");
    view.setShareSellInput("-9.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Number of shares cannot be a number less than zero.", e.getMessage());
    }
  }

  @Test
  public void testBuyInvalidShares() {
    view.setTickerInput("GOOG");
    view.setShareInput("invalid");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid number of shares.", e.getMessage());
    }
  }

  @Test
  public void testSellInvalidShares() {
    view.setTickerSellInput("GOOG");
    view.setShareSellInput("invalid");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid number of shares.", e.getMessage());
    }
  }

  @Test
  public void testBuyInvalidDate() {
    view.setDateInput("2023-02-30");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testBuyInvalidYearFuture() {
    view.setDateInput("2029-05-15");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testBuyInvalidDay() {
    view.setDateInput("2024-05-35");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testBuyInvalidMonth() {
    view.setDateInput("2024-14-30");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleBuyAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testSellInvalidDay() {
    view.setDateInput("2024-05-35");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testSellInvalidMonth() {
    view.setDateInput("2024-15-14");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testSellInvalidYearFuture() {
    view.setDateInput("2029-05-15");
    view.setTickerInput("GOOG");
    view.setShareInput("5.5");
    view.setSelectedPortfolio("Darshini");
    try {
      controller.handleSellAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format.", e.getMessage());
    }
  }

  @Test
  public void testLoadPortfolioInvalidFile() {
    view.setInputResponse("none.txt");
    try {
      controller.handleLoadPortfolioAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Error loading portfolio: none.txt does not exist.", e.getMessage());
    }
  }

  @Test
  public void testSavePortfolioInvalidName() {
    view.setSelectedPortfolio("");
    try {
      controller.handleSavePortfolioAction();
    } catch (IllegalArgumentException e) {
      assertEquals("Portfolio name cannot be empty.", e.getMessage());
    }
  }

}
