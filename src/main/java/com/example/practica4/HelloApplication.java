package com.example.practica4;

import controller.CallSongRecSys;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

import static util.SongRecSys.readNames;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "src" + sep + "main" + sep + "resources" + sep + "songs_files";
        CallSongRecSys callSongRecSys = new CallSongRecSys();
        AtomicReference<String> selectedSong = new AtomicReference<>("");

        primaryStage.setTitle("Recommend!");
        Stage resultsStage = new Stage();
        resultsStage.initModality(Modality.APPLICATION_MODAL);

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setPadding(new Insets(2, 10, 10, 10));

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setSpacing(15);
        vBox1.setPadding(new Insets(5, 5, 10, 5));
        vBox1.setMinWidth(250);
        vBox1.setPrefWidth(300);
        hBox1.getChildren().add(vBox1);

        VBox vBox2 = new VBox();
        vBox2.setSpacing(10);

        ToggleGroup toggleGroup1 = new ToggleGroup();
        Text text1 = new Text("Recommendation Type");
        vBox2.getChildren().add(text1);

        RadioButton radioButton1 = new RadioButton("Recommend based on song features");
        toggleGroup1.getToggles().add(radioButton1);
        vBox2.getChildren().add(radioButton1);
        radioButton1.setSelected(true);

        RadioButton radioButton2 = new RadioButton("Recommend based on guessed genre");
        toggleGroup1.getToggles().add(radioButton2);
        vBox2.getChildren().add(radioButton2);

        vBox1.getChildren().add(vBox2);

        VBox vBox3 = new VBox();
        vBox3.setSpacing(10);

        ToggleGroup toggleGroup2 = new ToggleGroup();
        Text text2 = new Text("Distance Type");
        vBox3.getChildren().add(text2);

        RadioButton radioButton3 = new RadioButton("Euclidean");
        toggleGroup2.getToggles().add(radioButton3);
        vBox3.getChildren().add(radioButton3);
        radioButton3.setSelected(true);

        RadioButton radioButton4 = new RadioButton("Manhattan");
        toggleGroup2.getToggles().add(radioButton4);
        vBox3.getChildren().add(radioButton4);

        vBox1.getChildren().add(vBox3);

        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.TOP_CENTER);
        Button button1 = new Button("Recommend...");
        button1.setDisable(true);
        hBox2.getChildren().add(button1);
        vBox1.getChildren().add(hBox2);
        //TODO button1.addEventHandler(ActionEvent.ACTION,new HelloController());

        ObservableList<String> names = FXCollections.observableArrayList(readNames(ruta + sep + "songs_test_names.csv"));
        ListView<String> listView1 = new ListView<>(names);
        listView1.setTooltip(new Tooltip("Double click for recommendations based on this song"));
        listView1.setOrientation(Orientation.VERTICAL);
        listView1.setMinHeight(50);
        listView1.setPrefWidth(10000);
        hBox1.getChildren().add(listView1);
        listView1.getSelectionModel().selectedItemProperty().addListener(e -> {
            button1.setDisable(false);
        });


        //Scene scene = new Scene(hBox1, 605, 225);
        Scene scene = new Scene(hBox1, 560, 500);
        scene.getStylesheets().add(HelloApplication.class.getResource("stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(340);
        primaryStage.setMinWidth(400);
        primaryStage.show();

        VBox vBox4 = new VBox();
        vBox4.setPadding(new Insets(2, 10, 10, 10));
        vBox4.setSpacing(15);

        HBox hBox3 = new HBox();
        hBox3.setAlignment(Pos.CENTER_LEFT);
        Text text3 = new Text("Number of recommendations:   ");
        Text text4 = new Text("If you liked ... then you might like");


        Spinner<Integer> spinner = new Spinner<>(1, 50, 5);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spinner.setPrefWidth(80);
        spinner.editorProperty().get().setAlignment(Pos.CENTER);
        hBox3.getChildren().add(text3);
        hBox3.getChildren().add(spinner);
        vBox4.getChildren().add(hBox3);

        vBox4.getChildren().add(text4);


        ObservableList<String> names2 = FXCollections.observableArrayList(readNames(ruta + sep + "songs_test_names.csv"));
        ListView<String> listView2 = new ListView<>(names2);
        listView2.setPrefHeight(5000);
        vBox4.getChildren().add(listView2);

        Button button2 = new Button("Back");
        vBox4.getChildren().add(button2);
        button2.setOnAction(e -> {
            resultsStage.close();
        });

        Scene scene2 = new Scene(vBox4, 500, 400);
        scene2.getStylesheets().add(HelloApplication.class.getResource("stylesheet.css").toExternalForm());
        resultsStage.setScene(scene2);
        resultsStage.setMinHeight(340);
        resultsStage.setMinWidth(400);

        listView1.setOnMouseClicked(e -> {
            button1.setText("Recommend on "+listView1.getSelectionModel().getSelectedItem()+"...");
            if (e.getClickCount() == 2) {
                button1.fire();
            }
        });

        button1.setOnAction(e -> {
            String algorithmType = null;
            String distanceType = null;

            if (radioButton1.isSelected()) {
                algorithmType = "knn";
            } else if (radioButton2.isSelected()) {
                algorithmType = "kmeans";
            }

            if (radioButton3.isSelected()) {
                distanceType = "Euclidean";
            } else if (radioButton4.isSelected()) {
                distanceType = "Manhattan";
            }

            try {
                callSongRecSys.defineParameters(algorithmType, distanceType);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            spinner.getValueFactory().setValue(5);
            selectedSong.set(listView1.getSelectionModel().getSelectedItem());
            text4.setText("If you liked \"" + selectedSong + "\" then you might like...");
            ObservableList<String> names3 = FXCollections.observableArrayList(callSongRecSys.recommend(selectedSong.get(), 5));
            listView2.setItems(names3);
            resultsStage.show();
        });

        spinner.valueProperty().addListener(e -> {
            ObservableList<String> names3 = FXCollections.observableArrayList(callSongRecSys.recommend(selectedSong.get(), spinner.valueProperty().getValue()));
            listView2.setItems(names3);
            listView2.refresh();
        });
    }
}