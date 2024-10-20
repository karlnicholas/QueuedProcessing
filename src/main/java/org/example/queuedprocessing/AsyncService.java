package org.example.queuedprocessing;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class AsyncService {

    private final BlockingQueue<ProcessingRequest> requestQueue = new LinkedBlockingQueue<>();

    @Async
    public CompletableFuture<String> submitItem(ProcessingRequest request) {
        requestQueue.offer(request);
        return request.getFuture();
    }

    public BlockingQueue<ProcessingRequest> getRequestQueue() {
        return requestQueue;
    }
}
