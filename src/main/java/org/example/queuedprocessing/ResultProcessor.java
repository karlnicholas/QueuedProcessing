package org.example.queuedprocessing;

import java.util.concurrent.CompletableFuture;

public class ResultProcessor {

    public void processResult(CompletableFuture<String> future) {
        future.thenApply(this::transformResult)
                .thenAccept(this::handleTransformedResult)
                .exceptionally(ex -> {
                    handleException(ex);
                    return null;
                });
    }

    // A method to transform the result of the CompletableFuture
    private String transformResult(String result) {
        return "Transformed: " + result;
    }

    // A method to handle the transformed result
    private void handleTransformedResult(String transformedResult) {
        // Simulate handling of the transformed result (e.g., logging or further processing)
        System.out.println("Handled transformed result: " + transformedResult);
    }

    // A method to handle exceptions during the future processing
    private void handleException(Throwable ex) {
        // Simulate error handling (e.g., logging the error)
        System.err.println("Error processing result: " + ex.getMessage());
    }
}
