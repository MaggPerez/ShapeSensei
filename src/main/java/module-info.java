module com.example.csc311_hw2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.healthmarketscience.jackcess;


    opens com.example.csc311_hw2 to javafx.fxml;
    exports com.example.csc311_hw2;
}