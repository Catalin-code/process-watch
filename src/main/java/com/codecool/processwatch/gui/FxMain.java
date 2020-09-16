package com.codecool.processwatch.gui;

import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.queries.FilterByName;
import com.codecool.processwatch.queries.FilterByPPID;
import com.codecool.processwatch.queries.SelectAll;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * The JavaFX application Window.
 */
public class FxMain extends Application {
    private static final String TITLE = "Process Watch";

    private App app;

    /**
     * Entrypoint for the javafx:run maven task.
     *
     * @param args an array of the command line parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Build the application window and set up event handling.
     *
     * @param primaryStage a stage created by the JavaFX runtime.
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE);

        ObservableList<ProcessView> displayList = observableArrayList();
        app = new App(displayList);
        // TODO: Factor out the repetitive code
        var tableView = new TableView<ProcessView>(displayList);
        var pidColumn = new TableColumn<ProcessView, Long>("Process ID");
        pidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("pid"));
        var parentPidColumn = new TableColumn<ProcessView, Long>("Parent Process ID");
        parentPidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("parentPid"));
        var userNameColumn = new TableColumn<ProcessView, String>("Owner");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("userName"));
        var processNameColumn = new TableColumn<ProcessView, String>("Name");
        processNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("processName"));
        var argsColumn = new TableColumn<ProcessView, String>("Arguments");
        argsColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("args"));
        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(ignoreEvent -> {
            System.out.println("Refresh button pressed");
            app.setQuery(new SelectAll());
        });

        TextField userFilterTextField = new TextField();

        var userFilterButton = new Button("Filter by user");
        userFilterButton.setOnAction(ignoreEvent -> {
            System.out.println("User filter button pressed");
            app.setQuery(new FilterByName(userFilterTextField.getText()));
        });

        TextField ppidFilterTextField = new TextField();

        var ppidFilterButton = new Button("Filter by parentID");
        ppidFilterButton.setOnAction(ignoreEvent -> {
            System.out.println("PPID filter button pressed");
            app.setQuery(new FilterByPPID(ppidFilterTextField.getText()));
        });

        var deleteButton = new Button("End process");
        deleteButton.setOnAction(ignoreEvent ->{
            System.out.println("Delete button pressed");
            System.out.println();
        });

        deleteButton.setOnAction(e -> buttonClicked());

        private void buttonClicked(){
            String message = "";
            ObservableList<String> moves;
            moves = tableView.getSelectionModel().getSelectedItems();
            for(String m: moves){
                message += m + "\n";
            }
            System.out.println(message);
        }

        var box = new VBox();
        var scene = new Scene(box, 640, 480);
        var elements = box.getChildren();
        elements.addAll(refreshButton,
                        userFilterButton,
                        userFilterTextField,
                        ppidFilterButton,
                        ppidFilterTextField,
                        deleteButton,
                        tableView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
