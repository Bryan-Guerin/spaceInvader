package com.bryan.spaceinvader.model.ressource.manager;

public enum ResourceType {
    FXML("fxml/"),
    IMAGE("assets/image/"),
    AUDIO("assets/audio/");

    private final String pathPrefix;

    ResourceType(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }
}
