package util;

import model.Table;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

abstract class ReaderTemplate {
    Scanner fileScanner;
    Table table;

    public ReaderTemplate() {

    }

    public final Table readTableFromSource(String source) throws FileNotFoundException {
        openSource(source);
        if (hasMoreData())
            processHeaders(getNextData());
        while (hasMoreData()) {
            processData(getNextData());
        }
        closeSource();
        return table;
    }

    abstract void openSource(String source) throws FileNotFoundException;

    abstract void processHeaders(String headers);

    abstract void processData(String data);

    abstract void closeSource();

    abstract boolean hasMoreData();

    abstract String getNextData();

    abstract List<String> stringToList(String str);
}
