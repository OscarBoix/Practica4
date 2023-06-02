package model.util;

import model.algorithms.Algorithm;
import model.Row;
import model.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecSys {
    Algorithm<Table, Integer> algorithm;
    Map<Row, Object> rowsToLabel;
    Table testData;
    List<String> testItemNames;


    public RecSys(Algorithm<Table, Integer> algorithm) {
        this.algorithm = algorithm;
        rowsToLabel = new HashMap<>();
    }

    public void train(Table trainData) {
        algorithm.train(trainData);
    }

    public void run(Table testData, List<String> testItemNames) {
        this.testData = testData;
        this.testItemNames = testItemNames;

        for (int i = 0; i < this.testData.getNumRows(); i++) {
            rowsToLabel.put(this.testData.getRowAt(i), algorithm.estimate(this.testData.getRowAt(i).getData()));
        }
    }

    public List<String> recommend(String nameLikedItem, int numRecommendations) {
        if (!testItemNames.contains(nameLikedItem)) return new ArrayList<>();
        Object labelLikedItem = findName(nameLikedItem);
        return getNameSelectItems(labelLikedItem, numRecommendations, nameLikedItem);
    }
    private Object findName(String nameItem){
        int idx = testItemNames.indexOf(nameItem);
        Row rowLikedItem = testData.getRowAt(idx);
        return rowsToLabel.get(rowLikedItem);
    }
    private List<String> getNameSelectItems(Object labelLikedItem, int numRecommendations, String nameLikedItem) {
        List<String> recomendaciones = new ArrayList<>();
        for (int i = 0; i < testData.getNumRows(); i++) {
            Row currentRow = testData.getRowAt(i);
            if (rowsToLabel.get(currentRow) == labelLikedItem && testItemNames.get(i) != nameLikedItem) {
                recomendaciones.add(testItemNames.get(i));
                if (recomendaciones.size() >= numRecommendations)
                    break;
            }
        }
        return recomendaciones;
    }
}

/*
Código alternativo que se ha acabado desechando ya que en el enunciado se pedía que se hiciera de una manera específica, aunque creemos que era más óptimo

Map<String, Object> namesToIndex;

    public RecSys(Algorithm algorithm){
        this.algorithm = algorithm;
        namesToIndex = new HashMap<>();
    }
    public void run(Table testData, List<String> testItemNames){
        for (int i = 0; i<testData.getNumRows(); i++) {
            namesToIndex.put(testItemNames.get(i),algorithm.estimate(testData.getRowAt(i).getData()));
        }
    }
    public List<String> recommend(String nameLikedItem, int numRecommendations){
        List<String> recomendaciones = new ArrayList<>();
        Object index = namesToIndex.get(nameLikedItem);
        for (String s : namesToIndex.keySet()){
            System.out.println(s);
            System.out.println(namesToIndex.get(s));
            if (namesToIndex.get(s) == index && s != nameLikedItem){
                recomendaciones.add(s);
            if (recomendaciones.size() >= numRecommendations)
                break;
            }
        }
        return recomendaciones;
    }
 */
