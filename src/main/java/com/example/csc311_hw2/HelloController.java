package com.example.csc311_hw2;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.w3c.dom.events.MouseEvent;

import java.sql.*;
import java.util.Random;




public class HelloController {

    //*****************************************************************************************************************
    //                                  Creating and setting up all fx:ids and variables
    //*****************************************************************************************************************


    //TextFields fx:id
    @FXML
    private TextField totalGuessesTextField;
    @FXML
    private TextField correctGuessesTextField;




    //Radio buttons and guess button fx:id
    @FXML
    private RadioButton rectRadioButton;
    @FXML
    private RadioButton circleRadioButton;
    @FXML
    private Button guessButton;



    //Shapes fx:id
    @FXML
    private Rectangle rectangleShape;
    @FXML
    private Circle circleShape;



    //list view fx:id
    @FXML
    private ListView<String> listView;


    //Creating random class to be able to randomly select shapes within a String[] array.
    Random rand = new Random();



    //Variables to keep track of Total guess score and Correct guess score for the TextFields.
    //Both are set to 0
    int totalGuessCount = 0;
    int correctGuessCount = 0;





    //*****************************************************************************************************************
    //                                  The Initialization When Starting the Game
    //*****************************************************************************************************************



    public void initialize() {
        //Creating Initializations for TextFields, toggle group and database.


        //When the app starts, Total Guess Count and Correct Guess Counts are set to 0.
            //We'll display both guess scores in a method, and it will be displayed in the TextFields.
        displayGuessScores(totalGuessCount, correctGuessCount);



        //Creating toggle groups for radio buttons Rectangle and Circle
        ToggleGroup group = new ToggleGroup();
        rectRadioButton.setToggleGroup(group);
        circleRadioButton.setToggleGroup(group);


        //When the app starts, it will check and delete any data in the database
        //from previous games if user did not press "New Game".
        try {
            String dbFilePath = ".//Guesses.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
            Connection conn = DriverManager.getConnection(databaseURL);
            String sql = "DELETE FROM Guesses";
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = conn.prepareStatement(sql);
                int rowsDeleted = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }


    }   //end of initialize




    //*****************************************************************************************************************
    //                                          New Game Event Handler
    //*****************************************************************************************************************

