package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Section;
import com.todoist.todoist.models.Task;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class TaskController extends BaseController<Task> {
    private final ProjectController projectController;
    private final TagController tagController;
    private final SectionController sectionController;

    public TaskController(
            MongoDatabase db,
            ProjectController projectController,
            TagController tagController,
            SectionController sectionController
    ) {
        super(db, "tasks");
        this.projectController = projectController;
        this.tagController = tagController;
        this.sectionController = sectionController;
    }

    @Override
    protected Task documentToModel(Document doc) {
        Project project = projectController.findById(doc.getObjectId("project"));
        Section section = null;
        if (doc.get("section") != null)
            section = sectionController.findById(doc.getObjectId("section"));
        ArrayList<Tag> tags = new ArrayList<Tag>();
        List<ObjectId> labelsIds = doc.getList("labels", ObjectId.class);

        labelsIds.forEach(id -> {
            tags.add(tagController.findById(id));
        });

        return new Task(
                doc.getObjectId("_id"),
                doc.getString("title"),
                doc.getString("description"),
                doc.getDate("date"),
                doc.getBoolean("checked"),
                doc.getString("attachment"),
                project,
                section,
                tags
        );
    }

    @Override
    protected Document modelToDocument(Task task) {
        ArrayList<ObjectId> labelsList = new ArrayList<>();

        task.tags.forEach(tag -> labelsList.add(tag.id));

        return new Document()
                .append("_id", task.id)
                .append("title", task.title)
                .append("description", task.description)
                .append("date", task.dueDate)
                .append("checked", task.checked)
                .append("attachment", task.attachment)
                .append("project", task.project.id)
                .append("section", task.section != null ? task.section.id : null)
                .append("labels", labelsList);
    }
}
