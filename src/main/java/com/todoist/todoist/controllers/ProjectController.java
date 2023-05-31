package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;
import org.bson.types.ObjectId;

public class ProjectController extends BaseController<Project> {
    public ProjectController(MongoDatabase db) {
        super(db, "projects");
    }

    @Override
    protected Project documentToModel(Document doc) {
        return new Project(
                doc.getObjectId("_id"),
                doc.getString("title")
        );
    }

    @Override
    protected Document modelToDocument(Project project) {
        return new Document()
                .append("_id", project.id)
                .append("title", project.title);
    }
}
