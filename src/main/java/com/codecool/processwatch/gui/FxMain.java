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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

        GridPane gridUp = new GridPane();
        gridUp.setHgap(10);
        gridUp.setVgap(12);

        GridPane gridDown = new GridPane();
        gridDown.setHgap(10);
        gridDown.setVgap(12);

        HBox refreshButtons = new HBox();
        refreshButtons.setSpacing(10.0);
        var refreshButton = new Button("Refresh");
        refreshButton.setOnAction(ignoreEvent -> {
            System.out.println("Refresh button pressed");
            app.setQuery(new SelectAll());
        });
        var refreshButtonInfo = new Button("?");
        refreshButtons.getChildren().addAll(refreshButton, refreshButtonInfo);

        HBox userFilterButtons = new HBox();
        userFilterButtons.setSpacing(10.0);
        TextField userFilterTextField = new TextField();
        var userFilterButton = new Button("Filter by user");
        userFilterButton.setOnAction(ignoreEvent -> {
            System.out.println("User filter button pressed");
            app.setQuery(new FilterByName(userFilterTextField.getText()));
        });
        var userFilterButtonInfo = new Button("?");
        userFilterButtons.getChildren().addAll(userFilterButton, userFilterButtonInfo);

        HBox ppidFilterButtons = new HBox();
        ppidFilterButtons.setSpacing(10.0);
        TextField ppidFilterTextField = new TextField();
        var ppidFilterButton = new Button("Filter by parentID");
        ppidFilterButton.setOnAction(ignoreEvent -> {
            System.out.println("PPID filter button pressed");
            app.setQuery(new FilterByPPID(ppidFilterTextField.getText()));
        });
        var ppidFilterButtonInfo = new Button("?");
        ppidFilterButtons.getChildren().addAll(ppidFilterButton, ppidFilterButtonInfo);

        HBox deleteButtons = new HBox();
        deleteButtons.setSpacing(10.0);
        var deleteButton = new Button("End process");
        deleteButton.setOnAction(ignoreEvent ->{
            System.out.println("Delete button pressed");
            for (ProcessView p: tableView.getSelectionModel().getSelectedItems()){
                ProcessHandle handler = ProcessHandle.of(p.getPid()).get();
                handler.destroy();
                app.setQuery(new SelectAll());
            }
        });
        var deleteButtonInfo = new Button("?");
        deleteButtons.getChildren().addAll(deleteButton, deleteButtonInfo);

        HBox aboutButtons = new HBox();
        aboutButtons.setSpacing(10.0);
        var aboutButton = new Button("About");
        aboutButton.setOnAction(ignoreEvent ->{
            System.out.println("About button pressed");
        });
        var aboutButtonInfo = new Button("?");
        aboutButtons.getChildren().addAll(aboutButton, aboutButtonInfo);

        gridUp.add(refreshButtons, 0, 0 , 1, 1);
        gridUp.add(userFilterButtons, 0, 1 , 1, 1);
        gridUp.add(userFilterTextField, 0, 2);
        gridUp.add(ppidFilterButtons, 0, 3 , 1, 1);
        gridUp.add(ppidFilterTextField, 0, 4);
        gridDown.add(deleteButtons, 0, 0, 1, 1);
        gridDown.add(aboutButtons, 0, 1, 1, 1);


        var box = new VBox();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        var scene = new Scene(box, 640, 480);
        var elements = box.getChildren();
        elements.addAll(gridUp, tableView, gridDown);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
