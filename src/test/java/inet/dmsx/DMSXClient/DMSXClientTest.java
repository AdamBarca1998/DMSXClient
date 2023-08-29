package inet.dmsx.DMSXClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DMSXClientTest {

    private final DMSXClient client = new DMSXClient("http://localhost:8080");
    private final ParamsStruct params = new ParamsStruct("tmp", "2023_08", "test.txt");
    private final Path testPath = Paths.get("src/test/resources/test.txt");

    @BeforeEach
    void beforeEach() throws URISyntaxException, IOException, InterruptedException {
        uploadFile();
    }

    @AfterEach
    void afterEach() throws URISyntaxException, IOException, InterruptedException {
        deleteFile();
    }

    @Test
    void uploadFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.uploadFile(params, testPath);

        assertEquals(200, response.statusCode());
    }

    @Test
    void getExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.getFile(params);

        try (var inputStream = response.body()) {
            var s = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("It works!", s);
        }

        assertEquals(200, response.statusCode());
    }

    @Test
    void getNotExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.getFile(new ParamsStruct(params.storageId(), params.directory(), UUID.randomUUID().toString()));

        assertEquals(500, response.statusCode());
    }

    @Test
    void deleteFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.deleteFile(params);

        assertEquals(200, response.statusCode());
    }

    @Test
    void infoExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.infoFile(params);

        assertEquals(200, response.statusCode());
        assertEquals("9", response.body());
    }

    @Test
    void infoNotExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.infoFile(new ParamsStruct(params.storageId(), params.directory(), UUID.randomUUID().toString()));

        assertEquals(500, response.statusCode());
    }

    @Test
    void pingServer() throws URISyntaxException, IOException, InterruptedException {
        var response = client.pingServer();

        assertEquals(200, response.statusCode());
    }

    @Test
    void checksumExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.checksumFile(params);

        assertEquals(200, response.statusCode());
        assertEquals("661d154abfc42a49970f3d53b758fd50", response.body());
    }

    @Test
    void checksumNotExistFile() throws URISyntaxException, IOException, InterruptedException {
        var response = client.checksumFile(new ParamsStruct(params.storageId(), params.directory(), UUID.randomUUID().toString()));

        assertEquals(500, response.statusCode());
    }


    @Test
    void pauseServerAndResumeServer() throws URISyntaxException, IOException, InterruptedException {
        // test in run state
        infoExistFile();

        // pause
        var pauseResponse = client.pauseServer();
        assertEquals(200, pauseResponse.statusCode());

        // test in pause state
        var infoResponse = client.infoFile(params);
        assertEquals(405, infoResponse.statusCode());

        // resume
        var resumeResponse = client.resumeServer();
        assertEquals(200, resumeResponse.statusCode());

        // test in run state
        infoExistFile();
    }

    @Test
    void heathServer() throws URISyntaxException, IOException, InterruptedException {
        var response = client.heathServer();

        assertEquals("RunState", response.state());
    }
}