

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataClass3 {
    
        private final Date date;
        private final String premiseCode;
        private final String itemCode;
        private final double price;

        public DataClass3(Date date, String premiseCode, String itemCode, double price) {
            this.date = date;
            this.premiseCode = premiseCode;
            this.itemCode = itemCode;
            this.price = price;
        }

        public Date getDate() {
            return date;
        }

        public String getPremiseCode() {
            return premiseCode;
        }

        public String getItemCode() {
            return itemCode;
        }

        public double getPrice() {
            return price;
        }
        
         public static List<DataClass3> readDataFromCsv(String fileName) throws IOException, CsvValidationException {
        List<DataClass3> dataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            List<String[]> records = csvReader.readAll();

            for (int i = 0; i < records.size(); i++) {
                String[] data = records.get(i);
                DataClass3 dataClass3 = createDataClass3(data);
                if (dataClass3 != null) {
                    dataList.add(dataClass3);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return dataList;
    }
 
 static DataClass3 createDataClass3(String[] data) {
        if (data.length >= 4) {
            try{
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(data[0]);
            return new DataClass3(date, data[1], data[2],  Double.parseDouble(data[3]));
        
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception as needed
        }
        }
        return null;
 }
    // Getter methods for each field

    @Override
    public String toString() {
        // Implement the toString method if needed
        StringBuilder result = new StringBuilder("DataClass3 ");
        result.append('\n');
        result.append("Date : ").append(date).append('\n');
        result.append("Premise Code : ").append(premiseCode).append('\n');
        result.append("Item Code : ").append(itemCode).append('\n');
        result.append("Price : ").append(price).append('\n');
 
        return super.toString();
    }
    
        // Function to read prices from a CSV file for a particular item
    public static List<DataClass3> readPricesFromCSV(String filePath, String targetItemCode) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // Assuming the CSV file has a header, skip it
        reader.readLine();

        List<DataClass3> priceDataList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(parts[0].trim());
                    String premiseCode = parts[1].trim();
                    String itemCode = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());

                    // Check if the current row corresponds to the target item code
                    if (itemCode.equals(targetItemCode)) {
                        DataClass3 priceData = new DataClass3(date, premiseCode, itemCode, price);
                        priceDataList.add(priceData);
                    }
                } catch (ParseException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        reader.close();
        return priceDataList;
    }
    
    // Function to calculate the average price for each day
    public static List<DailyPrice> calcAveragePrices(List<DataClass3> priceDataList) {
        List<DailyPrice> dailyPrices = new ArrayList<>();

        // Group prices by date and item code
        for (DataClass3 priceData : priceDataList) {
            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(priceData.getDate());
            String itemCode = priceData.getItemCode();

            // Combine date and item code as the key for grouping
            String key = dateString + "_" + itemCode;

            // Find the corresponding daily price or create a new one
            DailyPrice dailyPrice = getOrCreateDailyPrice(dailyPrices, dateString, itemCode);
            dailyPrice.addPrice(priceData.getPrice());
        }

        return dailyPrices;
    }
    
    // Helper function to get or create a DailyPrice object
    private static DailyPrice getOrCreateDailyPrice(List<DailyPrice> dailyPrices, String dateString, String itemCode) {
        for (DailyPrice dailyPrice : dailyPrices) {
            if (dailyPrice.getDateString().equals(dateString) && dailyPrice.getItemCode().equals(itemCode)) {
                return dailyPrice;
            }
        }

        DailyPrice newDailyPrice = new DailyPrice(dateString, itemCode);
        dailyPrices.add(newDailyPrice);
        return newDailyPrice;
    }
    
    // Function to find the minimum average price in the list of DailyPrice
    public static double findMinAveragePrice(List<DailyPrice> dailyPrices) {
        double min = Double.MAX_VALUE;

        for (DailyPrice dailyPrice : dailyPrices) {
            double averagePrice = dailyPrice.getAveragePrice();
            if (averagePrice < min) {
                min = averagePrice;
            }
        }

        return min;
    }

    // Function to find the maximum average price in the list of DailyPrice
    public static double findMaxAveragePrice(List<DailyPrice> dailyPrices) {
        double max = Double.MIN_VALUE;

        for (DailyPrice dailyPrice : dailyPrices) {
            double averagePrice = dailyPrice.getAveragePrice();
            if (averagePrice > max) {
                max = averagePrice;
            }
        }

        return max;
    }
    }

    class DailyPrice {
        private final String dateString;
        private final String itemCode;
        private final List<Double> prices;

        public DailyPrice(String dateString, String itemCode) {
            this.dateString = dateString;
            this.itemCode = itemCode;
            this.prices = new ArrayList<>();
        }

        public String getDateString() {
            return dateString;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void addPrice(double price) {
            prices.add(price);
        }

        public double getAveragePrice() {
            double sum = 0;

            for (Double price : prices) {
                sum += price;
            }

            return sum / prices.size();
        }
    }