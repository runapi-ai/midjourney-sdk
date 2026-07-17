package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.midjourney.types.CompletedTextToImageResponse;
import ai.runapi.midjourney.types.TextToImageParams;
import ai.runapi.midjourney.types.TextToImageResponse;

/** Text To Image operations. */
public final class TextToImageResource extends MidjourneyResource {
  /** API endpoint path for text to image operations. */
  public static final String ENDPOINT = "/api/v1/midjourney/text_to_image";

  /** Creates a resource bound to the supplied transport and client options. */
  public TextToImageResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a text to image task. */
  public TaskCreateResponse create(TextToImageParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a text to image task with per-request options. */
  public TaskCreateResponse create(TextToImageParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a text to image task by ID. */
  public TextToImageResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a text to image task by ID with per-request options. */
  public TextToImageResponse get(String id, RequestOptions options) {
    return getTask(id, options, TextToImageResponse.class);
  }

  /** Creates a text to image task and polls until it completes. */
  public CompletedTextToImageResponse run(TextToImageParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a text to image task with per-request options and polls until it completes. */
  public CompletedTextToImageResponse run(TextToImageParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, TextToImageResponse.class, CompletedTextToImageResponse.class);
  }
}
