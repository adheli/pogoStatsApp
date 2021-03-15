package ie.ait.tavares.pogo.external.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpConnectionHelper {

    private final String url;

    public HttpConnectionHelper(String url) {
        this.url = url;
    }

    public String processHttpCall() throws IOException {
        return processHttpCall(createConnection());
    }

    public String processHttpCall(HttpURLConnection connection) throws IOException {
        if (connection != null) {
            try {
                log.info("Request sent to URL: {}", url);

                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                log.info("Response headers: {}", connection.getHeaderFields());
                log.info("Response code: {}", connection.getResponseCode());

                if (connection.getResponseCode() > 299) {
                    try (Reader streamReader = new InputStreamReader(connection.getErrorStream())) {
                        throw new InvalidObjectException(getResponse(streamReader));
                    }
                } else {
                    try (Reader streamReader = new InputStreamReader(connection.getInputStream())) {
                        return getResponse(streamReader);
                    }
                }
            } finally {
                connection.disconnect();
            }
        }
        return null;
    }

    private String getResponse(Reader streamReader) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(streamReader)) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            log.info("Response body: {}", content.substring(0, Math.min(100, content.length())));
        } catch (IOException io) {
            log.error("Nothing to read from stream");
        }

        return content.toString();
    }

    public HttpURLConnection createConnection() {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            log.error("Cannot create connection to url {}, error: {}", url, e.getMessage());
            return null;
        }
    }
}
