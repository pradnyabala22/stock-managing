import javax.swing.SwingUtilities;
import controller.ControllerImpl;
import controller.GUIController;
import controller.GUIControllerImpl;
import controller.IController;
import model.BetterIModel;
import model.BetterModelImpl;
import view.BetterIView;
import view.BetterViewImpl;
import view.GUIViewImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This is the main, which is used to run the entire program. It has added private methods to run
 * the text based and GUI interface differently based on what the user wants to run.
 */
public class Main {

  /**
   * This is the constructor where an object of BetterIModel (model) is being initialized and the
   * conditions for text or GUI interface is set.
   * @param args the arguments taken in.
   * @throws IOException this is the exception thrown if certain conditions aren't met.
   */
  public static void main(String[] args) throws IOException {
    BetterIModel model = new BetterModelImpl();

    if (args.length > 0 && args[0].equals("-text")) {
      runTextInterface(model);
    } else {
      runGuiInterface(model);
    }
  }

  /**
   * The method to run the text based interface specifically.
   * @param model the BetterIModel object used.
   * @throws IOException this is the exception thrown if certain conditions aren't met.
   */
  private static void runTextInterface(BetterIModel model) throws IOException {
    BetterIView view = new BetterViewImpl(System.out, new Scanner(System.in), new StringBuilder());
    IController controller = new ControllerImpl(view, model);
    setupModelWithCsv(model);
    controller.execute(model, view);
  }

  /**
   * The method to run the GUI based interface specifically.
   * @param model the BetterIModel object used.
   */
  private static void runGuiInterface(BetterIModel model) {
    SwingUtilities.invokeLater(() -> {
      GUIViewImpl view = new GUIViewImpl();
      GUIController controller = new GUIControllerImpl(model, view);
      try {
        setupModelWithCsv(model);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      view.setController(controller);
      controller.portfolioSet();
      try {
        controller.execute(model, view);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  //handling csv file
  private static void setupModelWithCsv(BetterIModel model) throws IOException {
    List<String> csvTickers = Arrays.asList("AAL", "AAPL", "AMZN", "DIS", "F", "FFOX.V", "GOGO",
            "GOOG", "KO",
            "MCD", "TGT", "TSLA");
    String apiKey = "JTQFLMKHAVWUSUC4";
    String destinationFolder = "res/res/stocks";
    for (String ticker : csvTickers) {
      try {
        model.loadData(ticker, destinationFolder, apiKey);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        promptUserCsv(model, destinationFolder);
      }
    }
  }

  //handing api csv
  private static void promptUserCsv(BetterIModel model, String destinationFolder)
          throws IOException {
    Scanner scanner = new Scanner(System.in);
    Path folderPath = Paths.get(destinationFolder);
    File folder = new File(destinationFolder);

    System.out.println("API limit reached, choose from the available CSV files:");

    String[] listOfFiles = folder.list();
    if (listOfFiles != null && listOfFiles.length > 0) {
      for (String fileName : listOfFiles) {
        if (fileName.toLowerCase().endsWith(".csv")) {
          System.out.println(fileName);
        }
      }

      System.out.print("Enter CSV file you want to load: ");
      String fileName = scanner.nextLine();
      Path filePath = folderPath.resolve(fileName);

      if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
        model.loadFromFile(filePath.toString());
      } else {
        System.out.println("File name is invalid. ");
        promptUserCsv(model, destinationFolder);
      }
    }
  }
}
