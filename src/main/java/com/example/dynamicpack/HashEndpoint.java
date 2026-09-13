package com.example.dynamicpack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

public final class HashEndpoint {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_1_1)
            .executor(Executors.newCachedThreadPool(runnable -> {
                Thread thread = new Thread(runnable, "dynamic-pack-http");
                thread.setDaemon(true);
                return thread;
            }))
            .build();

    private HashEndpoint() {
    }

    public static CompletableFuture<String> fetchHash() {
        System.out.println("[Dynamic Pack Mod] Fetching SHA-1 from " + DynamicPackConfig.hashEndpoint());
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(DynamicPackConfig.hashEndpoint()))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
        } catch (IllegalArgumentException exception) {
            return CompletableFuture.failedFuture(new IOExceptionLikeException("Invalid hash endpoint", exception));
        }

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .orTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .thenCompose(response -> {
                    if (response.statusCode() < 200 || response.statusCode() >= 300) {
                        return CompletableFuture.failedFuture(new IOExceptionLikeException(
                                "Hash endpoint returned HTTP " + response.statusCode(), null));
                    }
                    String hash = response.body().trim();
                    if (!hash.matches("(?i)[0-9a-f]{40}")) {
                        return CompletableFuture.failedFuture(new IOExceptionLikeException(
                                "Hash endpoint did not return a 40-character SHA-1", null));
                    }
                    return CompletableFuture.completedFuture(hash.toLowerCase());
                });
    }

    static final class IOExceptionLikeException extends RuntimeException {
        IOExceptionLikeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
