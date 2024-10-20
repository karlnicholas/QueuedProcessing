package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class QueueProcessorService {

    private final SubmitToQueueService submitToQueueService;
    private volatile boolean running = true;
    private static final int BATCH_SIZE = 4; // Number of messages to process at once

    @Autowired
    public QueueProcessorService(SubmitToQueueService submitToQueueService) {
        this.submitToQueueService = submitToQueueService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        new Thread(this::processItems).start();
    }

    public void processItems() {
        try {
            while (running) {
                List<ProcessingRequest> requests = new ArrayList<>();
                // Use poll with a timeout to ensure that we don't block indefinitely
                ProcessingRequest initialRequest = submitToQueueService.getRequestQueue().poll(5, TimeUnit.SECONDS);

                if (initialRequest != null) {
                    requests.add(initialRequest);
                    submitToQueueService.getRequestQueue().drainTo(requests, BATCH_SIZE - 1);
                }

                // If we have any requests, process them
                if (!requests.isEmpty()) {
                    processItem(requests);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    String processItem(List<ProcessingRequest> requests) {
        // Simulate processing all requests
        for (ProcessingRequest request : requests) {
            String result = "Processed: " + request.getData();
            request.getFuture().complete(result); // Notify the async method that processing is complete
        }
        return "Batch of " + requests.size() + " requests processed";
    }

    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown() {
        running = false; // Set running to false to stop the loop
    }
}
