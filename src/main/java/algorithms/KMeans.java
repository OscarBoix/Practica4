package algorithms;

import exceptions.InvalidClusterNumberException;
import model.Row;
import model.RowWithLabel;
import model.Table;
import model.TableWithLabels;
import util.CSVLabeledFileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans implements Algorithm<Table, Integer>, DistanceClient {
    private final int numClusters;
    private final int numIterations;
    private final long seed;
    private Distance distance;

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public static void main(String[] args) throws IOException {
        CSVLabeledFileReader fileReader = new CSVLabeledFileReader();
        TableWithLabels table = fileReader.readTableWithLabelsFromSource("src/main/resources/csv_2d_kaggle/basic1.csv");
        KMeans algoritmo = new KMeans(4, 100, 2);
        algoritmo.train(table);

        Table datos_out = new Table(new ArrayList<>());

        for (int i = 0; i < table.getNumRows(); i++) {
            List<Double> data = table.getRowAt(i).getData();
            data.add((double) algoritmo.estimate(data));
            datos_out.addRow(data);
        }
        try {
            algoritmo.saveTable(datos_out, "src/main/resources/prueba1.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KMeans(int numClusters, int numIterations, long seed) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.seed = seed;
    }

    private final List<RowWithLabel> centroids = new ArrayList<>();
    private final List<RowWithLabel> groupedData = new ArrayList<>();

    public void train(Table data) {
        if (numClusters > data.getNumRows())
            throw new InvalidClusterNumberException("Hay más grupos que puntos de datos");
        if (numClusters <= 0 || numIterations <= 0) return;

        //Crea una serie de centroides para el número de clusters solicitados
        createCentroids(data);
        //Bucle que realiza las iteraciones
        for (int iterations = 0; iterations < numIterations; iterations++) {
            //PASO 1: Asigna a cada Row su grupo de tal manera que se pueden convertir en RowWithLabel e introducirlos en una lista con su grupo asignado
            assignCentroids(data);

            //PASO 2: Calcula un nuevo centroide para cada grupo
            createNewCentroids(data);
        }
    }

    private void createNewCentroids(Table data) {
        int[] counters = new int[numClusters];
        Double[][] newCentroidsCalculations = new Double[numClusters][data.getRowAt(0).size()];
        for (int i = 0; i < numClusters; i++) {
            counters[i] = 0;
            for (int j = 0; j < data.getRowAt(0).size(); j++) {
                newCentroidsCalculations[i][j] = 0.;
            }
        }

        for (RowWithLabel actual : groupedData) {
            counters[actual.getNumberClass()]++;
            for (int j = 0; j < actual.size(); j++) {
                newCentroidsCalculations[actual.getNumberClass()][j] += actual.getData().get(j);
            }
        }

        for (int i = 0; i < numClusters; i++) {
            for (int j = 0; j < newCentroidsCalculations[0].length; j++) {
                newCentroidsCalculations[i][j] /= counters[i];
                //TODO System.out.printf("%.2f, ", newCentroidsCalculations[i][j]);
            }
            //System.out.print("; ");
            centroids.remove(i);
            centroids.add(i, new RowWithLabel(Arrays.asList(newCentroidsCalculations[i]), i));
        }
        //System.out.println();
    }

    private void createCentroids(Table data) {
        Random random = new Random(seed);
        for (int i = 0; i < numClusters; i++) {
            int index = random.nextInt(data.getNumRows());
            RowWithLabel newCentroid = new RowWithLabel(data.getRowAt(index).getData(), i);
            centroids.add(newCentroid);
        }
    }

    private void assignCentroids(Table data) {
        for (int row = 0; row < data.getNumRows(); row++) {
            double closestNum = -1.;
            RowWithLabel closestCentroid = centroids.get(0);
            Row actualRow = data.getRowAt(row);
            for (int centroid = 0; centroid < numClusters; centroid++) {
                double sqrSum = distance.calculateDistance(actualRow.getData(), centroids.get(centroid).getData());
                if (closestNum == -1. || sqrSum < closestNum) {
                    closestNum = sqrSum;
                    closestCentroid = centroids.get(centroid);
                }
            }
            RowWithLabel newRow = new RowWithLabel(actualRow.getData(), closestCentroid.getNumberClass());
            groupedData.add(newRow);
        }
    }

    @Override
    public Integer estimate(List<Double> data) {


        if (centroids.size() == 0)
            throw new IllegalArgumentException("Cannot estimate, centroids are null. Execute this after train() has been executed.");
        if (data == null)
            throw new IllegalArgumentException("Argument received is null.");
        if (data.size() == 0 || data.size() != centroids.get(0).size())
            throw new IllegalArgumentException("Row size does not equal trained data row size."); //Si cualquiera de listas esta vacía o su tamaño no es el mismo lanza una excepción

        double closestNum = -1.;
        RowWithLabel closestCentroid = centroids.get(0);
        Row newRow = new Row(data);
        for (int centroid = 0; centroid < numClusters; centroid++) {
            double sqrSum = distance.calculateDistance(newRow.getData(), centroids.get(centroid).getData());
            if (closestNum == -1. || sqrSum < closestNum) {
                closestNum = sqrSum;
                closestCentroid = centroids.get(centroid);
            }
        }
        assert closestCentroid != null;
        return closestCentroid.getNumberClass();
    }

    public void saveTable(Table t, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for (int i = 0; i < t.getNumRows(); i++) {
            Row row = t.getRowAt(i);
            List<Double> datos = row.getData();
            int j = 0;
            for (; j < datos.size() - 1; j++) {
                fw.write(datos.get(j).toString());
                fw.write(",");
            }
            fw.write(datos.get(j).toString());
            fw.write("\n");
        }
        fw.close();
    }
}
