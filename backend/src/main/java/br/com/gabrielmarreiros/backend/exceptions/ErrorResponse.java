package br.com.gabrielmarreiros.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private String title;
    private String description;
    private int status;
    private String instance;

    public ErrorResponse() {}

    public ErrorResponse(String title, String description, int status, String instance) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.instance = instance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
