package org.example.queuedprocessing;

import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Getter
public class AsyncService {

    private final BlockingQueue<ProcessingRequest> requestQueue = new LinkedBlockingQueue<>();

    @Async
    public CompletableFuture<String> submitItem(String key, String data) {
        ProcessingRequest request = new ProcessingRequest(key, data);
        requestQueue.offer(request);
        return request.getFuture();
    }
}