    public void onHandleNewGame(){
        //Deselecting all radio buttons if any radio button was already selected
        rectRadioButton.setSelected(false);
        circleRadioButton.setSelected(false);


        //Resetting total guess count and correct guess count to 0 and displaying it to the TextFields
        totalGuessCount = 0;
        correctGuessCount = 0;

        displayGuessScores(totalGuessCount, correctGuessCount);



        //Clearing out list view
        listView.getItems().clear();


        //Starting New Game will delete all data from the database.
        try {
            String dbFilePath = ".//Guesses.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
            Connection conn = DriverManager.getConnection(databaseURL);
            String sql = "DELETE FROM Guesses";
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = conn.prepareStatement(sql);
                int rowsDeleted = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

    }





    //*****************************************************************************************************************
    //                                          Exit Menu Event Handler
    //*****************************************************************************************************************

    public void onHandleExitMenu(){
        Platform.exit();
    }








    //*****************************************************************************************************************
    //                                          Guess Button Event Handler
    //*****************************************************************************************************************


    public void onHandleGuessButton(){
        //Creating random number generator from 0-1
        int randomNumber = rand.nextInt(2);


        //Randomly choosing a shape.
                //Rectangle is 0, Circle is 1
        String[] shapes = {"RECTANGLE", "CIRCLE"};
        String randomShape = shapes[randomNumber];
        String guessedShape;



        //If user chooses neither shape, nothing happens.
        if(!rectRadioButton.isSelected() && !circleRadioButton.isSelected()){
            System.out.println("None chosen, guess a shape.");
        }




        //****************************************************************
        //        Checking to see if user guessed Rectangle Correctly
        //****************************************************************



        //If rectangle is selected and the correct random shape is set to "RECTANGLE", user guessed correctly.
        else if(rectRadioButton.isSelected() && randomShape.equals(shapes[0])){

            //Incrementing both Total and Guess counts by 1
            totalGuessCount++;
            correctGuessCount++;


            //Assigning user's guess shape to the correct guess shape, RECTANGLE, since they guessed correctly.
            guessedShape = randomShape;


            //Move Green Rectangle Animation gets played
            moveGreenRectangle();


            //After incrementing total guess count and correct guess count by 1, it will get updated in the textfields
            displayGuessScores(totalGuessCount, correctGuessCount);


            //total guess count, correct shape, and guessed shaped stored in database.
            storeGuessesToDatabase(totalGuessCount, randomShape, guessedShape); //*******************************

        } //end of 1st "else if"




        //****************************************************************
        //          Checking to see if user guessed Circle Correctly
        //****************************************************************



        //if circle is selected and the correct random shape is set to "CIRCLE", user guessed correctly.
        else if(circleRadioButton.isSelected() && randomShape.equals(shapes[1])){

            //Incrementing both Total and Guess counts by 1
            totalGuessCount++;
            correctGuessCount++;


            //Assigning user's guess shape to the correct guess shape, CIRCLE, since they guessed correctly.
            guessedShape = randomShape;


            //Move Green Circle Animation gets played
            moveGreenCircle();


            //After incrementing total guess count and correct guess count by 1, it will get updated in the textfields
            displayGuessScores(totalGuessCount, correctGuessCount);


            //total guess count, correct shape, and guessed shaped stored in database.
            storeGuessesToDatabase(totalGuessCount, randomShape, guessedShape); //*******************************

        } //end of 2nd else if



        else{                          //if user guessed wrong, the bottom section runs

            //totalGuessCount gets incremented except for correctGuessCount since user guessed wrong shape.
                //Then updated to the TextFields

            totalGuessCount++;
            displayGuessScores(totalGuessCount, correctGuessCount);



            //****************************************************************
            //          Checking to see if user guessed Rectangle while the correct shape was Circle
            //****************************************************************



            //if user chose rectangle and the correct random Shape is set to "CIRCLE", then user guessed wrong.
            if(rectRadioButton.isSelected() && randomShape.equals(shapes[1])){

                //Move Red Circle Animations gets played
                moveRedCircle();


                //Setting user's guessed shape to RECTANGLE, since they selected Rectangle button.
                guessedShape = shapes[0];


                //total guess count, correct shape, and guessed shaped stored in database.
                storeGuessesToDatabase(totalGuessCount, randomShape, guessedShape);//*******************************
            }



            //****************************************************************
            //          Checking to see if user guessed Circle while the correct shape was Rectangle
            //****************************************************************



            //if user chose circle and the correct random Shape is set to "RECTANGLE", user chose wrong.
            else if (circleRadioButton.isSelected() && randomShape.equals(shapes[0])) {

                //Move Red Rectangle Animation gets played
                moveRedRectangle();


                //Setting user's guessed shape to CIRCLE since they selected Circle button.
                guessedShape = shapes[1];


                //total guess count, correct shape, and guessed shaped stored in database.
                storeGuessesToDatabase(totalGuessCount, randomShape, guessedShape); //*******************************
            }


        } //end of else statement


    }






    //*****************************************************************************************************************
    //                                      Show Guesses From Database Event Handler
    //*****************************************************************************************************************



    public void onHandleShowGuessesFromDB(){
        ObservableList<String> items = listView.getItems();
        items.clear();

        try {
            String dbFilePath = ".//Guesses.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
            Connection conn = DriverManager.getConnection(databaseURL);

            String tableName = "Guesses";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);

            while (result.next()) {
                int guess_number = result.getInt("Guess_Number");
                String correct_shape = result.getString("Correct_Shape");
                String guessed_shape = result.getString("Guessed_Shape");
                items.add(guess_number + ", correct: " + correct_shape + ", guessed: " + guessed_shape);
            }

        } catch (SQLException except) {
            except.printStackTrace();
        }   //end of try catch block

    }





    //*****************************************************************************************************************
    //                                      Method to Store Guesses to Database
    //*****************************************************************************************************************


