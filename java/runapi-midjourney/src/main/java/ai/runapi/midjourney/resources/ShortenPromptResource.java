package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.Task;
import ai.runapi.midjourney.types.ShortenPromptParams;
import ai.runapi.midjourney.types.ShortenPromptResponse;

/** Prompt shortening operations. */
public final class ShortenPromptResource extends MidjourneyResource {
  /** API endpoint path for prompt shortening operations. */
  public static final String ENDPOINT = "/api/v1/midjourney/shorten_prompt";

  /** Creates a resource bound to the supplied transport and client options. */
  public ShortenPromptResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Starts prompt shortening and returns a terminal result or an accepted Task. */
  public Task<ShortenPromptResponse> create(ShortenPromptParams params) {
    return create(params, RequestOptions.none());
  }

  /** Starts prompt shortening with per-request options. */
  public Task<ShortenPromptResponse> create(ShortenPromptParams params, RequestOptions options) {
    return createHybridTask(params.action(), params.toMap(), options, ShortenPromptResponse.class);
  }

  /** Shortens a prompt and returns concise suggestions. */
  public ShortenPromptResponse run(ShortenPromptParams params) {
    return run(params, RequestOptions.none());
  }

  /** Shortens a prompt with per-request options and returns concise suggestions. */
  public ShortenPromptResponse run(ShortenPromptParams params, RequestOptions options) {
    return create(params, options).subscribe();
  }
}
