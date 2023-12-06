package com.fathi.newrootacademymanager.controllers.others;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import com.fathi.newrootacademymanager.models.Grade;
import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GradesRoomsViewController {
    @FXML
    public TextField searchGradeText;
    @FXML
    public TextField searchRoomText;
    @FXML
    public TableView<Grade> tableView;
    @FXML
    public ListView<Room> listView;
    @FXML
    public TextField levelText;
    @FXML
    public TextField yearText;
    @FXML
    public TextField codeText;
    private int gradeId, roomId;

    public void initialize() {
        loadUI();
    }

    public void searchGradeAction() {
        FilteredList<Grade> filter = new FilteredList<>(tableView.getItems(), e -> true);
        searchGradeText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getLevel().toString().toLowerCase().contains(key) ||
                        String.valueOf(predicate.getYear()).contains(key);
            });
        });
        tableView.setItems(filter);
    }

    public void searchRoomAction() {
        FilteredList<Room> filter = new FilteredList<>(listView.getItems(), e -> true);
        searchRoomText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getCode().toLowerCase().contains(key);
            });
        });
        listView.setItems(filter);
    }

    public void insertGrade() {
        if (levelText.getText().isEmpty() ||
                yearText.getText().isEmpty()) {
            System.out.println("You should insert all required data");
        } else {
            CRUDService.create(new Grade(Level.valueOf(levelText.getText()), Integer.parseInt(yearText.getText())));
            refreshTable();
        }
    }

    public void updateGrade() {
        if (levelText.getText().isEmpty() ||
                yearText.getText().isEmpty()) {
            System.out.println("You should insert all required data");
        } else {
            Grade grade = CRUDService.readById(Grade.class, gradeId);
            assert grade != null;
            grade.setLevel(Level.valueOf(levelText.getText()));
            grade.setYear(Integer.parseInt(yearText.getText()));
            CRUDService.update(grade);
            refreshTable();
        }
    }

    public void deleteGrade() {
        CRUDService.delete(CRUDService.readById(Grade.class, gradeId));
        refreshTable();
    }

    public void clearGrade() {
        levelText.clear();
        yearText.clear();
    }

    public void insertRoom() {
        if (codeText.getText().isEmpty()) System.out.println("You should insert all required data");
        else {
            CRUDService.create(new Room(codeText.getText()));
            refreshTable();
        }
    }

    public void updateRoom() {
        if (codeText.getText().isEmpty()) System.out.println("You should insert all required data");
        else {
            Room room = CRUDService.readById(Room.class, roomId);
            assert room != null;
            room.setCode(codeText.getText());
            CRUDService.update(room);
            refreshTable();
        }
    }

    public void deleteRoom() {
        CRUDService.delete(CRUDService.readById(Room.class, roomId));
        refreshTable();
    }

    public void clearRoom() {
        codeText.clear();
    }

    private void loadUI() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            gradeId = newSelection.getId();
            levelText.setText(newSelection.getLevel().toString());
            yearText.setText(String.valueOf(newSelection.getYear()));
        });
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (room == null || empty) setText(null);
                else setText(room.getCode());
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            roomId = newSelection.getId();
            codeText.setText(newSelection.getCode());
        });
        refreshTable();
    }

    private void refreshTable() {
        tableView.setItems(CRUDService.readAll(Grade.class));
        listView.setItems(CRUDService.readAll(Room.class));
    }
}
