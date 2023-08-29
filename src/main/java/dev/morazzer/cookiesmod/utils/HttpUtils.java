package dev.morazzer.cookiesmod.utils;

import net.minecraft.util.Identifier;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final Identifier LOG_HTTP_REQUESTS = DevUtils.createIdentifier("log_http_requests");

    public static byte[] getResponseBody(URI uri) {
        AtomicReference<byte[]> response = new AtomicReference<>(new byte[0]);
        getResponse(uri, httpResponse -> {
            if (httpResponse == null) return;
            try {
                response.set(httpResponse.getEntity().getContent().readAllBytes());
            } catch (IOException e) {
                ExceptionHandler.handleException(e);
            }
        });
        return response.get();
    }

    public static void getResponse(URI uri, Consumer<CloseableHttpResponse> consumer) {
        try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
            CloseableHttpResponse execute = closeableHttpClient.execute(new HttpGet(uri));
            if (DevUtils.isEnabled(LOG_HTTP_REQUESTS)) {
                byte[] bytes = execute.getEntity().getContent().readAllBytes();
                execute.getEntity().getContent().reset();
                int size = bytes.length;

                logger.info("HTTP GET to {} with response length {} and status {}", uri.toString(), size, execute.getStatusLine().getStatusCode());
            }

            consumer.accept(execute);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

}
