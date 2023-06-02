package model.util;

import model.algorithms.*;
import model.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongRecSys {
    private final RecSys recsys;

    public SongRecSys(String method, Distance distance) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "src" + sep + "main" + sep + "resources" + sep + "songs_files";

        // File names (could be provided as arguments to the constructor to be more general)
        Map<String, String> filenames = new HashMap<>();
        filenames.put("knn" + "train", ruta + sep + "songs_train.csv");
        filenames.put("knn" + "test", ruta + sep + "songs_test.csv");
        filenames.put("kmeans" + "train", ruta + sep + "songs_train_withoutnames.csv");
        filenames.put("kmeans" + "test", ruta + sep + "songs_test_withoutnames.csv");

        // Algorithms
        Map<String, Algorithm> algorithms = new HashMap<>();
        algorithms.put("knn", new KNN());
        algorithms.put("kmeans", new KMeans(15, 200, 4321));

        ((DistanceClient) algorithms.get("knn")).setDistance(distance);
        ((DistanceClient) algorithms.get("kmeans")).setDistance(distance);

        // Tables
        Map<String, Table> tables = new HashMap<>();
        String[] stages = {"train", "test"};
        CSVUnlabeledFileReader csvU = new CSVUnlabeledFileReader();
        CSVLabeledFileReader csvL = new CSVLabeledFileReader();
        for (String stage : stages) {
            tables.put("knn" + stage, csvL.readTableWithLabelsFromSource(filenames.get("knn" + stage)));
            tables.put("kmeans" + stage, csvU.readTableFromSource(filenames.get("kmeans" + stage)));
        }

        // Names of items
        List<String> names = readNames(ruta + sep + "songs_test_names.csv");

        // Start the RecSys
        this.recsys = new RecSys(algorithms.get(method));
        this.recsys.train(tables.get(method + "train"));
        this.recsys.run(tables.get(method + "test"), names);

        // Given a liked item, ask for a number of recomendations


        // Display the recommendation text (to be replaced with graphical display with JavaFX implementation)

    }

    public List<String> recommend(String songName, int numRecommendations) {
        return this.recsys.recommend(songName, numRecommendations);
    }

    public static List<String> readNames(String fileOfItemNames) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileOfItemNames));
        String line;
        List<String> names = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            names.add(line);
        }
        br.close();
        return names;
    }

    private void reportRecommendation(String liked_name, List<String> recommended_items) {
        System.out.println("If you liked \"" + liked_name + "\" then you might like:");
        for (String name : recommended_items) {
            System.out.println("\t * " + name);
        }
    }
}