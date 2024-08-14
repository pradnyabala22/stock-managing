package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the mock we made of the GUIView, and it implements the original BetterIModel
 * interface. This is made in order to test the GUIControllerImpl. This serves as a copy of or a
 * replacement of the normal BetterIModel but is made for the purpose of testing so the user does
 * not have direct access parts of the program that they should not have access to.
 */

public class MockGUIView implements GUIView {
  private final List<String> executeMethods = new ArrayList<>();
  private String tickerInput = "GOOG";
  private String dateInput = "2024-05-15";
  private String shareInput = "10";
  private String tickerSellInput = "GOOG";
  private String shareSellInput = "7";
  private String selectedPortfolio = "Darshini";
  private String inputResponse;

  @Override
  public String getDateInput() {
    executeMethods.add("getDateInput");
    return dateInput;
  }

  @Override
  public String getTickerInput() {
    executeMethods.add("getTickerInput");
    return tickerInput;
  }

  @Override
  public String getShareInput() {
    executeMethods.add("getShareInput");
    return shareInput;
  }

  @Override
  public String getTickerSellInput() {
    executeMethods.add("getTickerSellInput");
    return tickerSellInput;
  }

  @Override
  public String getShareSellInput() {
    executeMethods.add("getShareSellInput");
    return shareSellInput;
  }

  @Override
  public String getSelectedPortfolio() {
    executeMethods.add("getSelectedPortfolio");
    return selectedPortfolio;
  }

  @Override
  public void setCompositionPortfolio() {
    executeMethods.add("setCompositionPortfolio");
  }

  @Override
  public void setValuePortfolio() {
    executeMethods.add("setValuePortfolio");
  }

  @Override
  public void addChangeDateListener(ActionListener listener) {
    executeMethods.add("addChangeDateListener");
  }

  @Override
  public void addBuyListener(ActionListener listener) {
    executeMethods.add("addBuyListener");
  }

  @Override
  public void addSellListener(ActionListener listener) {
    executeMethods.add("addSellListener");
  }

  @Override
  public void addCreatePortfolioListener(ActionListener listener) {
    executeMethods.add("addCreatePortfolioListener");
  }

  @Override
  public void addLoadPortfolioListener(ActionListener listener) {
    executeMethods.add("addLoadPortfolioListener");
  }

  @Override
  public void addSavePortfolioListener(ActionListener listener) {
    executeMethods.add("addSavePortfolioListener");
  }

  @Override
  public void addPortfolioSelectionListener(ActionListener listener) {
    executeMethods.add("addPortfolioSelectionListener");
  }

  @Override
  public void portfolioSet(List<String> portNames) {
    executeMethods.add("portfolioSet");
  }

  @Override
  public void writeMessage(String message) {
    executeMethods.add("writeMessage");
  }

  @Override
  public String getInput() {
    return inputResponse;
  }

  public void setInputResponse(String inputResponse) {
    this.inputResponse = inputResponse;
  }

  public List<String> getExecuteMethods() {
    return executeMethods;
  }

  public void setTickerInput(String tickerInput) {
    this.tickerInput = tickerInput;
  }

  public void setShareInput(String shareInput) {
    this.shareInput = shareInput;
  }

  public void setTickerSellInput(String tickerSellInput) {
    this.tickerSellInput = tickerSellInput;
  }

  public void setShareSellInput(String shareSellInput) {
    this.shareSellInput = shareSellInput;
  }

  public void setSelectedPortfolio(String selectedPortfolio) {
    this.selectedPortfolio = selectedPortfolio;
  }

  public void setDateInput(String dateInput) {
    this.dateInput = dateInput;
  }
}
