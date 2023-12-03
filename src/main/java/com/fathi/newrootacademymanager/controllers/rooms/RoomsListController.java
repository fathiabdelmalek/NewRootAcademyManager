package com.fathi.newrootacademymanager.controllers.rooms;

import com.fathi.newrootacademymanager.helpers.ViewFactory;
import com.fathi.newrootacademymanager.models.Room;
import com.fathi.newrootacademymanager.models.StudentView;
import com.fathi.newrootacademymanager.services.CRUDService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

public class RoomsListController {
    @FXML
    public ListView<String> listView;

    @FXML
    protected void initialize() {
        loadData();
    }

    public void addAction() {
//        ViewFactory.createAddView("/com/fathi/newrootacademymanager/views/rooms/add-room-view.fxml", "Add Room");
        refreshTable();
    }

    private void loadData() {
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(CRUDService.readAll(Room.class));
        ObservableList<String> roomsNames = FXCollections.observableArrayList();
        for (Room room : rooms) {
            roomsNames.add(room.getCode());
        }
        listView.setItems(roomsNames);
    }
}
