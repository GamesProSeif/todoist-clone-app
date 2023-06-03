package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Section;
import com.todoist.todoist.models.Task;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
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
        List<ObjectId> tagsId = doc.getList("tags", ObjectId.class);

        ArrayList<ObjectId> tags = new ArrayList<>(tagsId);

        return new Task(
                doc.getObjectId("_id"),
                doc.getString("title"),
                doc.getString("description"),
                doc.getDate("date") != null
                    ? LocalDate.ofInstant(doc.getDate("date").toInstant(), ZoneId.systemDefault())
                    : null,
                doc.getBoolean("checked"),
                project,
                section,
                tags
        );
    }

    @Override
    protected Document modelToDocument(Task task) {
        return new Document()
                .append("_id", task.id)
                .append("title", task.title)
                .append("description", task.description)
                .append("date", task.dueDate)
                .append("checked", task.checked)
                .append("project", task.project.id)
                .append("section", task.section != null ? task.section.id : null)
                .append("tags", task.tags);
    }
}
