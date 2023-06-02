package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableWithLabels extends Table {
    private final List<RowWithLabel> rows;
    private final Map<String,Integer> labelsToIndex;
    public TableWithLabels(List<String> headers) {
        super(headers);
        labelsToIndex = new HashMap<>();
        rows = new ArrayList<>();
    }
    public void addRow(List<Double> data, String label){
        if(!labelsToIndex.containsKey(label))
            labelsToIndex.put(label,labelsToIndex.size());
        RowWithLabel newRow = new RowWithLabel(data, labelsToIndex.get(label));
        rows.add(newRow);
    }

    public RowWithLabel getRowAt(int index){
        return rows.get(index);
    }

    @Override
    public void printRow(int index) {
        System.out.print(rows.get(index).getNumberClass()+": ");
        super.printRow(index);
    }

    public void printAllRows(){
        for(int i=0;i<rows.size();i++) {
            printRow(i);
        }
    }

    public int getNumRows(){
        return rows.size();
    }
}
