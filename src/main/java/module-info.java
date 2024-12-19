module com.bryan.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.logging.log4j;
    requires static lombok;

    opens com.bryan.spaceinvader to javafx.fxml;
    exports com.bryan.spaceinvader;
    exports com.bryan.spaceinvader.controller;
    opens com.bryan.spaceinvader.controller to javafx.fxml;
    exports com.bryan.spaceinvader.model.ressource.manager;
    opens com.bryan.spaceinvader.model.ressource.manager to javafx.fxml;
}