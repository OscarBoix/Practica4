package model.util;

import model.TableWithLabels;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class CSVLabeledFileReader extends CSVUnlabeledFileReader {
    boolean labelIsDouble;
    TableWithLabels table;

    public TableWithLabels readTableWithLabelsFromSource(String source) throws FileNotFoundException {
        openSource(source);
        if (hasMoreData())
            processHeaders(getNextData());
        while (hasMoreData())
            processData(getNextData());
        closeSource();
        return table;
    }

    void processHeaders(String headers) {
        this.table = new TableWithLabels(stringToList(headers));
    }

    void processData(String data) {
        List<Double> doubleList = new ArrayList<>();
        String label = "";
            /*
            El siguiente fragmento de código utiliza try y catch aunque suponemos un par de cosas para simplificarlo.
            El bucle recorre cada fila y va añadiendo los doubles a la lista de doubles.
            En el caso de que se encuentre un elemento que no sea double, entonces supondrá que es el label y lo añadira como label.
            Por otro lado, si todos los valores que se encuentra son dobles, entonces la variable labelIsDouble se mantendrá en true
            y el programa tratará el último elemento de la lista doubleList como el label, consecuentemente eliminándolo de la lista.

            Todo esto funciona suponiendo que en el archivo CSV, todas las columnas tienen definido un tipo de elemento y no varían en diferentes filas.
             */
        for (String st : stringToList(data)) {
            try {
                doubleList.add(parseDouble(st));
            } catch (NumberFormatException a) {
                label = st;
                labelIsDouble = false;
            }
        }
        if (labelIsDouble) {
            label = String.valueOf(doubleList.get(doubleList.size() - 1));
            doubleList.remove(doubleList.size() - 1);
        }
        table.addRow(doubleList, label);
    }
}
