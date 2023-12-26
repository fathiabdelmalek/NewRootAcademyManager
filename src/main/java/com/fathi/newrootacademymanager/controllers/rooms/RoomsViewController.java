package com.fathi.newrootacademymanager.controllers.rooms;

import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.services.CRUDService;
import com.fathi.newrootacademymanager.services.LoggingService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RoomsViewController {
    @FXML
    private TextField searchText;
    @FXML
    private ListView<Room> listView;
    @FXML
    private TextField codeText;
    private int id;

    @FXML
    void initialize() {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                id = newSelection.getId();
                codeText.setText(newSelection.getCode());
            }
        });
        refreshTable();
    }

    @FXML
    void searchAction() {
        FilteredList<Room> filter = new FilteredList<>(listView.getItems(), e -> true);
        searchText.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicate -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String key = newValue.toLowerCase();
                return predicate.getCode().toLowerCase().contains(key);
            });
        });
        listView.setItems(filter);
    }

    @FXML
    void insertAction() {
        if (codeText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Room room = new Room(codeText.getText());
            CRUDService.create(room);
            refreshTable();
            LoggingService.add(room + " have been added");
        }
    }

    @FXML
    void updateAction() {
        if (codeText.getText().isEmpty())
            LoggingService.error("You should insert all required fields");
        else {
            Room room = CRUDService.readById(Room.class, id);
            String oldCode = room.toString();
            room.setCode(codeText.getText());
            CRUDService.update(room);
            refreshTable();
            LoggingService.update("Room code have been changed from " + oldCode + " to " + room);
        }
    }

    @FXML
    void deleteAction() {
        Room room = CRUDService.readById(Room.class, id);
        CRUDService.delete(room);
        refreshTable();
        LoggingService.delete(room + " have been deleted");
    }

    @FXML
    void clearAction() {
        codeText.clear();
    }

    private void refreshTable() {
        listView.setItems(CRUDService.readAll(Room.class));
    }
}
