package org.example.ballsort.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.ballsort.Database.DAO;
import org.example.ballsort.model.Score;

import java.sql.SQLException;
import java.util.List;


public class ScoresController {
    @FXML private ComboBox<String> levelsChoiceBox;
    @FXML private TableView<Score> scoreTable;
    @FXML private TableColumn<Score, Integer> rankColumn;
    @FXML private TableColumn<Score, Integer> scoreColumn;
    @FXML private TableColumn<Score, String> userColumn;
    private boolean scoreTableOpenned = false;

    public void loadScores(int level) throws SQLException {

            scoreTableOpenned = true;
            rankColumn.setCellValueFactory(data ->data.getValue().rankProperty().asObject());
            scoreColumn.setCellValueFactory(data -> data.getValue().valueProperty().asObject());
            userColumn.setCellValueFactory(data -> data.getValue().userProperty());

            List<Score> scores = DAO.getListScores(level);
            ObservableList<Score> observableScores = FXCollections.observableArrayList(scores);
            scoreTable.setItems(observableScores);

    }


    @FXML
    public void initialize() {
        levelsChoiceBox.setOnAction(event -> {
            String levelSelected = levelsChoiceBox.getSelectionModel().getSelectedItem();
            if(levelSelected.equals("Level 1")){
                try {
                    loadScores(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(levelSelected.equals("Level 2")){
                try {
                    loadScores(2);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    loadScores(3);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
