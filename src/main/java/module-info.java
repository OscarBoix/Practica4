module com.example.practica4 {
    requires javafx.controls;

    opens view to javafx.fxml;
    exports view;
}