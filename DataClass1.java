
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.similarity.LevenshteinDistance; //for fuzzy search

public class DataClass1 {
    private String code;
    private String itemName;
    private String unit;
    private String itemGroup;
    private String itemCategory;

    public DataClass1(String code, String itemName, String unit, String itemGroup, String itemCategory) {
        this.code = code;
        this.itemName = itemName;
        this.unit = unit;
        this.itemGroup = itemGroup;
        this.itemCategory = itemCategory;
    }

    // Getters and setters

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemCategory() {
        return itemCategory;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void updateItemDetails(String newUnit, String newItemGroup, String newItemCategory) {
        this.unit = newUnit;
        this.itemGroup = newItemGroup;
        this.itemCategory = newItemCategory;
    }

    public String toString() {
         return "DataClass1 :" + '\n'+
                "Code : " +code+ '\n' +
                "Item Name  : " + itemName + '\n' +
                "Unit : " + unit + '\n' +
                "Item Group : " + itemGroup + '\n' +
                "Item Category : " + itemCategory;
    }
    public DataClass1() {
    // Default constructor with no parameters
}
public String getItem() {
    return itemName;
}


    // Method to read data from CSV
public static List<DataClass1> readDataFromCsv(String fileName) throws IOException {
    List<DataClass1> dataList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            
            if (data.length > 5) {
                DataClass1 item = new DataClass1(data[0], data[1], data[2], data[3], data[4]);
                dataList.add(item);
            } 
        }
    }
    return dataList;
}
    // Method to write data to CSV
    public static void writeDataToCsv(List<DataClass1> dataList, String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (DataClass1 item : dataList) {
                String line = String.join(",", item.getItemName(), item.getUnit(), item.getItemGroup(), item.getItemCategory());
                bw.write(line);
                bw.newLine();
        }
    }
}
    
    public static List<String> getDistinctColumnValues(List<project.CsvData> csvDataList, String columnName) {
        //initialise an empty listto store value from the specified column
        List<String> distinctValues = new ArrayList<>();

        // go through each of the CSV data == loop over each object in the list
        for (project.CsvData csvData : csvDataList) {
            // amik/extract the value of the specified column from the csvData object
            String value = csvData.getField(columnName);
            
            // Check if the value is already in the distinctValue list
            boolean valueAlreadyInList = false;
            //iterate thro the existig distinctValues list to see if current value is in or not
            for (String listItem : distinctValues) {
                if (listItem.equals(value)) {
                    valueAlreadyInList = true;
                    break;
                }
            }
            
            // Add the value to the list if the value is not found
            if (!valueAlreadyInList) {
                distinctValues.add(value);
            } 
        }
        // Return the distinct values
        return distinctValues;
    }
    public static List<String> getItemCategories(List<project.CsvData> csvDataList, String selectedGroup) {
        //initialise empty list to store item categories for the specified item group
        List<String> distinctItemCategories = new ArrayList<>();

        // loop thro each CSV data object
        for (project.CsvData csvData : csvDataList) {
            //extract values from 2 column, itemgroup and item category
            String itemGroup = csvData.getField("item_group");
            String itemCategory = csvData.getField("item_category");

            // Check if the current item belongs to the selected group
            if (itemGroup.equals(selectedGroup)) {
                if (!distinctItemCategories.contains(itemCategory)) {
                    distinctItemCategories.add(itemCategory);
                }
            }
        } //loop until all CsvData objects have been processed
        // Return the distinct item categories
        return distinctItemCategories;
    }

    public static void displayItemsForCategory(List<project.CsvData> csvDataList, String selectedGroup, String selectedCategory) {
        System.out.println("\nItems for Category: " + selectedCategory);

        // loop through each CSV data object 
        for (project.CsvData csvData : csvDataList) {
            //extract values from 3 column 
            String group = csvData.getField("item_group");
            String category = csvData.getField("item_category");
            String itemName = csvData.getField("item");

            // Check if the item belongs to the selected group and category
            if (group.equalsIgnoreCase(selectedGroup) && category.equalsIgnoreCase(selectedCategory)) {
                System.out.println(itemName);
            }
        }
    }
   

public static DataClass1 findItemByFuzzyName(String fileName, String targetItemName) throws CsvException {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            List<String[]> records = csvReader.readAll();

            //implement a new library levenshteindistance == measuring similarity of 2 string
            //edit = insertions, deletions, or substitutions
            LevenshteinDistance levenshtein = new LevenshteinDistance();

            for (int i = 1; i < records.size(); i++) {
                String[] data = records.get(i);
                if (data.length >= 2 ){
                    String itemName = data[1];
                    
                    int distance = levenshtein.apply(itemName, targetItemName);

                    // can adjust the threshold
                    //if we put 3 or 4, some items are v similar in name, so it might call a different item
                    if (distance <= 2) { // up to 2 edit operations == suitable for very close matches
                        return createDataClass1(data);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //if no item found
    }

public static DataClass1 createDataClass1(String[] data) {
    // Assuming data array has the required information
    String code=data[0];
    String itemName = data[1];
    String unit = data[2];
    String itemGroup = data[3];
    String itemCategory = data[4];

    return new DataClass1(code,itemName, unit, itemGroup, itemCategory);
}
}