package org.example.queuedprocessing;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class SubmitToQueueServiceTest {

    private final SubmitToQueueService submitToQueueService = new SubmitToQueueService();

    @Test
    void testSubmitItem() {
        ProcessingRequest request = new ProcessingRequest("key1", "data1");
        CompletableFuture<String> future = submitToQueueService.submitItem(request);

        assertNotNull(future);
        assertFalse(future.isDone());
        assertEquals(request, submitToQueueService.getRequestQueue().poll());
    }

    @Test
    void testGetRequestQueue() {
        BlockingQueue<ProcessingRequest> queue = submitToQueueService.getRequestQueue();
        assertTrue(queue instanceof LinkedBlockingQueue);
    }
}
