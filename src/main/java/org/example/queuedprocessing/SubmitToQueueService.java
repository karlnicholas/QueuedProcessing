package org.example.queuedprocessing;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SubmitToQueueService {

    private final BlockingQueue<ProcessingRequest> requestQueue = new LinkedBlockingQueue<>();

    public CompletableFuture<String> submitItem(ProcessingRequest request) {
        requestQueue.offer(request);
        return request.getFuture();
    }

    public BlockingQueue<ProcessingRequest> getRequestQueue() {
        return requestQueue;
    }
}
