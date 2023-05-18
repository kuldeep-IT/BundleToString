package com.dada.getpost2;

public class OuterObject {
    private InnerObject innerObject;
    String eventName;

    public OuterObject() {
    }

    public OuterObject(String eventName, InnerObject innerObject) {
        this.innerObject = innerObject;
        this.eventName = eventName;
    }

    // Getter and setter methods

    public InnerObject getInnerObject() {
        return innerObject;
    }

    public void setInnerObject(InnerObject innerObject) {
        this.innerObject = innerObject;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}