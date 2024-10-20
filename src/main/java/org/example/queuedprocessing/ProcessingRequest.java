package org.example.queuedprocessing;

import java.util.concurrent.CompletableFuture;

public class ProcessingRequest {
    private final String key;
    private final String data;
    private final CompletableFuture<String> future;

    public ProcessingRequest(String key, String data) {
        this.key = key;
        this.data = data;
        this.future = new CompletableFuture<>();
    }

    // Constructor to reuse existing ProcessingRequest
    public ProcessingRequest(String key, String data, CompletableFuture<String> future) {
        this.key = key;
        this.data = data;
        this.future = future;
    }

    public String getKey() {
        return key;
    }

    public String getData() {
        return data;
    }

    public CompletableFuture<String> getFuture() {
        return future;
    }
}
