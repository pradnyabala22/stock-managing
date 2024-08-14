Here is a list of features of our program that work and are complete. 
1. Loading stock data
   1. Can read stock data from CSV files 
   2. Can get stock data from external API 
   3. Data is retrieved, parsed, and stored appropriately
2. Stock analysis 
   1. Can calculate moving averages for stocks 
   2. Can calculate gains and losses over given period of time
   3. Can perform crossover analysis for stocks over given period of time 
   4. All performed in model and results displayed in view
   5. Can determine performance of stocks over a given timeframe.
3. Managing portfolios 
   1. Can create multiple stock portfolios and add stocks to them 
   2. Can view their portfolios, particularly the list of stocks and their quantities
   3. Can calculate the value of the portfolio at given dates
   4. Can buy and sell stocks within portfolio
   5. Can rebalance portfolios to specified weights on given dates
   6. Can get composition of portfolios on given date, with stocks and shares
   7. Can get value distribution of portfolios, with value of each stock within the
   portfolio on given date
   8. Can determine performance of portfolio over a given timeframe
   9. Can be saved into res/portfolios.
   10. Can be loaded from res/portfolios.
4. User interface 
   1. Users can interact through the user interface managed by the view 
   2. The view properly takes in user inputs and processes it through the controller and
   hence the model as well
5. Testing 
   1. Mock implementations allow for testing logic of the program
6. GUI Implementation (Graphical User Interface)
   1. Ability to create a new portfolio.
   2. Ability to buy/sell stocks by specifying the stock, number of shares, and date.
   3. Ability to query the value and composition of a portfolio at a certain date.
   4. Ability to save portfolios.
   5. Ability to load portfolios from files.
