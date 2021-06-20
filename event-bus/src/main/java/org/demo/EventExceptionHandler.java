package org.demo;

public interface EventExceptionHandler {

    void handle (Throwable cause, EventContext context);
}
