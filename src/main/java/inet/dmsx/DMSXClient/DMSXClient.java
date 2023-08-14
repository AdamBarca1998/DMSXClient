package inet.dmsx.DMSXClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class DMSXClient {

    private final String uri;
    private final HttpClient client = HttpClient.newBuilder().build();

    public DMSXClient(String uri) {
        this.uri = uri;
    }

    public HttpResponse<String> uploadFile(ParamsStruct params, Path path) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(getUriWithParams(params))
                .POST(HttpRequest.BodyPublishers.ofFile(path))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<InputStream> getFile(ParamsStruct params) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(getUriWithParams(params))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }

    public HttpResponse<String> deleteFile(ParamsStruct params) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(getUriWithParams(params))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> infoFile(ParamsStruct params) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI(getUriWithParams(params) + "/info"))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> pingServer() throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI(uri + "/ping"))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> checksumFile(ParamsStruct params) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI(getUriWithParams(params) + "/checksum"))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI getUriWithParams(ParamsStruct params) throws URISyntaxException {
        return new URI(uri + "/" + params.storageId() + "/" + params.directory() + "/" + params.fileName());
    }
}
