module com.todoist.todoist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.dotenv;

    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    opens com.todoist.todoist to javafx.fxml;
    exports com.todoist.todoist;
}