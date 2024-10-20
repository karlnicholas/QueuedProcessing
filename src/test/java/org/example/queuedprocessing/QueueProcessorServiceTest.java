package org.example.queuedprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueueProcessorServiceTest {

    private SubmitToQueueService submitToQueueService;
    private QueueProcessorService queueProcessorService;

    private LinkedBlockingQueue<ProcessingRequest> requestQueue;

    @BeforeEach
    void setup() {
        submitToQueueService = new SubmitToQueueService();
        queueProcessorService = new QueueProcessorService(submitToQueueService);
        requestQueue = (LinkedBlockingQueue<ProcessingRequest>) submitToQueueService.getRequestQueue();
    }

    @Test
    void testProcessItemsInBatches() throws InterruptedException, ExecutionException {
        // Create 4 ProcessingRequests and add them to the queue
        ProcessingRequest request1 = new ProcessingRequest("key1", "data1");
        ProcessingRequest request2 = new ProcessingRequest("key2", "data2");
        ProcessingRequest request3 = new ProcessingRequest("key3", "data3");
        ProcessingRequest request4 = new ProcessingRequest("key4", "data4");

        requestQueue.offer(request1);
        requestQueue.offer(request2);
        requestQueue.offer(request3);
        requestQueue.offer(request4);

        // Start the processing in a separate thread
        new Thread(queueProcessorService::processItems).start();

        // Give it a shorter time to process
        Thread.sleep(50);

        // Verify that all futures are completed with the expected results
        assertEquals("Processed: data1", request1.getFuture().get());
        assertEquals("Processed: data2", request2.getFuture().get());
        assertEquals("Processed: data3", request3.getFuture().get());
        assertEquals("Processed: data4", request4.getFuture().get());
    }

    @Test
    void testProcessItemsHandlesLessThanBatchSize() throws InterruptedException, ExecutionException {
        // Create 2 ProcessingRequests and add them to the queue
        ProcessingRequest request1 = new ProcessingRequest("key1", "data1");
        ProcessingRequest request2 = new ProcessingRequest("key2", "data2");

        requestQueue.offer(request1);
        requestQueue.offer(request2);

        // Start the processing in a separate thread
        new Thread(queueProcessorService::processItems).start();

        // Give it some time to process
        Thread.sleep(100);

        // Verify that the futures are completed even with fewer than 4 messages
        assertEquals("Processed: data1", request1.getFuture().get());
        assertEquals("Processed: data2", request2.getFuture().get());
    }

    @Test
    void testProcessItemsInterrupted() throws InterruptedException {
        ProcessingRequest request = new ProcessingRequest("key", "data");
        requestQueue.offer(request);

        Thread processingThread = new Thread(queueProcessorService::processItems);
        processingThread.start();
        processingThread.interrupt();

        // Allow some time for the thread to process the interruption
        Thread.sleep(100);

        assertEquals(false, processingThread.isAlive());
    }

    @Test
    void testProcessItem() throws InterruptedException, ExecutionException {
        ProcessingRequest request1 = new ProcessingRequest("key1", "data1");
        ProcessingRequest request2 = new ProcessingRequest("key2", "data2");

        List<ProcessingRequest> requests = List.of(request1, request2);
        String result = queueProcessorService.processItem(requests);

        assertEquals("Batch of 2 requests processed", result);
        assertEquals("Processed: data1", request1.getFuture().get());
        assertEquals("Processed: data2", request2.getFuture().get());
    }

    @Test
    void testOnApplicationShutdown() {
        queueProcessorService.onApplicationShutdown();
        // This test can be expanded by verifying that the processItems loop ends gracefully.
    }
}
