package model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    protected List<String> headers;
    protected List<Row> rows;
    public Table(List<String> headers){
        this.headers = headers;
        rows = new ArrayList<>();
    }
    public void addRow(List<Double> data){
        Row newRow = new Row(data);
        rows.add(newRow);
    }
    public Row getRowAt(int index){
        return rows.get(index);
    }
    public void printRow(int index){
        for(Double d:getRowAt(index).getData()){
            System.out.print(d+" ");
        }
        System.out.println();
    }
    public void printAllRows(){
        for(int i=0;i<rows.size();i++) {
            printRow(i);
        }
    }

    public List<String> getHeaders(){
        return headers;
    }
    public int getNumRows(){
        return rows.size();
    }

}
