
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
   
public class DataClass2 {

   
    private final String premise_code;
    private final String premise;
    private final String address;
    private final String premise_type;
    private final String state;
    private final String district;

    public DataClass2(String premise_code, String premise, String address, String premise_type, String state, String district) {
        this.premise_code = premise_code;
        this.premise = premise;
        this.address = address;
        this.premise_type = premise_type;
        this.state = state;
        this.district = district;
    }

     public static List<DataClass2> readDataFromCsv(String fileName) throws IOException, CsvValidationException {
        List<DataClass2> dataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            List<String[]> records = csvReader.readAll();

            for (int i = 1; i < records.size(); i++) {
                String[] data = records.get(i);
                DataClass2 dataClass2 = createDataClass2(data);
                if (dataClass2 != null) {
                    dataList.add(dataClass2);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    static DataClass2 createDataClass2(String[] data) {
        if (data.length >= 6) {
            return new DataClass2(data[0], data[1], data[2], data[3],data[4],data[5]);
        } else {
            return null; 
        }
        }
    

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("DataClass2 ");
        result.append('\n');
        result.append("Premise Code : ").append(premise_code).append('\n');
        result.append("Premise : ").append(premise).append('\n');
        result.append("Address : ").append(address).append('\n');
        result.append("Premise Type : ").append(premise_type).append('\n');
        result.append("State : ").append(state).append('\n');
        result.append("District : ").append(district).append('\n');
 
        return super.toString();
    }
}
