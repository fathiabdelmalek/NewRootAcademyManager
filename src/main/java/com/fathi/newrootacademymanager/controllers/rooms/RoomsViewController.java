package com.fathi.newrootacademymanager.controllers.rooms;

import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
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
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (room == null || empty) setText(null);
                else setText(room.getCode());
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            id = newSelection.getId();
            codeText.setText(newSelection.getCode());
        });
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
        if (codeText.getText().isEmpty()) System.out.println("You should insert all required data");
        else {
            CRUDService.create(new Room(codeText.getText()));
            refreshTable();
        }
    }

    @FXML
    void updateAction() {
        if (codeText.getText().isEmpty()) System.out.println("You should insert all required data");
        else {
            Room room = CRUDService.readById(Room.class, id);
            assert room != null;
            room.setCode(codeText.getText());
            CRUDService.update(room);
            refreshTable();
        }
    }

    @FXML
    void deleteAction() {
        CRUDService.delete(CRUDService.readById(Room.class, id));
        refreshTable();
    }

    @FXML
    void clearAction() {
        codeText.clear();
    }

    private void refreshTable() {
        listView.setItems(CRUDService.readAll(Room.class));
    }
}
