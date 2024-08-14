package view;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * GUIView interface that extends the BetterIView interface, giving additional methods
 * for the GUI and its management of stock portfolios.
 */
public interface GUIView extends BetterIView {

  /**
   * Gets input of ticker symbol for buying stocks.
   *
   * @return ticker symbol input
   */
  String getTickerInput();

  /**
   * Gets input of share quantity for buying stocks.
   *
   * @return share quantity input
   */
  String getShareInput();

  /**
   * Gets input of ticker symbol for selling stocks.
   *
   * @return ticker symbol input
   */
  String getTickerSellInput();

  /**
   * Gets input of share quantity for selling stocks.
   *
   * @return share quantity input.
   */
  String getShareSellInput();

  /**
   * Gets selected portfolio name from the dropdown.
   *
   * @return the selected portfolio name.
   */
  String getSelectedPortfolio();

  /**
   * Sets the composition of the selected portfolio in the view.
   */
  void setCompositionPortfolio();

  /**
   * Sets value of portfolio in the view.
   *
   * @throws IOException if input/output error occurs.
   */
  void setValuePortfolio() throws IOException;

  /**
   * Adds action listener for change date button.
   *
   * @param listener action listener to be added.
   */
  void addChangeDateListener(ActionListener listener);

  /**
   * Adds action listener for the buy button.
   *
   * @param listener action listener to be added.
   */
  void addBuyListener(ActionListener listener);

  /**
   * Adds action listener for the sell button.
   *
   * @param listener action listener to be added
   */
  void addSellListener(ActionListener listener);

  /**
   * Adds action listener for create portfolio button.
   *
   * @param listener action listener to be added.
   */
  void addCreatePortfolioListener(ActionListener listener);

  /**
   * Adds action listener for load portfolio button.
   *
   * @param listener action listener to be added.
   */
  void addLoadPortfolioListener(ActionListener listener);

  /**
   * Adds action listener for the save portfolio button.
   *
   * @param listener action listener to be added.
   */
  void addSavePortfolioListener(ActionListener listener);

  /**
   * Adds action listener for portfolio selection dropdown.
   *
   * @param listener action listener to be added.
   */
  void addPortfolioSelectionListener(ActionListener listener);

  /**
   * Sets portfolio names in dropdown.
   *
   * @param portfolioNames list of portfolio names to be set.
   */
  void portfolioSet(List<String> portfolioNames);

  /**
   * Sets selected portfolio name in dropdown.
   *
   * @param portfolioName the portfolio name to be selected
   */
  void setSelectedPortfolio(String portfolioName);
}
