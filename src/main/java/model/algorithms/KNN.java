package model.algorithms;

import model.RowWithLabel;
import model.TableWithLabels;

import java.util.List;

public class KNN implements Algorithm<TableWithLabels,Integer>, DistanceClient {
    TableWithLabels trainedData;
    private Distance distance;
    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    public void train(TableWithLabels data){
        trainedData = data;
    }
    public Integer estimate(List<Double> data) {
        if (data.size() == 0 || trainedData.getRowAt(0).size() == 0 || data.size() != trainedData.getRowAt(0).size())
            throw new IllegalArgumentException(); //Si cualquiera de listas esta vacía o su tamaño no es el mismo lanza una excepción
        RowWithLabel closestElement = findClosestElement(data);
        assert closestElement != null;
        return closestElement.getNumberClass();
    }
    private RowWithLabel findClosestElement(List<Double> data){
        double closestNum = -1.; //-1. se entiende que es como un null
        RowWithLabel closestElement = null;
        for (int currentRow = 0; currentRow < trainedData.getNumRows(); currentRow++) {
            double sqrSum = distance.calculateDistance(trainedData.getRowAt(currentRow).getData(),data);
            if (closestNum == -1. || sqrSum < closestNum) {
                closestNum = sqrSum;
                closestElement = trainedData.getRowAt(currentRow);
            }
        }
        return closestElement;
    }
}