    public void storeGuessesToDatabase(int totalGuessCount, String correctShape, String guessedShape){
        //After the guesses have been made, total guess count, the correct shape,
            //and user's guessed shape are stored into the DB
        try {
            String dbFilePath = ".//Guesses.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
            Connection conn = DriverManager.getConnection(databaseURL);

            String sql = "INSERT INTO Guesses (Guess_Number, Correct_Shape, Guessed_Shape) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = null;

            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, totalGuessCount);
                preparedStatement.setString(2, correctShape);
                preparedStatement.setString(3, guessedShape);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }       //end of first "try"
        catch (SQLException except){
            except.printStackTrace();
        }
    }






    //*****************************************************************************************************************
    //                                      Method to display updated guess scores
    //*****************************************************************************************************************

    public void displayGuessScores(int totalGuessCount, int correctGuessCount){
        //Updating the new guess scores after they got incremented.

        totalGuessesTextField.setText(Integer.toString(totalGuessCount));
        correctGuessesTextField.setText(Integer.toString(correctGuessCount));

    }






    //*****************************************************************************************************************
    //                                    Shape Animations for Rectangle and Circle
    //*****************************************************************************************************************


    //********************************
    //Move Green Rectangle Animation
    //********************************
    public void moveGreenRectangle(){
        //When animation starts, guess button becomes disabled.
        guessButton.setDisable(true);


        //Opacity Change
        FadeTransition fadeTransition =
                new FadeTransition(Duration.seconds(2), rectangleShape);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setOnFinished(e -> rectangleShape.setOpacity(0.0));
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);


        //Fill
        FillTransition fillTransition =
                new FillTransition(Duration.seconds(2), rectangleShape);
        fillTransition.setToValue(Color.GREEN);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);


        //Translate
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), rectangleShape);
        translateTransition.setByX(100);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        //After animation ends, guess button gets enabled.
        translateTransition.setOnFinished(e -> guessButton.setDisable(false));


        ParallelTransition parallelTransition =
                new ParallelTransition(fadeTransition, fillTransition, translateTransition);
        parallelTransition.play();


    }



    //********************************
    //Move Red Rectangle Animation
    //********************************
    public void moveRedRectangle(){
        //When animation starts, guess button becomes disabled.
        guessButton.setDisable(true);


        //Opacity Change
        FadeTransition fadeTransition =
                new FadeTransition(Duration.seconds(2), rectangleShape);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setOnFinished(e -> rectangleShape.setOpacity(0.0));
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);


        //Fill
        FillTransition fillTransition =
                new FillTransition(Duration.seconds(2), rectangleShape);
        fillTransition.setToValue(Color.RED);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);


        //Translate
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), rectangleShape);
        translateTransition.setByX(100);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        //After animation ends, guess button gets enabled.
        translateTransition.setOnFinished(e -> guessButton.setDisable(false));


        ParallelTransition parallelTransition =
                new ParallelTransition(fadeTransition, fillTransition, translateTransition);
        parallelTransition.play();
    }



    //********************************
    //Move Green Circle Animation
    //********************************
    public void moveGreenCircle(){
        //When animation starts, guess button becomes disabled.
        guessButton.setDisable(true);


        //Opacity Change
        FadeTransition fadeTransition =
                new FadeTransition(Duration.seconds(2), circleShape);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setOnFinished(e -> circleShape.setOpacity(0.0));
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);


        //Fill
        FillTransition fillTransition =
                new FillTransition(Duration.seconds(2), circleShape);
        fillTransition.setToValue(Color.GREEN);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);


        //Translate
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), circleShape);
        translateTransition.setByX(100);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        //After animation ends, guess button gets enabled.
        translateTransition.setOnFinished(e -> guessButton.setDisable(false));


        ParallelTransition parallelTransition =
                new ParallelTransition(fadeTransition, fillTransition, translateTransition);
        parallelTransition.play();
    }



    //********************************
    //Move Red Circle Animation
    //********************************
    public void moveRedCircle(){
        //When animation starts, guess button becomes disabled.
        guessButton.setDisable(true);


        //Opacity Change
        FadeTransition fadeTransition =
                new FadeTransition(Duration.seconds(2), circleShape);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setOnFinished(e -> circleShape.setOpacity(0.0));
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);


        //Fill
        FillTransition fillTransition =
                new FillTransition(Duration.seconds(2), circleShape);
        fillTransition.setToValue(Color.RED);
        fillTransition.setCycleCount(2);
        fillTransition.setAutoReverse(true);


        //Translate
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), circleShape);
        translateTransition.setByX(100);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        //After animation ends, guess button gets enabled.
        translateTransition.setOnFinished(e -> guessButton.setDisable(false));


        ParallelTransition parallelTransition =
                new ParallelTransition(fadeTransition, fillTransition, translateTransition);
        parallelTransition.play();
    }


}