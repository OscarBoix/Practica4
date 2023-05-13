package util;

import model.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class CSVUnlabeledFileReader extends ReaderTemplate {
    void openSource(String source) throws FileNotFoundException {
        File csvFile = new File(source);
        fileScanner = new Scanner(csvFile);
    }

    void processHeaders(String headers) {
        this.table = new Table(stringToList(headers));
    }

    void processData(String data) {
        List<Double> doubleList = new ArrayList<>();
        //Se han aplicado algunas técnicas del método readTableWithLabels, aunque no se utilicen labels, para poder trabajar fácilmente con varios tipos de archivos CSV.
        for (String st : stringToList(data)) {
            try {
                doubleList.add(parseDouble(st));
            } catch (NumberFormatException ignored) {

            }
        }
        table.addRow(doubleList);
    }

    void closeSource() {
        fileScanner.close();
    }

    boolean hasMoreData() {
        return fileScanner.hasNextLine();
    }

    String getNextData() {
        return fileScanner.nextLine();
    }

    List<String> stringToList(String str) {
        return new ArrayList<>(List.of(str.split(",")));
    }
}
