package com.example.csc311_hw2;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CSC 311 Magdaleno Perez - Shape Guessing");
        stage.setScene(scene);
        stage.show();
    }


    //Creating Database and table
    public static void createDatabaseAndTable(){
        //********************************
        //Creating Database
        //********************************

        String dbFilePath = ".//Guesses.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            try (Database db =
                         DatabaseBuilder.create(Database.FileFormat.V2010, new File(dbFilePath))) {
                System.out.println("The database file has been created.");
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }



        //********************************
        //Creating Table
        //********************************

        try {
            Connection conn = DriverManager.getConnection(databaseURL);
            String sql;
            sql = "CREATE TABLE Guesses (Guess_Number INT, Correct_Shape nvarchar(255), Guessed_Shape nvarchar(255))";
            Statement createTableStatement = conn.createStatement();
            createTableStatement.execute(sql);
            conn.commit();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }       //end of creating database and table


    public static void main(String[] args) {
//        createDatabaseAndTable();
        launch();
    }
}