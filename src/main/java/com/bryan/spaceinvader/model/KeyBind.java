package com.bryan.spaceinvader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import javafx.scene.input.KeyCode;

public class KeyBind {

    @JsonProperty(value = "key", required = true)
    private String key;
    @JsonIgnore
    private transient KeyCode keyCode;

    public KeyBind() {
    }

    public KeyBind(KeyCode keyCode) {
        this.keyCode = keyCode;
        this.key = keyCode.toString();
    }

    public KeyBind setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
        this.key = keyCode.toString();
        return this;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    @JsonSetter("key")
    public void setKey(String key) {
        this.key = key;
        this.keyCode = KeyCode.valueOf(key);
    }
}
