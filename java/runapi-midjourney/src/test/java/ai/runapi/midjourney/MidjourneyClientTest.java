package ai.runapi.midjourney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.midjourney.types.CompletedTextToImageResponse;
import ai.runapi.midjourney.types.TextToImageResponse;
import ai.runapi.midjourney.types.CompletedEditImageResponse;
import ai.runapi.midjourney.types.CompletedExtendVideoResponse;
import ai.runapi.midjourney.types.CompletedImageToVideoResponse;
import ai.runapi.midjourney.types.CompletedTextToImageResponse;
import ai.runapi.midjourney.types.EditImageModel;
import ai.runapi.midjourney.types.EditImageParams;
import ai.runapi.midjourney.types.EditImageResponse;
import ai.runapi.midjourney.types.ExtendVideoParams;
import ai.runapi.midjourney.types.GetSeedParams;
import ai.runapi.midjourney.types.GetSeedResponse;
import ai.runapi.midjourney.types.ImageToPromptParams;
import ai.runapi.midjourney.types.ImageToPromptResponse;
import ai.runapi.midjourney.types.ImageToVideoModel;
import ai.runapi.midjourney.types.ImageToVideoParams;
import ai.runapi.midjourney.types.ImageToVideoResponse;
import ai.runapi.midjourney.types.ShortenPromptParams;
import ai.runapi.midjourney.types.ShortenPromptResponse;
import ai.runapi.midjourney.types.TextToImageModel;
import ai.runapi.midjourney.types.TextToImageParams;
import ai.runapi.midjourney.types.TextToImageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class MidjourneyClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").build();

    assertNotNull(client.textToImage());
    assertNotNull(client.extendVideo());
    assertNotNull(client.shortenPrompt());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new TextToImageModel("midjourney-v8.1"));

    assertEquals("\"midjourney-v8.1\"", json);
    assertEquals(new TextToImageModel("midjourney-v8.1"), Json.mapper().readValue(json, TextToImageModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

    client.textToImage().create(
        TextToImageParams.builder()
            .model(TextToImageModel.MIDJOURNEY_V8_1)
            .prompt("A small red cube on a plain white table, studio product photo")
            .enablePromptTranslation(true)
            .includeSplitImages(true)
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/midjourney/text_to_image", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
    assertEquals(true, body.get("enable_prompt_translation").asBoolean());
    assertEquals(true, body.get("include_split_images").asBoolean());
  }

  @Test
  void extendVideoUsesOnlyPublicContinuationFields() throws Exception {
    CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_extend\",\"status\":\"processing\"}");
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(createTransport).build();

    client.extendVideo().create(
        ExtendVideoParams.builder()
            .sourceTaskId("task_source")
            .prompt("Continue the camera orbit")
            .callbackUrl("https://example.test/callback")
            .build());

    assertEquals("POST", createTransport.request.getMethod().name());
    assertEquals("/api/v1/midjourney/extend_video", createTransport.request.getPath());
    JsonNode body = bodyJson(createTransport.request);
    assertEquals("task_source", body.get("source_task_id").asText());
    assertEquals("Continue the camera orbit", body.get("prompt").asText());
    assertEquals("https://example.test/callback", body.get("callback_url").asText());
    assertEquals(false, body.has("model"));
    assertEquals(false, body.has("video_id"));
    assertEquals(false, body.has("video_index"));

    CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_extend\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/extended\"}]}");
    MidjourneyClient getClient = MidjourneyClient.builder().apiKey("sk-test").transport(getTransport).build();
    assertNotNull(getClient.extendVideo().get("task_extend"));
    assertEquals("/api/v1/midjourney/extend_video/task_extend", getTransport.request.getPath());

    SequenceTransport runTransport = new SequenceTransport(
        "{\"id\":\"task_extend\",\"status\":\"processing\"}",
        "{\"id\":\"task_extend\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/extended\"}]}");
    MidjourneyClient runClient = MidjourneyClient.builder().apiKey("sk-test").transport(runTransport).build();
    CompletedExtendVideoResponse response = runClient.extendVideo().run(
        ExtendVideoParams.builder().sourceTaskId("task_source").build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
    assertNotNull(response.getVideos());
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

    TextToImageResponse response = client.textToImage().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/midjourney/text_to_image/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedTextToImageResponse response = client.textToImage().run(
        TextToImageParams.builder()
            .model(TextToImageModel.MIDJOURNEY_V8_1)
            .prompt("A small red cube on a plain white table, studio product photo")
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.textToImage().run(
                TextToImageParams.builder()
                    .model(TextToImageModel.MIDJOURNEY_V8_1)
                    .prompt("A small red cube on a plain white table, studio product photo")
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversEditimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_edit_image\",\"status\":\"processing\"}");
      MidjourneyClient createClient = MidjourneyClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.editImage().create(
              EditImageParams.builder()
                  .model(EditImageModel.MIDJOURNEY_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .includeSplitImages(true)
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_image_options\",\"status\":\"processing\"}");
      MidjourneyClient createWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.editImage().create(
              EditImageParams.builder()
                  .model(EditImageModel.MIDJOURNEY_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_edit_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getClient = MidjourneyClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.editImage().get("task_edit_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.editImage().get("task_edit_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_edit_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runClient = MidjourneyClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedEditImageResponse runResponse = runClient.editImage().run(
              EditImageParams.builder()
                  .model(EditImageModel.MIDJOURNEY_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_edit_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.editImage().run(
              EditImageParams.builder()
                  .model(EditImageModel.MIDJOURNEY_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversGetseedResourceMethods() {
      CapturingTransport transport = new CapturingTransport("{\"id\":\"sync_get_seed\",\"seed\":1}");
      MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

      GetSeedResponse response = client.getSeed().run(
              GetSeedParams.builder()
                  .imageId("sample")
                  .build()
      );
      assertNotNull(response);

      CapturingTransport transportWithOptions = new CapturingTransport("{\"id\":\"sync_get_seed_options\",\"seed\":1}");
      MidjourneyClient clientWithOptions = MidjourneyClient.builder().apiKey("sk-test").transport(transportWithOptions).build();
      assertNotNull(clientWithOptions.getSeed().run(
              GetSeedParams.builder()
                  .imageId("sample")
                  .build(),
          RequestOptions.none()));
    }

    @Test
    void coversImagetopromptResourceMethods() {
      CapturingTransport transport = new CapturingTransport("{\"id\":\"sync_image_to_prompt\",\"prompts\":[\"sample\"]}");
      MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

      ImageToPromptResponse response = client.imageToPrompt().run(
              ImageToPromptParams.builder()
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build()
      );
      assertNotNull(response);

      CapturingTransport transportWithOptions = new CapturingTransport("{\"id\":\"sync_image_to_prompt_options\",\"prompts\":[\"sample\"]}");
      MidjourneyClient clientWithOptions = MidjourneyClient.builder().apiKey("sk-test").transport(transportWithOptions).build();
      assertNotNull(clientWithOptions.imageToPrompt().run(
              ImageToPromptParams.builder()
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));
    }

    @Test
    void shortensPromptSynchronously() throws Exception {
      CapturingTransport transport = new CapturingTransport("{\"prompts\":[\"Concise mountain landscape\"]}");
      MidjourneyClient client = MidjourneyClient.builder().apiKey("sk-test").transport(transport).build();

      ShortenPromptResponse response = client.shortenPrompt().run(
          ShortenPromptParams.builder()
              .prompt("A detailed cinematic mountain landscape")
              .build());

      assertEquals("POST", transport.request.getMethod().name());
      assertEquals("/api/v1/midjourney/shorten_prompt", transport.request.getPath());
      assertEquals("A detailed cinematic mountain landscape", bodyJson(transport.request).get("prompt").asText());
      assertEquals("Concise mountain landscape", response.getPrompts().get(0));
    }

    @Test
    void coversImagetovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"processing\"}");
      MidjourneyClient createClient = MidjourneyClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.MIDJOURNEY_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .enableLoop(true)
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"processing\"}");
      MidjourneyClient createWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.MIDJOURNEY_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getClient = MidjourneyClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.imageToVideo().get("task_image_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.imageToVideo().get("task_image_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runClient = MidjourneyClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedImageToVideoResponse runResponse = runClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.MIDJOURNEY_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.MIDJOURNEY_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttoimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"processing\"}");
      MidjourneyClient createClient = MidjourneyClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.MIDJOURNEY_V8_1)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"processing\"}");
      MidjourneyClient createWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.MIDJOURNEY_V8_1)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getClient = MidjourneyClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToImage().get("task_text_to_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient getWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToImage().get("task_text_to_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runClient = MidjourneyClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToImageResponse runResponse = runClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.MIDJOURNEY_V8_1)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      MidjourneyClient runWithOptionsClient = MidjourneyClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.MIDJOURNEY_V8_1)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
