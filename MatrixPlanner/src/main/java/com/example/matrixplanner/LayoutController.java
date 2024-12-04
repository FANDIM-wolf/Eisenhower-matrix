package com.example.matrixplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LayoutController {
    @FXML
    protected void handleButton1Action(ActionEvent event) {
        System.out.println("Button 1 clicked");
    }

    @FXML
    protected void handleButton2Action(ActionEvent event) {
        System.out.println("Button 2 clicked");
    }

    @FXML
    protected void handleButton3Action(ActionEvent event) {
        System.out.println("Button 3 clicked");
    }

    @FXML
    protected void handleConnectAction(ActionEvent event) {
        System.out.println("Connect button clicked");
    }
}
