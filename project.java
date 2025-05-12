
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class project {

    private static List<DataClass1> dataList;

    //3 function for easier file reference
    public static String filepath1() {
        return "C:\\Users\\User\\Desktop\\YEAR 1\\FOP\\sem1 project\\lookup_item.csv";
    }

    public static String filepath2() {
        return "C:\\Users\\User\\Desktop\\YEAR 1\\FOP\\sem1 project\\lookup_premise.csv";
    }

    public static String filepath3() {
        return "C:\\Users\\User\\Desktop\\YEAR 1\\FOP\\sem1 project\\pricecatcher_2023-08.csv";
    }

    public static void main(String[] args) throws CsvException {
        List<String> fileNames = List.of("C:\\Users\\User\\Desktop\\YEAR 1\\FOP\\sem1 project\\lookup_item.csv");
        fileNames.forEach((String fileName) -> {
            List<DataClass1> dataClass1List = null;
            try {
                dataClass1List = DataClass1.readDataFromCsv(fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Scanner scanner = new Scanner(System.in);

            boolean registered = false;
            String username = "";

            while (!registered) {
                System.out.println("Welcome to the Price Tracker System.");
                System.out.println("\nPlease select an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Renew password");
                System.out.println("4. Exit");
                System.out.println();
                System.out.print("Choose your option: ");

                if (scanner.hasNextInt()) {
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            System.out.print("Please enter your username: ");
                            username = scanner.nextLine();

                            System.out.print("Please enter your password: ");
                            String password = scanner.nextLine();

                            System.out.print("Please confirm your password: ");
                            String confirmPassword = scanner.nextLine();

                            System.out.print("Please enter your email: ");
                            String email = scanner.nextLine();

                            System.out.print("Please enter your phone number: ");
                            String phoneNumber = scanner.nextLine();

                            System.out.print("Please enter your district: ");
                            String district = scanner.nextLine();
                            if (!password.equals(confirmPassword)) {
                                System.out.println("Passwords do not match. Please try again.");
                            }
                            registered = true;
                            break;
                        case 2:
                            login();
                            registered = true;
                            break;
                        case 3:
                            renewPassword();
                            break;
                        case 4:
                            System.out.println("Thank you for using the user management system.");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid option. Please select a valid option.");
                    }
                } else if (scanner.hasNext()) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.nextLine();
                } else {
                    System.out.println("No more input. Exiting...");
                    System.exit(0);
                }
            }
            pss:
            if (registered) {
                boolean pssfirstLoop = true;
                while (pssfirstLoop)
                try {

                    System.out.println("\n Welcome to Product Search and Selection \n");
                    System.out.println("1. Import Data");
                    System.out.println("2. Browse by Categories");
                    System.out.println("3. Search for a Product");
                    System.out.println("4. View Shopping Cart");
                    System.out.println("5. Account Setting");
                    System.out.println("6. Exit");
                    System.out.println();
                    System.out.print("Choose your option: ");
                    int pssOption = scanner.nextInt();
                    scanner.nextLine();

                    switch (pssOption) {
                        case 1:
                            fileName = filepath1();
                            try {
                                dataClass1List = DataClass1.readDataFromCsv(fileName);
                                System.out.println("Data has been imported successfully!");
                                break;
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                System.out.println("Error: Data import failed.");
                            }
                            break;
                        case 2:
                            browsebycategory();
                            try {
                                selection3(dataClass1List, username);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (SQLException ex) {
                            }
                            break;
                        case 3:
                            DetailsProduct();
                            try {
                                selection3(dataClass1List, username);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (SQLException ex) {
                            }
                            break;
                        case 4:
                            viewshoppingcart(username);
                            break;
                        case 5:
                            accountSettings();
                            break;
                        case 6:
                            System.out.println("Thank you for using the user management system.");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid option. Please select a valid option.");
                    }
                } catch (IOException | SQLException ex) {
                }
            }
        });
    }

    //selection3 is for the cli selection
    private static void selection3(List<DataClass1> dataClass1List, String username) throws IOException, IOException, IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        boolean pssLoop = true;

        while (pssLoop) {
            System.out.println("\nSelect Actions:");
            System.out.println("1. View item details");
            System.out.println("2. Modify item details");
            System.out.println("3. View top 5 cheapest seller");
            System.out.println("4. View price trend");
            System.out.println("5. Add to shopping cart");
            System.out.println("6. Back to menu");
            System.out.print("\nEnter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    DetailsProduct();
                    break;
                case 2:
                    modifyItem();
                    break;
                case 3:
                    Cheapest();
                    break;
                case 4:
                    priceTrend();
                    break;
                case 5:
                    addshoppingcart(username);
                    break;
                case 6:
                    System.out.println("Returning to the main menu.");
                    return; // Exit this selectio3 method and return to the main loop(pss)
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        sc.close();
    }

    //METHODS
    private static void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();

        // Check if the username and password are valid
        if (isValidLogin(username, password)) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void renewPassword() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your username:");
        String username = scanner.nextLine();

        // Check if the username exists
        if (!isValidUsername(username)) {
            System.out.println("Username does not exist. Please try again.");
            return;
        }

        System.out.print("Please enter your new password:");
        String newPassword = scanner.nextLine();

        System.out.print("Please confirm your new password:");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try again.");
            return;
        }

        // Update the password in the database
        System.out.println("Password renewal successful.");
    }

    private static boolean isValidUsername(String username) {
        // Check if the username exists in the database
        return true;
    }

    private static boolean isValidLogin(String username, String password) {
        // Check if the username and password are valid
        return true;
    }

    public static void browsebycategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a Category :");

        try {
            List<CsvData> csvDataList = readCsvFile(filepath1());

            // Display item groups
            String groupName = "item_group";
            List<String> distinctGroups = DataClass1.getDistinctColumnValues(csvDataList, groupName);

            // Display distinct group of item with numbering
            int groupOption = 1;
            for (String group : distinctGroups) {
                if (group != null && !group.isEmpty()) {
                    System.out.println(groupOption + ". " + group);
                    groupOption++;
                }
            }

            // Choose item group
            System.out.print("\nChoose your group of item: ");
            int itemgroup = scanner.nextInt();
            String selectedGroup = null;

            //use user input to find for the item group
            if (itemgroup >= 1 && itemgroup <= distinctGroups.size()) {
                int currentOption = 1;
                for (String group : distinctGroups) {
                    if (currentOption == itemgroup) {
                        selectedGroup = group;
                        break;
                    }
                    currentOption++;
                }
                System.out.println("\nYou selected: " + selectedGroup);

                // get item categories for the selected group above
                //using method getItemCategories from the DataClass1 to get the item catgory from each group
                List<String> ItemCategory = DataClass1.getItemCategories(csvDataList, selectedGroup);
                // Display item categories with numbering
                int itemCategoryOption = 1;
                for (String itemCategory : ItemCategory) {
                    System.out.println(itemCategoryOption + ". " + itemCategory);
                    itemCategoryOption++;
                }

                // Choose item category
                System.out.print("\nChoose your item category : ");
                int selectedCategory = scanner.nextInt();

                // get user input for item category
                if (selectedCategory >= 1 && selectedCategory <= ItemCategory.size()) {
                    String selectedCategoryValue = ItemCategory.get(selectedCategory - 1);
                    System.out.println("You selected: " + selectedCategoryValue);

                    // display items based on the selected item category
                    DataClass1.displayItemsForCategory(csvDataList, selectedGroup, selectedCategoryValue);
                } else {
                    System.out.println("Invalid option for item!");
                }
            } else {
                System.out.println("Invalid option for item group!");
            }
            System.out.println("");
            System.out.println("Please choose View Item Details prior other actions.");

        } catch (Exception e) {
        }
    }

    private static void accountSettings() {
        String url = "jdbc:mysql://localhost:3306/fop";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS accountsettings ("
                    + "username VARCHAR(255),"
                    + "password VARCHAR(255),"
                    + "email VARCHAR(255),"
                    + "phoneNumber VARCHAR(255),"
                    + "district VARCHAR(255))";

            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
                // Execute the query
                preparedStatement.executeUpdate();
                System.out.println("Table created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        String answer = "YES";
        System.out.print("Do you want to change your details? (YES/NO): ");
        String ans = scanner.nextLine();

        if (ans.equalsIgnoreCase(answer)) {

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.print("Please enter your new username: ");
                String newUName = scanner.nextLine();
                System.out.print("Please enter your new password: ");
                String newPass = scanner.nextLine();
                System.out.print("Please confirm your new password: ");
                String newConfirmPass = scanner.nextLine();
                System.out.print("Please enter your new email: ");
                String newEmail = scanner.nextLine();
                System.out.print("Please enter your new phone number: ");
                String newPNumber = scanner.nextLine();
                System.out.print("Please enter your new district: ");
                String newDistrict = scanner.nextLine();

                if (!newPass.equals(newConfirmPass)) {
                    System.out.println("Passwords do not match. Please try again.");
                    return;
                }

                String insertQuery = "INSERT INTO accountsettings (username,password,email,phoneNumber,district) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    // Set values for the columns
                    preparedStatement.setString(1, newUName);
                    preparedStatement.setString(2, newPass);
                    preparedStatement.setString(3, newEmail);
                    preparedStatement.setString(4, newPNumber);
                    preparedStatement.setString(5, newDistrict);
                    // Execute the query
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data inserted successfully!");
                    } else {
                        System.out.println("Failed to insert data!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Changes have been made successfully.");
        } else {
            System.out.println("Back to main menu.");
        }
        System.out.println("Exit program . Thank you for using this program ! ");
    }

    //view item details
    private static void DetailsProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the item name to search: ");
        String targetItemName = scanner.nextLine();

        //call method filepath1== lookup_item
        String fileName = filepath1();

        try {
            // Call the findItemByFuzzyName method to search for the item
            DataClass1 itemDetails = DataClass1.findItemByFuzzyName(fileName, targetItemName);

            if (itemDetails != null) {
                System.out.println("Item Details for Item Name " + targetItemName + ":");
                System.out.println(itemDetails);
            } else {
                System.out.println("Item with Item Name " + targetItemName + " not found.");
            }
        } catch (CsvException e) {
            e.printStackTrace();
        }
    }

    
    
  
    // a class to represent CSV data
    
    static class CsvData {

        private final List<String> headers = new ArrayList<>();
        private final List<String> values = new ArrayList<>();

        public void addField(String header, String value) {
            headers.add(header);
            values.add(value);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < headers.size(); i++) {
                result.append(headers.get(i)).append(": ").append(values.get(i)).append(", ");
            }
            return result.toString();
        }

        public String getField(String header) {
            int index = headers.indexOf(header);
            if (index != -1 && index < values.size()) {
                return values.get(index);
            }
            return null;
        }
    }  
 private static List<CsvData> readCsvFile(String fileName) {
        List<CsvData> csvDataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            List<String[]> records = csvReader.readAll();

            // Assuming the first row contains column headers
            String[] headers = records.get(0);

            for (int i = 1; i < records.size(); i++) {
                String[] data = records.get(i);
                CsvData csvData = createCsvData(headers, data);
                csvDataList.add(csvData);
            }
        } catch (IOException | CsvException e) {
        }

        return csvDataList;
    }

    private static CsvData createCsvData(String[] headers, String[] data) {
        CsvData csvData = new CsvData();

        for (int i = 0; i < headers.length && i < data.length; i++) {
            String header = headers[i];
            String value = data[i];
            csvData.addField(header, value);
        }
        return csvData;
    }

    
    
    
    
    
    
    public static void modifyItem() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/fop";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            System.out.print("Enter the item code to modify details: ");
            String itemcode = sc.nextLine();

            System.out.print("Enter the new item name: ");//update item name 
            String NewitemName = sc.nextLine();

            System.out.print("Enter the new item unit: ");//update unit 
            String NewitemUnit = sc.nextLine();

            System.out.print("Enter the new item group: ");//update item group
            String Newitemgroup = sc.nextLine();

            System.out.print("Enter the new item category: ");//update item category
            String Newitemcategory = sc.nextLine();

            String updateQuery = "UPDATE lookup_item SET item = ?, unit = ?, item_group = ?, item_category = ? WHERE item_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                // Set new values for column1, column2, and column3
                preparedStatement.setString(1, NewitemName);
                preparedStatement.setString(2, NewitemUnit);
                preparedStatement.setString(3, Newitemgroup);
                preparedStatement.setString(4, Newitemcategory);
                // Set the condition for the update
                preparedStatement.setString(5, itemcode);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data updated successfully!");
                } else {
                    System.out.println("Failed to update data! No matching row found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    
//cheapest
    private static List<String[]> readDataFile(String filePath, int minColumns) {
        List<String[]> dataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line.length >= minColumns) {
                    dataList.add(line);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static void Cheapest() {
        String itemsFilePath = filepath1();
        String premiseFilePath = filepath2();
        String pricesFilePath = filepath3();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the item name: ");
        String itemName = scanner.nextLine();

        findCheapestPrices(itemName, itemsFilePath, pricesFilePath, premiseFilePath);
    }

    private static void findCheapestPrices(String itemName, String itemsFilePath, String pricesFilePath, String premiseFilePath) {
        List<String[]> cheapestPrices = new ArrayList<>();

        List<String[]> itemCodeMap = readDataFile(itemsFilePath, 4);
        List<String[]> premiseDetailsList = readDataFile(premiseFilePath, 6);
        List<String[]> priceList = readDataFile(pricesFilePath, 4);

        for (String[] price : priceList) {
            String itemCode = price[2].trim();
            for (String[] item : itemCodeMap) {
                if (itemName.equals(item[1].trim()) && itemCode.equals(item[0].trim())) {
                    if (!containsPrice(cheapestPrices, price[3])) {
                        cheapestPrices.add(price);
                    }
                }
            }
        }

        cheapestPrices.sort(Comparator.comparingDouble(a -> Double.parseDouble(a[3])));

        if (cheapestPrices.isEmpty()) {
            System.out.println("No prices found for the item.");
        } else {
            String firstItemCode = cheapestPrices.get(0)[2];
            String itemUnit = getItemUnit(firstItemCode, itemCodeMap);

            System.out.println("Top 5 Cheapest Sellers for " + itemName + " (" + itemUnit + ")\n");
            int count = 0;
            char k = 'A';
            for (String[] price : cheapestPrices) {
                if (count >= 5) {
                    break;
                }
                String premiseCode = price[1];
                String premiseAddress = findPremiseAddress(premiseCode, premiseDetailsList);
                System.out.println(count + 1 + ". Retailer " + k + "\n"
                        + "   Premise Code: " + premiseCode + "\n"
                        + "   Price: RM" + price[3] + "\n"
                        + "   Address: " + premiseAddress
                );
                System.out.println();
                k++;
                count++;
            }
        }
    }

    private static boolean containsPrice(List<String[]> cheapestPrices, String priceToCheck) {
        for (String[] price : cheapestPrices) {
            if (price[3].equals(priceToCheck)) {
                return true;
            }
        }
        return false;
    }

    private static String findPremiseAddress(String premiseCode, List<String[]> premiseDetailsList) {
        for (String[] details : premiseDetailsList) {
            if (premiseCode.equals(details[0])) {
                String address = details[2].trim();
                String state = details[4].toUpperCase();
                String district = details[5].toUpperCase();

                return address + ", " + district + ", " + state;
            }
        }
        return "Address not found";
    }

    private static String getItemUnit(String itemCode, List<String[]> itemCodeMap) {
        for (String[] item : itemCodeMap) {
            if (item[0].trim().equals(itemCode.trim())) {
                return item[2];
            }
        }
        return "Unit not found";
    }

    private static void priceTrend() {
        String filePath3 = filepath3();

        // Specify the item code for which to track the price trend
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the item code: ");
        String targetItemCode = scanner.nextLine();

        try {
            // Read prices from the CSV file
            List<DataClass3> priceDataList = DataClass3.readPricesFromCSV(filePath3, targetItemCode);

            if (priceDataList.isEmpty()) {
                System.out.println("No price found for the given item code.");
                return; // Return to the calling method (back to the product selection menu)
            }

            // Calculate the average price for each day
            List<DailyPrice> dailyPrices = DataClass3.calcAveragePrices(priceDataList);

            // Display the average price trend for the first 14 days
            PriceTrend(dailyPrices, targetItemCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to display the average price trend for the first 14 days
    private static void PriceTrend(List<DailyPrice> dailyPrices, String targetItemCode) {
        System.out.println("Price Trend Chart for Item Code " + targetItemCode);
        System.out.println("\nDays  |  Average Price");
        System.out.println("-----------------------");

        // Display average prices and days for the first 14 days
        int dayCount = 1;

        for (DailyPrice dailyPrice : dailyPrices) {
            // Check if the date is on or before the 14th of August
            if (dayCount > 14) {
                break;  // Break the loop if we have reached day 14
            }

            System.out.printf("%2d    |  RM%.2f\n", dayCount, dailyPrice.getAveragePrice());
            dayCount++;
        }

        // Display scale
        double min = DataClass3.findMinAveragePrice(dailyPrices);
        double max = DataClass3.findMaxAveragePrice(dailyPrices);
        System.out.printf("\nScale: RM%.2f", ((max - min) / 2));
        System.out.println("");
    }

    
    
    
    
    
    
    //view shopping cart
    public static void viewshoppingcart(String username) throws IOException, SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/fop";
        String dbUsername = "root";
        String dbPassword = "";

        printCart(username, jdbcUrl, dbUsername, dbPassword);
        printTotal(username);

    }

    //addtoshoppingcart
    public static void addshoppingcart(String username) throws IOException, SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/fop";
        String dbUsername = "root";
        String dbPassword = "";

        Scanner sc = new Scanner(System.in);

        cartInterface(username, jdbcUrl, dbUsername, dbPassword);

        //calling method to print out cart
        System.out.println("Do you want to print the cart : (Yes/No)");
        String choice = sc.nextLine();

        if ("Yes".equals(choice)) {
            printCart(username, jdbcUrl, dbUsername, dbPassword);
            printTotal(username);
        }
        System.out.println("\nShopping Cart\n");
        System.out.println("1. View cheapest seller for all selected items.");
        System.out.println("2. Back to previous selection.");
        System.out.println("3. End the program.");
        System.out.print("Please enter your option: ");

        int option = sc.nextInt();
        if (option == 1) {
            cheapestCart(username);
        }
        if (option == 2) {
            return;
        }
        if (option == 3) {
            System.exit(0);
        } else {
            System.out.println("Invalid option.");
        }
    }

    //method to put into database
    public static void cartInterface(String username, String jdbcUrl, String dbUsername, String dbPassword) throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        List<project.PriceDataCart> cart = new ArrayList<>();

        //add to cart interface
        while (true) {
            System.out.print("Enter the item code (or 'stop' to finish): ");
            String CartItemCode = scanner.nextLine();

            if (CartItemCode.equalsIgnoreCase("stop")) {
                break;
            }

            System.out.print("Enter the premise code: ");
            String CartPremiseCode = scanner.nextLine();

            System.out.print("Enter the date (dd/MM/yyyy): ");
            String CartDate = scanner.nextLine();

            String filePath = filepath3();

            // Read the csv file
            List<project.PriceDataCart> priceDataList = readPricesFromCSV(filePath, CartItemCode, CartPremiseCode, CartDate, jdbcUrl, dbUsername, dbPassword);  // Add to cart
            // Display items in the cart
            System.out.println("Items in the Cart:");
            for (project.PriceDataCart priceDataCart : priceDataList) {
                System.out.println("Date: " + priceDataCart.getDates()
                        + ", Premise Code: " + priceDataCart.getPremiseCodes()
                        + ", Item Code: " + priceDataCart.getItemCodes()
                        + ", Price: " + priceDataCart.getPrices());

                // Insert into the database
                // Insert into the database only if the item is not already in the cart
                insertItem(username, priceDataCart);

            }
            System.out.println("\n");
        }

    }

    //reads the csv file 3
    public static List<project.PriceDataCart> readPricesFromCSV(String filePath, String CartItemCode, String CartPremiseCode, String CartDate, String jdbcUrl, String dbUsername, String dbPassword) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        reader.readLine();
        List<project.PriceDataCart> priceDataListCart = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                try {
                    String date = parts[0].trim();
                    String premiseCode = parts[1].trim();
                    String itemCode = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());

                    if (itemCode.equals(CartItemCode) && date.equals(CartDate) && premiseCode.equals(CartPremiseCode)) {

                        //calling methods to find the item and unit and premise name
                        String item = findItem(CartItemCode, jdbcUrl, dbUsername, dbPassword);
                        String unit = findUnit(CartItemCode, jdbcUrl, dbUsername, dbPassword);
                        String premise = findPremise(CartPremiseCode, jdbcUrl, dbUsername, dbPassword);

                        //insert the returned value of item unit and premise name in priceDataCart
                        project.PriceDataCart priceDataCart = new project.PriceDataCart(date, premiseCode, itemCode, price, item, premise, unit);
                        priceDataListCart.add(priceDataCart);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        reader.close();
        return priceDataListCart;
    }

    //methods to find and return item name (under add to shopping cart)
    public static String findItem(String CartItemCode, String jdbcUrl, String dbUsername, String dbPassword) throws SQLException {
        String itemName = null; // Declare the variable outside the try block

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT item FROM lookup_item WHERE item_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, CartItemCode);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // User found
                        itemName = resultSet.getString("item");
                    } else {
                        System.out.println("Item not found. Please check your item code.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return itemName inside the method
        return itemName;
    }

    //methods to find unit  and return unit
    public static String findUnit(String CartItemCode, String jdbcUrl, String dbUsername, String dbPassword) throws SQLException {
        String unit = null; // Declare the variable outside the try block

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT unit FROM lookup_item WHERE item_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, CartItemCode);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // User found
                        unit = resultSet.getString("unit");
                    } else {
                        System.out.println("Item not found. Please check your item code.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return itemName inside the method
        return unit;
    }

    //methods to find and return premise name
    public static String findPremise(String CartPremiseCode, String jdbcUrl, String dbUsername, String dbPassword) throws SQLException {
        String premise = null; // Declare the variable outside the try block

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT premise FROM lookup_premise WHERE premise_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, CartPremiseCode);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // User found
                        premise = resultSet.getString("premise");
                    } else {
                        System.out.println("Item not found. Please check your item code.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return itemName inside the method
        return premise;
    }

    //method to store in the added item into database
    public static void insertItem(String username, project.PriceDataCart priceDataCart) throws SQLException {
        System.out.println(priceDataCart.toString());
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO cart (item_code, premise_code, date, price, username, item, premise, unit) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, priceDataCart.getItemCodes());
            preparedStatement.setString(2, priceDataCart.getPremiseCodes());
            preparedStatement.setString(3, priceDataCart.getDates());
            preparedStatement.setDouble(4, priceDataCart.getPrices());
            preparedStatement.setString(5, username);
            preparedStatement.setString(6, priceDataCart.getItem());
            preparedStatement.setString(7, priceDataCart.getPremise());
            preparedStatement.setString(8, priceDataCart.getUnit());

            //premise unit and item  in the database as well
            preparedStatement.executeUpdate();
        }
    }

    //method to print the database cart
    public static void printCart(String username, String jdbcUrl, String dbUsername, String dbPassword) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT * FROM cart WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Shopping Cart for " + username + ":");

                    while (resultSet.next()) {
                        System.out.println("Item Code: " + resultSet.getString("item_code")
                                + ", Item: " + resultSet.getString("item")
                                + ", Premise Code: " + resultSet.getString("premise_code")
                                + ", Premise Name: " + resultSet.getString("premise")
                                + ", Unit: " + resultSet.getString("unit")
                                + ", Price: " + resultSet.getDouble("price")
                                + ", Date: " + resultSet.getString("date"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printTotal(String username) {

        try (Connection connection = DatabaseConnection.getConnection();) {
            String query = "SELECT price FROM cart WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    double total = 0;

                    while (resultSet.next()) {
                        total += resultSet.getDouble("price");
                    }
                    System.out.println("Total price for " + username + ": " + String.format("%.2f", total));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method to display to 5 cheapest
    public static void cheapestCart(String username) {
        String itemsFilePath = filepath1();
        String premiseFilePath = filepath2();
        String pricesFilePath = filepath3();

        try (Connection connection = DatabaseConnection.getConnection();) {
            String query = "SELECT * FROM cart WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Shopping Cart for " + username + ":");

                    while (resultSet.next()) {
                        System.out.println("Item: " + resultSet.getString("item"));
                        String itemCart = resultSet.getString("item");
                        findCheapestPrices(itemCart, itemsFilePath, pricesFilePath, premiseFilePath);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static class PriceDataCart {

        private String dates;
        private String premiseCodes;
        private String itemCodes;
        private double prices;
        private String items;
        private String units;
        private String premise;

        public PriceDataCart(String dateCart, String premiseCodeCart, String itemCodeCart, double priceCart, String item, String premise, String unit) {
            this.dates = dateCart;
            this.premiseCodes = premiseCodeCart;
            this.itemCodes = itemCodeCart;
            this.prices = priceCart;
            this.items = item;
            this.premise = premise;
            this.units = unit;
        }

        public String getDates() {
            return dates;
        }

        public String getPremiseCodes() {
            return premiseCodes;
        }

        public String getItemCodes() {
            return itemCodes;
        }

        public double getPrices() {
            return prices;
        }

        public String getItem() {
            return items;
        }

        public String getPremise() {
            return premise;
        }

        public String getUnit() {
            return units;
        }

        @Override
        public String toString() {
            return "PriceDataCart{" + "prices=" + prices + ", items=" + items + ", units=" + units + ", premise=" + premise + '}';
        }

    }

}
