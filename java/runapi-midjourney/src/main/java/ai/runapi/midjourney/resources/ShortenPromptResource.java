package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
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

  /** Shortens a prompt and returns concise suggestions. */
  public ShortenPromptResponse run(ShortenPromptParams params) {
    return run(params, RequestOptions.none());
  }

  /** Shortens a prompt with per-request options and returns concise suggestions. */
  public ShortenPromptResponse run(ShortenPromptParams params, RequestOptions options) {
    return runSync(params.action(), params.toMap(), options, ShortenPromptResponse.class);
  }
}
