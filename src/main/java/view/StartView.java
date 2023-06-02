package view;

import controller.SongRecSysController;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartView extends Application {
    private View view;
    private SongRecSysController controller;
    private Stage primaryStage;
    private Stage resultsStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        view = new View();
        this.controller = new SongRecSysController(view, this);
        view.startPrimaryStage(primaryStage);
        controller.addMainStageElements();
        showPrimaryStage();
    }

    public void showPrimaryStage() {
        primaryStage.show();
    }

    public void startResultsStage(String selectedSong) {
        resultsStage = new Stage();
        view.startResultsStage(resultsStage, selectedSong);
        controller.addResultsStageElements();
        showResultsStage();
    }

    public void showResultsStage() {
        resultsStage.show();
    }
    public void closeResultsStage() {
        resultsStage.close();
    }
}
