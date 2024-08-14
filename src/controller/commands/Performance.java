package controller.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import model.BetterIModel;
import view.BetterIView;
import view.IView;

/**
 * Performance class that implements ICommand and executes the performance command.
 * It shows the performance of a particular portfolio in a specified date range.
 */
public class Performance implements ICommand {

  /**
   * Constructs a Performance command with the given view.
   *
   * @param view the view component of the program
   */
  public Performance(IView view) {
    IView view1 = Objects.requireNonNull(view);
  }

  @Override
  public void run(BetterIModel model, BetterIView view) {
    Scanner scanner = new Scanner(System.in);

    view.writeMessage("Enter the name of the portfolio: ");
    String portfolioName = scanner.nextLine().trim();

    view.writeMessage("Enter the start year: ");
    int startYear = Integer.parseInt(scanner.nextLine().trim());
    view.writeMessage("Enter the start month (1-12): ");
    int startMonth = Integer.parseInt(scanner.nextLine().trim());
    view.writeMessage("Enter the start day: ");
    int startDay = Integer.parseInt(scanner.nextLine().trim());

    view.writeMessage("Enter the end year: ");
    int endYear = Integer.parseInt(scanner.nextLine().trim());
    view.writeMessage("Enter the end month (1-12): ");
    int endMonth = Integer.parseInt(scanner.nextLine().trim());
    view.writeMessage("Enter the end day: ");
    int endDay = Integer.parseInt(scanner.nextLine().trim());

    view.writeMessage("Enter the interval (daily, monthly, yearly): ");
    String timeInterval = scanner.nextLine().trim();

    try {
      String start = String.format("%04d-%02d-%02d", startYear, startMonth, startDay);
      String end = String.format("%04d-%02d-%02d", endYear, endMonth, endDay);

      Map<String, Double> performance = model.portfolioPerformance(portfolioName, start, end,
              timeInterval);
      chart(performance, view);
    } catch (Exception e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    }
  }

  //generates the bar chart
  private void chart(Map<String, Double> performance, IView view) {
    if (performance.isEmpty()) {
      view.writeMessage("No data in range.\n");
      return;
    }

    double maxVal = 0.0;
    for (double value : performance.values()) {
      if (value > maxVal) {
        maxVal = value;
      }
    }

    double scale;
    if (maxVal > 0) {
      scale = maxVal / 50.0;
    } else {
      scale = 1000.0;
    }

    List<String> dates = new ArrayList<>(performance.keySet());
    Collections.sort(dates);

    dates = handleDates(dates, 30);

    view.writeMessage("Performance of portfolio from " + dates.get(0) + " to " +
            dates.get(dates.size() - 1) + "\n");

    for (String date : dates) {
      double value = performance.get(date);
      String asterisksLine = asterisks(value, scale);
      view.writeMessage(String.format("%-10s: %s\n", date, asterisksLine));
    }
    view.writeMessage("Scale: * = " + scale + " dollars\n");
  }

  //appends atericks for bar chart
  private String asterisks(double value, double scale) {
    StringBuilder asterisks = new StringBuilder();
    double remain = value;
    while (remain >= scale) {
      asterisks.append('*');
      remain -= scale;
    }
    return asterisks.toString();
  }

  //takes list of dates and makes sure it is at max num of points
  private List<String> handleDates(List<String> dates, int maxPoints) {
    if (dates.size() <= maxPoints) {
      return dates;
    }
    List<String> listDate = new ArrayList<>();
    int step = dates.size() / maxPoints;
    for (int i = 0; i < dates.size(); i += step) {
      listDate.add(dates.get(i));
    }
    if (!listDate.contains(dates.get(dates.size() - 1))) {
      listDate.add(dates.get(dates.size() - 1));
    }
    return listDate;
  }
}
