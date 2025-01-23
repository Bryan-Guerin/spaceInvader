module com.bryan.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires org.apache.logging.log4j;
    requires static lombok;
    requires com.fasterxml.jackson.databind;

    opens com.bryan.spaceinvader to javafx.fxml;
    exports com.bryan.spaceinvader;
    exports com.bryan.spaceinvader.controller;
    opens com.bryan.spaceinvader.controller to javafx.fxml;
    exports com.bryan.spaceinvader.model.ressource.manager;
    opens com.bryan.spaceinvader.model.ressource.manager to javafx.fxml;

    exports com.bryan.spaceinvader.model;
    opens com.bryan.spaceinvader.model to com.fasterxml.jackson.databind;
}