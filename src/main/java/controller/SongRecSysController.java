package controller;

import model.algorithms.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import model.util.SongRecSys;
import view.StartView;
import view.View;

import java.io.IOException;
import java.util.List;

public class SongRecSysController {
    SongRecSys songRecSys;
    View view;
    StartView startView;
    Button recommendButton;
    ListView<String> songNamesListView;
    ListView<String> resultsListView;
    Spinner<Integer> spinner;
    String selectedSong;

    public SongRecSysController(View view, StartView startView) {
        this.view = view;
        this.startView = startView;
    }

    public void addMainStageElements() {
        recommendButton = view.getRecommendButton();
        songNamesListView = view.getSongNamesListView();

        recommendButton.setOnAction(e -> handleRecommendButton());
        songNamesListView.setOnMouseClicked(this::handleListSelect);
    }

    public void addResultsStageElements() {
        resultsListView = view.getResultsListView();
        spinner = view.getSpinner();

        view.getCloseButton().setOnAction(e -> handleCloseButton());
        spinner.valueProperty().addListener(e -> handleSpinnerValueChange());
    }

    public void defineModelParameters(String recommendationMethod, String distanceType) throws Exception {
        Distance distance;

        if (distanceType == "Euclidean") {
            distance = new EuclideanDistance();
        } else if (distanceType == "Manhattan") {
            distance = new ManhattanDistance();
        } else {
            throw new IllegalArgumentException();
        }

        songRecSys = new SongRecSys(recommendationMethod, distance);
    }

    public List<String> recommend(String name, int numRecommendations) {
        return songRecSys.recommend(name, numRecommendations);
    }

    public List<String> readNames() throws IOException {
        String sep = System.getProperty("file.separator");
        String ruta = "src" + sep + "main" + sep + "resources" + sep + "songs_files";
        return SongRecSys.readNames(ruta + sep + "songs_test_names.csv");
    }

    private void handleRecommendButton() {
        String algorithmType = view.getAlgorithmToggleGroup().getSelectedToggle().getUserData().toString();
        String distanceType = view.getDistanceToggleGroup().getSelectedToggle().getUserData().toString();

        try {
            defineModelParameters(algorithmType, distanceType);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        selectedSong = songNamesListView.getSelectionModel().getSelectedItem();
        startView.startResultsStage(selectedSong);

        spinner.getValueFactory().setValue(5);
        ObservableList<String> resultsNames = FXCollections.observableArrayList(recommend(selectedSong, 5));
        resultsListView.setItems(resultsNames);

        startView.showResultsStage();
    }
    private void handleCloseButton(){
        startView.closeResultsStage();
    }

    private void handleListSelect(MouseEvent e) {
        recommendButton.setText("Recommend on " + songNamesListView.getSelectionModel().getSelectedItem() + "...");
        recommendButton.setDisable(false);
        if (e.getClickCount() == 2) {
            handleRecommendButton();
        }
    }

    private void handleSpinnerValueChange() {
        ObservableList<String> resultNames = FXCollections.observableArrayList(recommend(selectedSong, spinner.valueProperty().getValue()));
        resultsListView.setItems(resultNames);
        resultsListView.refresh();
    }

}
