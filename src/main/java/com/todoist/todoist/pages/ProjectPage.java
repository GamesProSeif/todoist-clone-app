package com.todoist.todoist.pages;

import com.mongodb.client.model.Updates;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        BorderPane headerPane = new BorderPane();
        headerPane.setMinHeight(43);
        Label label = new Label(project.title);
        label.setOnMouseEntered(me -> app.panel.setCursor(Cursor.TEXT));
        label.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        label.getStyleClass().addAll(
                "panel-heading","alert","text-heading","h4","b","btn-lg"
        );
        label.setOnMouseClicked(e -> {
            TextField projectTitleTextField = new TextField();
            projectTitleTextField.setFont(label.getFont());
            projectTitleTextField.setPromptText("Project name");
            projectTitleTextField.setText(project.title);
            headerPane.setLeft(projectTitleTextField);
            projectTitleTextField.requestFocus();
            projectTitleTextField.setOnKeyPressed(ke -> {
                String projectTitle = projectTitleTextField.getText();
                if (projectTitle == null || projectTitle.equals(""))
                    return;
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    project.title = projectTitle;
                    var updates = Updates.set("title", project.title);
                    this.app.projectController.update(project, updates);
                    this.app.reload();
                }
            });
            projectTitleTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal)
                    headerPane.setLeft(label);
            });
        });

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
        BorderPane sectionHeaderPane = new BorderPane();
        sectionHeaderPane.setMinHeight(23);
        Label sectionTitleLabel = new Label(section.title);
        sectionTitleLabel.setOnMouseEntered(me -> app.panel.setCursor(Cursor.TEXT));
        sectionTitleLabel.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        sectionTitleLabel.setOnMouseClicked(e -> {
            TextField sectionTitleTextField = new TextField();
            sectionTitleTextField.setFont(sectionTitleLabel.getFont());
            sectionTitleTextField.setPromptText("Section name");
            sectionTitleTextField.setText(section.title);
            sectionHeaderPane.setLeft(sectionTitleTextField);
            sectionTitleTextField.requestFocus();
            sectionTitleTextField.setOnKeyPressed(ke -> {
                String sectionTitle = sectionTitleTextField.getText();
                if (sectionTitle == null || sectionTitle.equals(""))
                    return;
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    section.title = sectionTitle;
                    var updates = Updates.set("title", section.title);
                    this.app.sectionController.update(section, updates);
                    this.app.reload();
                }
            });
            sectionTitleTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal)
                    sectionHeaderPane.setLeft(sectionTitleLabel);
            });
        });
        ImageView iv = new ImageView(new Image("/trash-fill.red.png"));
        iv.setFitHeight(15);
        iv.setFitWidth(15);
        iv.setOnMouseEntered(me -> app.panel.setCursor(Cursor.HAND));
        iv.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        iv.setOnMouseClicked(e -> {
            hbox.getChildren().remove(vbox);
            tasks.forEach((taskId, task) -> {
                if (task.section.id.equals(section.id))
                    this.app.taskController.delete(task);
            });
            this.app.sectionController.delete(section);
            sections.remove(section.id);
        });
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
                        false, this.project,
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
        var taskVbox = renderTask(this.app, task, tags);
        if (index == -1)
            vbox.getChildren().add(taskVbox);
        else
            vbox.getChildren().add(index, taskVbox);
    }

    public static VBox renderTask(TodoistApp app, Task task, HashMap<ObjectId, Tag> tags) {
        VBox taskVbox = new VBox();
        taskVbox.setOnMouseClicked(me -> {
            if ((me.getTarget() instanceof VBox))
                app.switchPage("task:" + task.id);
        });
        taskVbox.setOnMouseEntered(me -> app.panel.setCursor(Cursor.HAND));
        taskVbox.setOnMouseExited(me -> app.panel.setCursor(Cursor.DEFAULT));
        taskVbox.setStyle("-fx-background-color: rgba(0,0,0,0.15); -fx-background-radius: 5px");
//        taskVbox.getStyleClass().setAll("alert");
        taskVbox.setSpacing(5);
        taskVbox.setPadding(new Insets(10));
        HBox tagsHBox;
        if (task.tags.size() > 0) {
            tagsHBox = new HBox();
            task.tags.forEach(tagId -> {
                var tag = tags.get(tagId);
                var tagLabel = new Label(tag.title);
                tagLabel.setStyle("-fx-background-radius: 5px; -fx-background-color: #" + tag.color);
                tagLabel.getStyleClass().setAll("badge");
                tagsHBox.getChildren().add(tagLabel);
                tagLabel.setOnMouseClicked(me -> app.switchPage("task:" + task.id));
            });
        } else {
            tagsHBox = null;
        }
        if (tagsHBox != null) {
            tagsHBox.setSpacing(5);
            taskVbox.getChildren().add(tagsHBox);
        }
        Label taskLabel = new Label(task.title);
        taskLabel.setOnMouseClicked(e -> app.switchPage("task:" + task.id));
        taskLabel.getStyleClass().addAll("h5");
        taskVbox.getChildren().add(taskLabel);
        if (task.dueDate != null) {
            var formatter = DateTimeFormatter.ofPattern("MMM d");
            Label taskDueDateLabel = new Label(formatter.format(task.dueDate));
            taskDueDateLabel.setStyle("-fx-background-radius: 5px;");
            double opacity;
            if (task.checked) {
                opacity = 0.6;
                taskDueDateLabel.getStyleClass().setAll("lbl-success");
            }
            else {
                opacity = 1;
                var diff = task.dueDate.compareTo(LocalDate.now());
                if (diff < 0)
                    taskDueDateLabel.getStyleClass().setAll("lbl-danger");
                else if (diff < 2)
                    taskDueDateLabel.getStyleClass().setAll("lbl-warning");
                else
                    taskDueDateLabel.getStyleClass().setAll("lbl-default");
            }
            taskDueDateLabel.setFont(new Font(12));
            taskDueDateLabel.setPadding(new Insets(3, 5, 3, 5));
            taskVbox.getChildren().add(taskDueDateLabel);
            taskDueDateLabel.setOpacity(opacity);
            taskDueDateLabel.setOnMouseEntered(e -> {
                taskDueDateLabel.setOpacity(0.8);
            });
            taskDueDateLabel.setOnMouseExited(e -> {
                taskDueDateLabel.setOpacity(opacity);
            });
            taskDueDateLabel.setOnMouseClicked(e -> {
                task.checked = !task.checked;
                var updates = Updates.set("checked", task.checked);
                app.taskController.update(task, updates);
                app.reload();
            });
        }
        return taskVbox;
    }
}
