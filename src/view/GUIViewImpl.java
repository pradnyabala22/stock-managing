package view;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import controller.GUIController;

/**
 * GUIViewImpl class that extends JFrame and implements GUIView interface
 * has provides methods to interact with the user through a GUI.
 */
public class GUIViewImpl extends JFrame implements GUIView {

  private JTextArea messageArea;
  private JTextField showDate;
  private JTextField showTicker;
  private JTextField showShare;
  private JTextField tickerSellText;
  private JTextField shareSellText;
  private JComboBox<String> portfolioBox;

  private JButton buyButton;
  private JButton sellButton;
  private JButton loadButton;
  private JButton saveButton;
  private JButton createPortfolioButton;
  private JButton changeDateButton;

  private JTextArea composition;
  private JTextArea value;

  private GUIController controller;

  /**
   * Constructs GUI setup.
   */
  public GUIViewImpl() {
    setTitle("Welcome to Managing Stock Portfolios!");
    setSize(1000, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * This method sets the controller.
   * @param controller represents the GUI controller of program.
   */

  public void setController(GUIController controller) {
    this.controller = controller;
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    add(panel);
    placeComponents(panel);

    setVisible(true);
  }

  //places components, setting the design
  private void placeComponents(JPanel panel) {
    panel.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(4, 4, 10, 10));
    panel.add(inputPanel, BorderLayout.NORTH);

    portfolioBox = new JComboBox<>();
    inputPanel.add(new JLabel("Portfolio name:"));
    portfolioBox.setSelectedItem(1);
    inputPanel.add(portfolioBox);

    inputPanel.add(new JLabel("Date:"));
    showDate = new JTextField("2024-05-31");
    inputPanel.add(showDate);

    changeDateButton = new JButton("Change Date");
    inputPanel.add(changeDateButton);

    inputPanel.add(new JLabel(""));
    inputPanel.add(new JLabel(""));
    inputPanel.add(new JLabel("Ticker Symbol (Buy):"));
    showTicker = new JTextField(9);
    inputPanel.add(showTicker);

    inputPanel.add(new JLabel("Shares of Stock (Buy):"));
    showShare = new JTextField(9);
    inputPanel.add(showShare);

    buyButton = new JButton("Buy Shares");
    inputPanel.add(buyButton);

    inputPanel.add(new JLabel(""));
    inputPanel.add(new JLabel("Ticker Symbol (Sell):"));
    tickerSellText = new JTextField(9);
    inputPanel.add(tickerSellText);

    inputPanel.add(new JLabel("Shares of Stock (Sell):"));
    shareSellText = new JTextField(9);
    inputPanel.add(shareSellText);

    sellButton = new JButton("Sell Shares");
    inputPanel.add(sellButton);

    inputPanel.add(new JLabel("Composition:"));
    composition = new JTextArea();
    composition.setEditable(false);
    inputPanel.add(composition);

    inputPanel.add(new JLabel("Value: "));
    value = new JTextArea();
    value.setEditable(false);
    inputPanel.add(value);

    JPanel portfolioPanel = new JPanel();
    portfolioPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    panel.add(portfolioPanel, BorderLayout.SOUTH);

    loadButton = new JButton("Load Portfolio");
    portfolioPanel.add(loadButton);

    saveButton = new JButton("Save Portfolio");
    portfolioPanel.add(saveButton);

    createPortfolioButton = new JButton("Create Portfolio");
    portfolioPanel.add(createPortfolioButton);

    messageArea = new JTextArea(5, 30);
    messageArea.setEditable(false);
    JScrollPane messageScrollPane = new JScrollPane(messageArea);
    panel.add(messageScrollPane, BorderLayout.CENTER);
  }

  @Override
  public void writeMessage(String message) {
    messageArea.append(message + "\n");
  }

  @Override
  public String getInput() {
    return null;
  }

  @Override
  public String getDateInput() {
    return showDate.getText();
  }

  @Override
  public String getTickerInput() {
    return showTicker.getText();
  }

  @Override
  public String getShareInput() {
    return showShare.getText();
  }

  @Override
  public String getTickerSellInput() {
    return tickerSellText.getText();
  }

  @Override
  public String getShareSellInput() {
    return shareSellText.getText();
  }

  @Override
  public void setCompositionPortfolio() {
    Map<String, Double> comp = controller.portfolioComposition(getSelectedPortfolio(),
            getDateInput());
    comp.remove("");
    StringBuilder compString = new StringBuilder();
    for (String key : comp.keySet()) {
      compString.append(key).append(": ").append(comp.get(key)).append(" shares\n");
    }
    composition.setText(compString.toString());
  }

  @Override
  public void setValuePortfolio() throws IOException {
    value.setText(Double.toString(controller.portfolioValue(getSelectedPortfolio(),
            getDateInput())));
  }

  @Override
  public String getSelectedPortfolio() {
    String portfolioName = (String) portfolioBox.getSelectedItem();
    if (portfolioName == null) {
      portfolioName = "";
    }
    return portfolioName;
  }

  @Override
  public void addChangeDateListener(ActionListener listener) {
    changeDateButton.addActionListener(listener);
  }

  @Override
  public void addBuyListener(ActionListener listener) {
    buyButton.addActionListener(listener);
  }

  @Override
  public void addSellListener(ActionListener listener) {
    sellButton.addActionListener(listener);
  }

  @Override
  public void addCreatePortfolioListener(ActionListener listener) {
    createPortfolioButton.addActionListener(listener);
  }

  @Override
  public void addLoadPortfolioListener(ActionListener listener) {
    loadButton.addActionListener(listener);
  }

  @Override
  public void addSavePortfolioListener(ActionListener listener) {
    saveButton.addActionListener(listener);
  }

  @Override
  public void addPortfolioSelectionListener(ActionListener listener) {
    portfolioBox.addActionListener(listener);
  }

  @Override
  public void portfolioSet(List<String> portfolioNames) {
    portfolioBox.removeAllItems();
    for (String portfolioName : portfolioNames) {
      portfolioBox.addItem(portfolioName);
    }
  }

  @Override
  public void setSelectedPortfolio(String portfolioName) {
    portfolioBox.setSelectedItem(portfolioName);
  }
}
