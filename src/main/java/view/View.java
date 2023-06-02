package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;

import static model.util.SongRecSys.readNames;

public class View extends HBox {
    ToggleGroup algorithmToggleGroup;
    ToggleGroup distanceToggleGroup;
    Button recommendButton;
    Button closeButton;
    ListView<String> songNamesListView;
    ListView<String> resultsListView;
    Spinner<Integer> spinner;

    public Button getRecommendButton() {
        return recommendButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public ToggleGroup getAlgorithmToggleGroup() {
        return algorithmToggleGroup;
    }

    public ToggleGroup getDistanceToggleGroup() {
        return distanceToggleGroup;
    }

    public ListView<String> getSongNamesListView() {
        return songNamesListView;
    }

    public ListView<String> getResultsListView() {
        return resultsListView;
    }

    public Spinner<Integer> getSpinner() {
        return spinner;
    }

    public void startPrimaryStage(Stage primaryStage) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "src" + sep + "main" + sep + "resources" + sep + "songs_files";

        primaryStage.setTitle("SongRecSys Recommendation");

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setPadding(new Insets(2, 10, 10, 10));

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setSpacing(15);
        vBox1.setPadding(new Insets(5, 5, 10, 5));
        vBox1.setMinWidth(275);
        vBox1.setPrefWidth(300);
        hBox1.getChildren().add(vBox1);

        VBox vBox2 = new VBox();
        vBox2.setSpacing(10);

        algorithmToggleGroup = new ToggleGroup();
        Text text1 = new Text("Recommendation Type");
        vBox2.getChildren().add(text1);

        RadioButton radioButton1 = new RadioButton("Recommend based on song features");
        algorithmToggleGroup.getToggles().add(radioButton1);
        radioButton1.setUserData("knn");
        vBox2.getChildren().add(radioButton1);
        radioButton1.setSelected(true);

        RadioButton radioButton2 = new RadioButton("Recommend based on guessed genre");
        algorithmToggleGroup.getToggles().add(radioButton2);
        radioButton2.setUserData("kmeans");
        vBox2.getChildren().add(radioButton2);

        vBox1.getChildren().add(vBox2);

        VBox vBox3 = new VBox();
        vBox3.setSpacing(10);

        distanceToggleGroup = new ToggleGroup();
        Text text2 = new Text("Distance Type");
        vBox3.getChildren().add(text2);

        RadioButton radioButton3 = new RadioButton("Euclidean");
        distanceToggleGroup.getToggles().add(radioButton3);
        radioButton3.setUserData("Euclidean");
        vBox3.getChildren().add(radioButton3);
        radioButton3.setSelected(true);

        RadioButton radioButton4 = new RadioButton("Manhattan");
        distanceToggleGroup.getToggles().add(radioButton4);
        radioButton4.setUserData("Manhattan");
        vBox3.getChildren().add(radioButton4);

        vBox1.getChildren().add(vBox3);

        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.TOP_CENTER);
        recommendButton = new Button("Recommend...");
        recommendButton.setDisable(true);
        hBox2.getChildren().add(recommendButton);
        vBox1.getChildren().add(hBox2);

        ObservableList<String> names = FXCollections.observableArrayList(readNames(ruta + sep + "songs_test_names.csv"));
        songNamesListView = new ListView<>(names);
        songNamesListView.setTooltip(new Tooltip("Double click for recommendations based on this song"));
        songNamesListView.setOrientation(Orientation.VERTICAL);
        songNamesListView.setMinHeight(50);
        songNamesListView.setPrefWidth(10000);
        hBox1.getChildren().add(songNamesListView);

        //Scene scene = new Scene(hBox1, 605, 225);
        Scene scene = new Scene(hBox1, 600, 500);
        scene.getStylesheets().add(String.valueOf(View.class.getResource("stylesheet.css")));
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(340);
        primaryStage.setMinWidth(400);
    }

    public void startResultsStage(Stage resultsStage, String selectedSong) {
        resultsStage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox4 = new VBox();
        vBox4.setPadding(new Insets(2, 10, 10, 10));
        vBox4.setSpacing(15);

        HBox hBox3 = new HBox();
        hBox3.setAlignment(Pos.CENTER_LEFT);
        Text text3 = new Text("Number of recommendations:   ");
        Text text4 = new Text("If you liked \"" + selectedSong + "\" then you might like...");


        spinner = new Spinner<>(1, 50, 5);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spinner.setPrefWidth(100);
        spinner.editorProperty().get().setAlignment(Pos.CENTER);
        hBox3.getChildren().add(text3);
        hBox3.getChildren().add(spinner);
        vBox4.getChildren().add(hBox3);

        vBox4.getChildren().add(text4);

        resultsListView = new ListView<>();
        resultsListView.setPrefHeight(5000);
        vBox4.getChildren().add(resultsListView);

        Button button2 = new Button("Back");
        vBox4.getChildren().add(button2);
        button2.setOnAction(e -> {

        });

        Scene scene2 = new Scene(vBox4, 500, 400);
        scene2.getStylesheets().add(String.valueOf(View.class.getResource("stylesheet.css")));
        resultsStage.setScene(scene2);
        resultsStage.setMinHeight(340);
        resultsStage.setMinWidth(400);
    }
}