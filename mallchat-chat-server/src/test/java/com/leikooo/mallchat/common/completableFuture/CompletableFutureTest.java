package com.leikooo.mallchat.common.completableFuture;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
public class CompletableFutureTest {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 3";
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        allFutures.thenRun(() -> {
            // All futures completed
            String result1 = future1.join();
            String result2 = future2.join();
            String result3 = future3.join();
            System.out.println(result1 + ", " + result2 + ", " + result3);
        });
    }

    @Test
    public void test1() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // Causes an ArithmeticException
            int result = 10 / 0;
            return result;
        });

        future.exceptionally(ex -> {
            // Default value to return if there's an exception
            System.out.println("Exception occurred: " + ex.getMessage());
            return 0;
        }).thenAccept(result -> {
            System.out.println("Result: " + result);
        });
    }

    @Test
    public void test2() {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread(supplyAsync1): " + Thread.currentThread().getName());
            return "hello";
        });

        CompletableFuture<Integer> transformedFuture = future.thenApplyAsync(s -> {
            System.out.println("Thread(thenApplyAsync2): " + Thread.currentThread().getName());
            return s.length();
        });

        transformedFuture.thenAcceptAsync(length -> {
            System.out.println("Thread(thenAccept3): " + Thread.currentThread().getName());
            System.out.println("Length of Hello: " + length);
        });
    }

    @Test
    public void test3() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future2 = future1.thenComposeAsync(s -> CompletableFuture.supplyAsync(() -> s + " World"));

        future2.thenAccept(System.out::println);
    }

    @Test
    public void test4() throws InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("http://localhost:8080/users");
            return "future1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("http://localhost:8080/products");
            return "future2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("http://localhost:8080/orders");
            return "future3";
        });

        // allOf 没有执行顺序
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future1, future2, future3);
        voidCompletableFuture.thenAccept(v -> {
            String f1 = future1.join();
            String f2 = future2.join();
            String f3 = future3.join();
            System.out.println("f1 = " + f1 + "f2 = " + f2 + " f3 = " + f3);
        });

        Thread.sleep(10000);
    }
}
