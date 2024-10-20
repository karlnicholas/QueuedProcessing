package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SyncProcessor {

    private final AsyncService asyncService;
    private volatile boolean running = true;

    @Autowired
    public SyncProcessor(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    public void processItems() {
        try {
            while (running) {
                // Take the first item (blocks until at least one is available)
                ProcessingRequest initialRequest = asyncService.getRequestQueue().take();

                // Collect any additional available items from the queue
                List<ProcessingRequest> requests = new ArrayList<>();
                requests.add(initialRequest);
                asyncService.getRequestQueue().drainTo(requests); // Drains all available items into the list

                // Process all collected items
                for (ProcessingRequest request : requests) {
                    String result = processItem(request.getData());
                    request.getFuture().complete(result); // Notify the async method that processing is complete
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    private String processItem(String data) {
        // Simulate processing
        return "Processed: " + data;
    }

    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown() {
        running = false; // Set running to false to stop the loop
    }
}
