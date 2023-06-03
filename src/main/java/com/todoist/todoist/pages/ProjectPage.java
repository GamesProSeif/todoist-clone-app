package com.todoist.todoist.pages;

import com.todoist.todoist.modals.AddSectionModal;
import com.todoist.todoist.modals.EditTagsModal;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Section;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.models.Task;
import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectPage extends Page {
    public final Project project;
    HashMap<ObjectId, Tag> tags;
    HashMap<ObjectId, Section> sections;
    HashMap<ObjectId, Task> tasks;

    public ProjectPage(TodoistApp app, Project project) {
        super(app);
        this.project = project;
        tags = new HashMap<>();
        sections = new HashMap<>();
        tasks = new HashMap<>();
    }

    private void fetchData() {
        this.app.tagController.list().forEach((tagId, tag) -> {
            if (tag.project.id.equals(project.id))
                tags.put(tagId, tag);
        });

        this.app.sectionController.list().forEach((sectionId, section) -> {
            if (section.project.id.equals(project.id))
                sections.put(sectionId, section);
        });

        this.app.taskController.list().forEach((taskId, task) -> {
            if (task.project.id.equals(project.id))
                tasks.put(taskId, task);
        });
    }

    @Override
    public Pane getContent() {
        fetchData();

        BorderPane pane = new BorderPane();
        Label label = new Label(project.title);
        label.getStyleClass().addAll(
                "panel-heading","alert","text-heading","h4","b","btn-lg"
        );

        Button deleteProjectButton = new Button("Delete Project");
        deleteProjectButton.getStyleClass().setAll("btn", "btn-danger");
        deleteProjectButton.setOnAction(e -> {
            this.app.switchPage("Dashboard");
            tasks.forEach((taskId, task) -> this.app.taskController.delete(task));
            tags.forEach((tagId, tag) -> this.app.tagController.delete(tag));
            sections.forEach((sectionId, section) -> this.app.sectionController.delete(section));
            this.app.projectController.delete(project);
            this.app.reload();
        });

        Button editTagsButton = new Button("Edit Tags");
        editTagsButton.getStyleClass().setAll("btn", "btn-warning");
        editTagsButton.setOnAction(e ->
            (new EditTagsModal(this.app, project, tags)).getContent().show());
        BorderPane headerPane = new BorderPane();
        headerPane.setLeft(label);
        HBox buttonHBox = new HBox(editTagsButton, deleteProjectButton);
        buttonHBox.setSpacing(10);
        headerPane.setRight(buttonHBox);
        pane.setTop(headerPane);

        HBox hbox = new HBox();
        ScrollPane sp = new ScrollPane();
        sp.setContent(hbox);
        sp.setBackground(Background.fill(Color.rgb(255, 255, 255)));
        sp.getStyleClass().setAll("edge-to-edge");
        hbox.setPadding(new Insets(10, 0, 0, 0));
        pane.setCenter(sp);
        hbox.setSpacing(10);
        hbox.setFillHeight(false);

        sections.forEach((sectionId, section) -> renderSection(hbox, section));

        Button addSectionButton = new Button("+ New Section");
        addSectionButton.setPrefSize(200, 30);
        addSectionButton.setOnAction(e -> (new AddSectionModal(this.app, this)).getContent().show() );
        addSectionButton.getStyleClass().setAll("btn", "btn-primary");
        hbox.getChildren().add(addSectionButton);

        return pane;
    }

    private void renderSection(HBox hbox, Section section) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: rgba(0,0,0,0.1)");
        vbox.getStyleClass().setAll("alert");
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setMinWidth(200);
        hbox.getChildren().add(vbox);
        Label sectionTitleLabel = new Label(section.title);
        ImageView iv = new ImageView(new Image("/trash-fill.red.png"));
        iv.setFitHeight(15);
        iv.setFitWidth(15);
        iv.setOnMouseEntered(me -> app.panel.setCursor(Cursor.HAND));
        iv.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        iv.setOnMouseClicked(e -> {
            tasks.forEach((taskId, task) -> {
                if (task.section.id.equals(section.id))
                    this.app.taskController.delete(task);
            });
            this.app.sectionController.delete(section);
            sections.remove(section.id);
            hbox.getChildren().remove(vbox);
        });
        BorderPane sectionHeaderPane = new BorderPane();
        sectionHeaderPane.setLeft(sectionTitleLabel);
        sectionHeaderPane.setRight(iv);
        sectionTitleLabel.getStyleClass().setAll("b");
        vbox.getChildren().add(sectionHeaderPane);
        tasks.forEach((taskId, task) -> {
            if (!task.section.id.equals(section.id))
                return;
            renderTask(vbox, task, -1);
        });
        TextField newTaskTextField = new TextField();
        newTaskTextField.setPromptText("Add Task...");
        newTaskTextField.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                String taskTitle = newTaskTextField.getText();
                if (taskTitle == null || taskTitle.equals(""))
                    return;
                Task task = new Task(
                        taskTitle, null, null,
                        false, null, this.project,
                        section, new ArrayList<>()
                );
                this.app.taskController.insert(task);
                newTaskTextField.setText("");
                renderTask(vbox, task, vbox.getChildren().size() - 1);
                this.app.loadPages();
            }
        });
        vbox.getChildren().add(newTaskTextField);
    }

    private void renderTask(VBox vbox, Task task, int index) {
        VBox taskVbox = new VBox();
        taskVbox.setOnMouseClicked(me -> app.switchPage("task:" + task.id));
        taskVbox.setOnMouseEntered(me -> app.panel.setCursor(Cursor.HAND));
        taskVbox.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        taskVbox.setStyle("-fx-background-color: rgba(0,0,0,0.15); -fx-background-radius: 5px");
//        taskVbox.getStyleClass().setAll("alert");
        taskVbox.setSpacing(5);
        taskVbox.setPadding(new Insets(10));
        HBox tagsHBox = null;
        if (task.tags.size() > 0) {
            tagsHBox = new HBox();
            task.tags.forEach(tag -> {
                var tagLabel = new Label(tag.title);
                tagLabel.setStyle("-fx-background-color: #" + tag.color);
                tagLabel.getStyleClass().setAll("badge");
            });
        }
        if (tagsHBox != null)
            taskVbox.getChildren().add(tagsHBox);
        Label taskLabel = new Label(task.title);
        taskVbox.getChildren().add(taskLabel);
        if (index == -1)
            vbox.getChildren().add(taskVbox);
        else
            vbox.getChildren().add(index, taskVbox);
    }
}
