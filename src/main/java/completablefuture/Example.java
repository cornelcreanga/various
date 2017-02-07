package completablefuture;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Example {

    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(10);

        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> {
                    try (InputStream is = new URL("http://www.nurkiewicz.com").openStream()) {
                        System.out.println("Downloading..");
                        return IOUtils.toString(is, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, pool);


        CompletableFuture<Integer> intFuture = future.thenApply(s -> s.length());

        System.out.println(intFuture.get());
    }

}
