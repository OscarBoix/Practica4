package model;

import java.util.List;

public class Centroid extends RowWithLabel{
    public Centroid(List<Double> data, int numberClass) {
        super(data, numberClass);
    }
    public void sumData(List<Double> opData){
        if(opData==null || opData.size() != data.size()) throw new IllegalArgumentException();
        for (int i=0 ;i<data.size();){

        }

    }
}
