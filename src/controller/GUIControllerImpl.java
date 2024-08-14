package controller;

import model.BetterIModel;
import model.Portfolio;
import model.PriceOfStock;
import model.StockPrice;
import view.BetterIView;
import view.GUIView;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUIControllerImpl that implements the GUIController interface and manages
 * interactions between the GUIView and the model.
 */
public class GUIControllerImpl implements GUIController {
  private final BetterIModel model;
  private final GUIView view;

  /**
   * Constructs a GUIControllerImpl with the model and view.
   * @param model represents the model of the program.
   * @param view represents the view of the program.
   */
  public GUIControllerImpl(BetterIModel model, GUIView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void execute(BetterIModel model, BetterIView view) {
    listeners();
    portfolioSet();
  }

  //populates portfolio list
  @Override
  public void portfolioSet() {
    List<String> portfolioNames = new ArrayList<>();
    for (Portfolio portfolio : model.getPortfolios()) {
      portfolioNames.add(portfolio.getName());
    }
    view.portfolioSet(portfolioNames);
  }

  //sets up action listeners for various commands/buttons
  private void listeners() {
    view.addBuyListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        buyAction();
        view.setCompositionPortfolio();
        try {
          view.setValuePortfolio();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    view.addSellListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sellAction();
        view.setCompositionPortfolio();
        try {
          view.setValuePortfolio();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    view.addCreatePortfolioListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        createPortfolioAction();
        view.setCompositionPortfolio();
        try {
          view.setValuePortfolio();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    view.addLoadPortfolioListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loadPortfolioAction();
      }
    });

    view.addSavePortfolioListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          savePortfolioAction();
        } catch (IOException ex) {
          view.writeMessage("Error saving portfolio: " + ex.getMessage());
        }
      }
    });

    view.addChangeDateListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          changeDateAction();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    view.addPortfolioSelectionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          portfolioSelection();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });
  }

  //buying stocks action
  private void buyAction() {
    String portName = view.getSelectedPortfolio();
    String tickerSymbol = view.getTickerInput();
    String shareInput = view.getShareInput();
    String date = view.getDateInput();

    if (portName == null || tickerSymbol.isEmpty() || shareInput.isEmpty() || date.isEmpty()) {
      view.writeMessage("All necessary fields must be filled.");
      return;
    }

    try {
      double shares = Double.parseDouble(shareInput);
      if (shares <= 0) {
        view.writeMessage("Number of shares should be more than zero.");
        return;
      }
      model.buyStock(portName, tickerSymbol, (int) shares, date);
      view.writeMessage("Bought " + shares + " shares of " + tickerSymbol + " on " + date
              + " in portfolio " + portName);
    } catch (NumberFormatException e) {
      view.writeMessage("Invalid share number.");
    } catch (Exception e) {
      view.writeMessage("Error buying stock: " + e.getMessage());
    }
  }

  //selling stocks action
  private void sellAction() {
    String portName = view.getSelectedPortfolio();
    String tickerSymbol = view.getTickerSellInput();
    String shareInput = view.getShareSellInput();
    String date = view.getDateInput();

    if (portName == null || tickerSymbol.isEmpty() || shareInput.isEmpty() || date.isEmpty()) {
      view.writeMessage("All fields must be filled.");
      return;
    }

    try {
      double shares = Double.parseDouble(shareInput);
      if (shares <= 0) {
        view.writeMessage("Number of shares should be more than zero.");
        return;
      }

      Map<String, Double> composition = model.getPortfolioComposition(portName, date);
      double leftShares = composition.getOrDefault(tickerSymbol, 0.0);
      if (leftShares < shares) {
        view.writeMessage("Not enough shares to sell. Shares available: "
                + leftShares + ", requested shares: " + shares);
        return;
      }

      model.sellStock(portName, tickerSymbol, (int) shares, date);
      view.writeMessage("Sold " + shares + " shares of " + tickerSymbol + " on " + date
              + " in portfolio " + portName);
    } catch (NumberFormatException e) {
      view.writeMessage("Invalid share number.");
    } catch (Exception e) {
      view.writeMessage("Error selling stock: " + e.getMessage());
    }
  }

  //creating portfolio action
  private void createPortfolioAction() {
    String portName = JOptionPane.showInputDialog(null,
            "Enter name of the new portfolio:");
    if (portName != null && !portName.trim().isEmpty()) {
      try {
        model.createPortfolio(portName, "", "0");
        view.writeMessage("Created new portfolio: " + portName);
        portfolioSet();
        view.setSelectedPortfolio(portName);

        int addStock = JOptionPane.showConfirmDialog(null,
                "Want to add a stock to this portfolio?",
                "Add Initial Stock", JOptionPane.YES_NO_OPTION);

        if (addStock == JOptionPane.YES_OPTION) {
          String startTicker = JOptionPane.showInputDialog(
                  null, "Enter the ticker symbol of the stock you want to add: ");
          String startQuantity = JOptionPane.showInputDialog(
                  null, "Enter the quantity of the stock you want to start with: ");
          String startDate = JOptionPane.showInputDialog(
                  null, "Enter date (yyyy-MM-dd): ");

          if (startTicker != null && startQuantity != null && startDate != null &&
                  !startTicker.trim().isEmpty() && !startQuantity.trim().isEmpty()
                  && !startDate.trim().isEmpty()) {
            try {
              double initialQuantity = Double.parseDouble(startQuantity);
              model.buyStock(portName, startTicker, (int) initialQuantity, startDate);
              view.writeMessage("Added initial stock: " + startTicker + " with quantity: "
                      + initialQuantity);
            } catch (NumberFormatException e) {
              view.writeMessage("Invalid quantity.");
            } catch (Exception e) {
              view.writeMessage("Error adding the stock: " + e.getMessage());
            }
          }
        }

        view.setCompositionPortfolio();
        view.setValuePortfolio();
      } catch (Exception e) {
        view.writeMessage("Error creating portfolio: " + e.getMessage());
      }
    } else {
      view.writeMessage("Portfolio name empty.");
    }
  }

  //loading portfolio action
  private void loadPortfolioAction() {
    String fileName = JOptionPane.showInputDialog(null,
            "Enter the name of the portfolio file you would like to load:");
    if (fileName != null && !fileName.trim().isEmpty()) {
      try {
        model.loadPortfolio(fileName);
        view.writeMessage("Loaded portfolio from file: " + fileName);
        portfolioSet();
      } catch (IOException e) {
        view.writeMessage("Error loading portfolio: " + e.getMessage());
      }
    } else {
      view.writeMessage("File name e,pty.");
    }
  }

  private void savePortfolioAction() throws IOException {
    String portName = view.getSelectedPortfolio();
    if (portName != null && !portName.trim().isEmpty()) {
      model.savePortfolio(portName);
      view.writeMessage("Portfolio " + portName + " has been saved.");
    } else {
      view.writeMessage("Portfolio name empty.");
    }
  }

  //changing the date action
  private void changeDateAction() throws IOException {
    view.setCompositionPortfolio();
    view.setValuePortfolio();
  }

  //selecting portfolio action
  private void portfolioSelection() throws IOException {
    view.setCompositionPortfolio();
    view.setValuePortfolio();
  }

  @Override
  public Map<String, Double> portfolioComposition(String portfolioName, String date) {
    return model.getPortfolioComposition(portfolioName, date);
  }

  @Override
  public double portfolioValue(String portfolioName, String date) throws IOException {
    PriceOfStock stockPrice = new StockPrice(model);
    List<Portfolio> listOfPortfolios = model.getPortfolios();
    return model.getPortfolioValue(portfolioName, date, stockPrice, listOfPortfolios);
  }
}
