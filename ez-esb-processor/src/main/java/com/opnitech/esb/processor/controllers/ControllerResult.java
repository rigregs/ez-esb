package com.opnitech.esb.processor.controllers;

import java.io.Serializable;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ControllerResult implements Serializable {

    private static final long serialVersionUID = -5973387062590462027L;

    private ControllerResultEnum controllerResultEnum;
    private String description;

    public ControllerResult() {

        // Default constructor
    }

    public ControllerResult(ControllerResultEnum controllerResultEnum, String description) {

        this.setControllerResultEnum(controllerResultEnum);
        this.setDescription(description);
    }

    public ControllerResultEnum getControllerResultEnum() {

        return this.controllerResultEnum;
    }

    public void setControllerResultEnum(ControllerResultEnum controllerResultEnum) {

        this.controllerResultEnum = controllerResultEnum;
    }

    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}
