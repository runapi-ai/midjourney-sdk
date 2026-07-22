package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.midjourney.types.CompletedExtendVideoResponse;
import ai.runapi.midjourney.types.ExtendVideoParams;
import ai.runapi.midjourney.types.ExtendVideoResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/** First-video extension operations. */
public final class ExtendVideoResource extends MidjourneyResource {
  /** API endpoint path for first-video extension operations. */
  public static final String ENDPOINT = "/api/v1/midjourney/extend_video";
  private static final String MODEL = "midjourney-image-to-video";

  /** Creates a resource bound to the supplied transport and client options. */
  public ExtendVideoResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a first-video extension task. */
  public TaskCreateResponse create(ExtendVideoParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a first-video extension task with per-request options. */
  public TaskCreateResponse create(ExtendVideoParams params, RequestOptions options) {
    Map<String, Object> body = params.toMap();
    return createTask(params.action(), withModel(body), body, options);
  }

  /** Retrieves a first-video extension task by ID. */
  public ExtendVideoResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a first-video extension task by ID with per-request options. */
  public ExtendVideoResponse get(String id, RequestOptions options) {
    return getTask(id, options, ExtendVideoResponse.class);
  }

  /** Creates a first-video extension task and polls until it completes. */
  public CompletedExtendVideoResponse run(ExtendVideoParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a first-video extension task with per-request options and polls until it completes. */
  public CompletedExtendVideoResponse run(ExtendVideoParams params, RequestOptions options) {
    Map<String, Object> body = params.toMap();
    return runTask(
        params.action(),
        withModel(body),
        body,
        options,
        ExtendVideoResponse.class,
        CompletedExtendVideoResponse.class);
  }

  private static Map<String, Object> withModel(Map<String, Object> body) {
    Map<String, Object> validationBody = new LinkedHashMap<String, Object>(body);
    validationBody.put("model", MODEL);
    return validationBody;
  }
}
