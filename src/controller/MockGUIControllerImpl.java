package controller;

import java.util.HashMap;
import java.util.Map;

import model.BetterIModel;
import view.BetterIView;
import view.GUIView;

/**
 * Mock class of the GUI controller implementation to test and ensure that all the
 * methods and functionality of the new controller works as it should. This mock is made for testing
 * purposes (works like a copy or replacement for the original GUIControllerImpl). It
 * implements GUIController and makes sure the user does not have access to the parts of the
 * program that they shouldn't have.
 */

public class MockGUIControllerImpl implements GUIController {
  private final Map<String, Object> executeMethod = new HashMap<>();
  private final GUIView view;

  /**
   * This is the constructor that initializes the objects of the BetterIModel and GUIView.
   *
   * @param model BetterIModel object used in the mock class.
   * @param view  GUIView object used in the mock class.
   */

  public MockGUIControllerImpl(BetterIModel model, GUIView view) {
    this.view = view;
  }

  @Override
  public void portfolioSet() {
    executeMethod.put("portfolioSet", true);
  }

  @Override
  public Map<String, Double> portfolioComposition(String portName, String date) {
    executeMethod.put("portfolioComposition", new Object[]{portName, date});
    Map<String, Double> mockComp = new HashMap<>();
    mockComp.put("GOOG", 50.0);
    mockComp.put("WMT", 50.0);
    return mockComp;
  }

  @Override
  public double portfolioValue(String portName, String date) {
    executeMethod.put("portfolioValue", new Object[]{portName, date});
    return 1500.0;
  }

  @Override
  public void execute(BetterIModel model, BetterIView view) {
    executeMethod.put("execute", true);
  }

  /**
   * This is the method that allows a portfolio to be saved when the save button is clicked.
   */

  public void handleSavePortfolioAction() {
    String portName = view.getSelectedPortfolio();
    if (portName == null || portName.trim().isEmpty()) {
      throw new IllegalArgumentException("Portfolio name cannot be empty.");
    }
    executeMethod.put("handleSavePortfolioAction", portName);
  }

  /**
   * This is the method that allows for a certain portfolio to be loaded when the load button is
   * clicked.
   */

  public void handleLoadPortfolioAction() {
    String filePath = view.getInput();
    if (filePath == null || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("File name cannot be empty.");
    }
    executeMethod.put("handleLoadPortfolioAction", filePath);
  }

  /**
   * This is the method that allows for a portfolio to be create when the create button is
   * clicked.
   */

  public void handleCreatePortfolioAction() {
    String portName = view.getInput();
    if (portName == null || portName.trim().isEmpty()) {
      throw new IllegalArgumentException("Portfolio name cannot be empty.");
    }
    executeMethod.put("handleCreatePortfolioAction", portName);
  }

  /**
   * This is the method that allows for stocks to be sold when the user wants to.
   */

  public void handleSellAction() {
    String portName = view.getSelectedPortfolio();
    String tickerSymbol = view.getTickerSellInput();
    String shareInput = view.getShareSellInput();
    String date = view.getDateInput();
    if (portName == null || portName.trim().isEmpty() || tickerSymbol == null
            || tickerSymbol.trim().isEmpty()
            || shareInput == null || shareInput.trim().isEmpty() || date == null
            || date.trim().isEmpty()) {
      throw new IllegalArgumentException("All fields must be filled out to sell shares.");
    }
    executeMethod.put("handleSellAction", new Object[]{portName, tickerSymbol, shareInput, date});
  }

  /**
   * This is the method that allows for stocks to be bought in a certain portfolio when the user
   * wants to.
   */

  public void handleBuyAction() {
    String portName = view.getSelectedPortfolio();
    String tickerSymbol = view.getTickerInput();
    String shareInput = view.getShareInput();
    String date = view.getDateInput();
    if (portName == null || portName.trim().isEmpty() || tickerSymbol == null
            || tickerSymbol.trim().isEmpty() ||
            shareInput == null || shareInput.trim().isEmpty() || date == null
            || date.trim().isEmpty()) {
      throw new IllegalArgumentException("All fields must be filled out to buy shares.");
    }
    executeMethod.put("handleBuyAction", new Object[]{portName, tickerSymbol, shareInput, date});
  }

  public Map<String, Object> getExecuteMethod() {
    return executeMethod;
  }
}
