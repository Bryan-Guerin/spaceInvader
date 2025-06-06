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
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires annotations;

    exports com.bryan.spaceinvader;
    exports com.bryan.spaceinvader.controller;
    exports com.bryan.spaceinvader.model;
    exports com.bryan.spaceinvader.model.ressource.manager;

    opens com.bryan.spaceinvader to javafx.fxml;
    opens com.bryan.spaceinvader.controller to javafx.fxml;
    opens com.bryan.spaceinvader.model to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.bryan.spaceinvader.model.ressource.manager to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.bryan.spaceinvader.model.game;
    opens com.bryan.spaceinvader.model.game to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.bryan.spaceinvader.model.shop;
    opens com.bryan.spaceinvader.model.shop to com.fasterxml.jackson.databind, javafx.fxml;
    opens com.bryan.spaceinvader.model.player to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.bryan.spaceinvader.model.player;
    exports com.bryan.spaceinvader.model.invader;
    exports com.bryan.spaceinvader.model.invader.state;
    opens com.bryan.spaceinvader.model.invader to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.bryan.spaceinvader.model.game.level.generator;
    opens com.bryan.spaceinvader.model.game.level.generator to com.fasterxml.jackson.databind, javafx.fxml;
}