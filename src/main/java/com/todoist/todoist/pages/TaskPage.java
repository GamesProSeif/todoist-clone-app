package com.todoist.todoist.pages;

import com.mongodb.client.model.Updates;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.models.Task;
import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskPage extends Page {
    private final Task task;
    public TaskPage(TodoistApp app, Task task) {
        super(app);
        this.task = task;
    }

    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label(task.title);
        label.getStyleClass().addAll(
                "panel-heading","alert","text-heading","h4","b","btn-lg"
        );
        Button deleteTaskButton = new Button("Delete Task");
        deleteTaskButton.setOnAction(e -> {
            this.app.taskController.delete(task);
            this.app.switchPage("project:" + task.project.id);
            this.app.reload();
        });
        deleteTaskButton.getStyleClass().setAll("btn", "btn-danger");
        BorderPane taskHeaderPane = new BorderPane();
        taskHeaderPane.setLeft(label);
        taskHeaderPane.setRight(deleteTaskButton);
        pane.setTop(taskHeaderPane);

        Label sectionLabel = new Label(task.project.title + " / " + task.section.title);
        sectionLabel.setUnderline(true);
        sectionLabel.setStyle("-fx-text-fill: #2e6da5");
        sectionLabel.setOnMouseEntered(me -> app.panel.setCursor(Cursor.HAND));
        sectionLabel.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        sectionLabel.setOnMouseClicked(e ->
            this.app.switchPage("project:" + task.project.id));

        Label dueDateLabel = new Label("Due date: ");
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setValue(task.dueDate);
        dueDatePicker.setOnAction(e -> {
            var updates = Updates.set("date", dueDatePicker.getValue());
            task.dueDate = dueDatePicker.getValue();
            this.app.taskController.update(task, updates);
        });
        CheckBox checkedBox = new CheckBox();
        checkedBox.setSelected(task.checked);
        checkedBox.setOnAction(e -> {
            var updates = Updates.set("checked", checkedBox.isSelected());
            task.checked = checkedBox.isSelected();
            this.app.taskController.update(task, updates);
        });
        HBox dueDateHbox = new HBox(dueDateLabel, dueDatePicker, checkedBox);
        dueDateHbox.setSpacing(5);
        dueDateHbox.setAlignment(Pos.CENTER_LEFT);

        Label tagLabel = new Label("Labels:");
        HBox tagsPane = new HBox();
        tagsPane.setSpacing(10);
        this.app.tagController.list().forEach((tagId, tag) -> {
            if (!tag.project.id.equals(task.project.id))
                return;
            CheckBox tagChecked = new CheckBox();
            tagChecked.setSelected(task.tags.contains(tag));
            tagChecked.setOnAction(e -> {
                if (tagChecked.isSelected())
                    task.tags.add(tag);
                else
                    task.tags.remove(tag);
                ArrayList<ObjectId> updatedTags = new ArrayList<>();
                task.tags.forEach(mappedTag -> updatedTags.add(mappedTag.id));
                var updates = Updates.set("tags", updatedTags);
                this.app.taskController.update(task, updates);
            });
            Label tagTitleLabel = new Label(tag.title);
            tagTitleLabel.setStyle("-fx-background-color: #" + tag.color.substring(2));
            tagTitleLabel.getStyleClass().setAll("badge");

            HBox tagHbox = new HBox(tagChecked, tagTitleLabel);
            tagHbox.setSpacing(5);
            tagHbox.setAlignment(Pos.CENTER_LEFT);
            tagsPane.getChildren().add(tagHbox);
        });

        Label descriptionLabel = new Label("Description:");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setText(task.description);
        descriptionTextArea.setMaxWidth(750);
        Button saveDescriptionButton = new Button("Save");
        saveDescriptionButton.setOnAction(e -> {
            var updates = Updates.set("description", descriptionTextArea.getText());
            task.description = descriptionTextArea.getText();
            this.app.taskController.update(task, updates);
        });
        saveDescriptionButton.getStyleClass().setAll("btn", "btn-primary");
        saveDescriptionButton.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(
                sectionLabel,
                dueDateHbox,
                tagLabel,
                tagsPane,
                descriptionLabel,
                descriptionTextArea,
                saveDescriptionButton
        );
        vbox.setPadding(new Insets(0, 0, 0, 17));
        vbox.setSpacing(10);
        pane.setCenter(vbox);
        return pane;
    }
}
