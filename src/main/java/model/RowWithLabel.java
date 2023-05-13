package model;

import java.util.List;

public class RowWithLabel extends Row {
    private int numberClass;

    public RowWithLabel(List<Double> data, int numberClass) {
        super(data);
        this.numberClass = numberClass;
    }


    public int getNumberClass() {
        return numberClass;
    }
}
