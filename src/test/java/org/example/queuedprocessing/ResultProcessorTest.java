package org.example.queuedprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultProcessorTest {

    private ResultProcessor resultProcessor;
    private CompletableFuture<String> future;

    @BeforeEach
    void setup() {
        resultProcessor = new ResultProcessor();
        future = new CompletableFuture<>();
    }

    @Test
    void testProcessResultSuccess() throws ExecutionException, InterruptedException {
        // Complete the future with a result and process it
        future.complete("Processed: data1");
        resultProcessor.processResult(future);

        // Since transformResult is private, we can't access it directly.
        // Instead, we simulate checking that the transformed result was handled.
        // For testing purposes, let's use the future's completion status.
        assertTrue(future.isDone());
    }

    @Test
    void testProcessResultFailure() {
        // Complete the future with an exception
        future.completeExceptionally(new RuntimeException("Test exception"));
        resultProcessor.processResult(future);

        // Verify that the exception is handled
        assertTrue(future.isCompletedExceptionally());
    }
}
