Part Three: To implement a GUI, we created a new GUIView interface and a GUIViewImpl class that
implements it. We also created a new GUIController and GUIControllerImpl. Within the GUIViewImpl
we created components and placed them accordingly to create a design for our GUI. We added listeners
that allows the controller to handle user interactions. The action listeners ensure that the GUI is
responsive to the user inputs and can connect to the controller to perform the necessary actions. We
also implemented the model appropriately so that the proper functionalities are performed, similarly to
the text-based interface. We also added needed mock implementations for testing reasons.

Part Two: For additional functionality, we added to the model package. We added a BetterIModel
interface and BetterModelImpl class that implements it with new methods for portfolio operations (buying stocks,
selling stocks, composition, values, saving, loading, rebalancing, and evaluating performance. We added a
TypeOfTransaction enum to define the transactions performed with stocks in portfolio (buy stocks and sell
stocks). The Transaction class is used to represent a transaction, specifically buy or sell. The
PriceOfStock interface and the StockPrice class that implements it is used to access stock price. Some
additional methods were added Portfolio for specific operations in relation to portfolios. Multiple command
classes implementing ICommand were also incorporated. Specifically, Buy, Sell, Rebalance, Composition,
Distribution, Value, Performance, StockPerformance, SavePortfolio, and LoadPortfolio. The way the packages are
connected and the overall functionality is consistent with how we outlined it in part one. That is our overall
design thought process and change from the last part of the assignment.

Part One: This project is a Java-based application relating to the creation, management,
and calculations/operations of stocks. To implement this, we followed the Model-View-Controller
design pattern. The clear distinction and separation of the three is evident through the creation
of packages, which concerns data management/calculation (model), the user interface (view) and the
command logic (controller). The components of the model are IModel, ModelImpl, IStock, Stock, Portfolio,
and MockModel. The IModel interface defines the methods for the model, while ModelImpl implements it
(hence the name), meaning it performs operations on the stock data. IStock is an interface representing
a stock specifically, and the stock implements it. A separate Portfolio class is included to aid in
the creation of portfolios and to find values of them, as it represents a collection of stocks.
Finally, the MockModel is a mock implementation of the model for testing. Within the view package
is IView, ViewImpl, and MockView. IView is the interface that defines methods for the view, and
ViewImpl implements it, specifically taking care of the user interface. MockView is a mock
implementation of the view for testing, similar to MockModel. In the controller package, there
is IController and ControllerImpl. IController is the interface that defines methods for the
controller, and ControllerImpl implements it, allowing for a connection and interaction between the
model and view. Within the controller package we also included a commands package, reiterating the
command design pattern. ICommand is the interface for the command pattern implementation. The
CrossOver, GainOrLoss, MovingAverage, and Portfolio classes implement ICommand for different stock
analysis operations specified by the assignment (finding crossovers, seeing if there has been a gain
or loss, calculating moving average, creating a portfolio, and finding the value of the portfolio).
Now putting the whole design together, the user interacts with the user interface (ViewImpl), and the
inputs collected by the View are sent to the Controller. The Controller determines what commands should
be done on the basis of these inputs (ControllerImpl) and if data needs to be manipulated or calculations
need to be done, the Controller seeks methods in the Model.The Model then executes said processes
(ModelImpl) and the Controller receives these results. The controller then passes it back to the View,
which updates the user interface as necessary to display the results. Particularly in terms of the
stock data, the loadData method takes stock data from a CSV file and populates a map of the stocks
with Stock objects and their relative data. API is integrated similarly. This is our overall design of
the project.