package org.example.queuedprocessing;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class ProcessingRequest {
    private final String key;
    private final String data;
    private final CompletableFuture<String> future;

    public ProcessingRequest(String key, String data) {
        this.key = key;
        this.data = data;
        this.future = new CompletableFuture<>();
    }

}
