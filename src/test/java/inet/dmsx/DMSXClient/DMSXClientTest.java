package inet.dmsx.DMSXClient;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DMSXClientTest {

    private final DMSXClient client = new DMSXClient("http://localhost:8080");
    private final ParamsStruct params = new ParamsStruct("tmp", "2023_08", "test.txt");
    private final Path testPath = Paths.get("src/test/resources/test.txt");

    @Test
    void uploadFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.uploadFile(params, testPath);

        assertEquals(200, response.statusCode());
    }

    @Test
    void getFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.getFile(params);

        try (var inputStream = response.body()) {
            var s = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("It works!", s);
        }

        assertEquals(200, response.statusCode());
    }

    @Test
    void pingServer() throws URISyntaxException, IOException, InterruptedException {
        var response = client.pingServer();

        assertEquals(200, response.statusCode());
    }
}