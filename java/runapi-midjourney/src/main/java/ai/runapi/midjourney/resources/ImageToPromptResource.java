package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.midjourney.types.ImageToPromptParams;
import ai.runapi.midjourney.types.ImageToPromptResponse;

/** Image To Prompt operations. */
public final class ImageToPromptResource extends MidjourneyResource {
  /** API endpoint path for image to prompt operations. */
  public static final String ENDPOINT = "/api/v1/midjourney/image_to_prompt";

  /** Creates a resource bound to the supplied transport and client options. */
  public ImageToPromptResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Runs image to prompt and returns the response. */
  public ImageToPromptResponse run(ImageToPromptParams params) {
    return run(params, RequestOptions.none());
  }

  /** Runs image to prompt with per-request options and returns the response. */
  public ImageToPromptResponse run(ImageToPromptParams params, RequestOptions options) {
    return runSync(params.action(), params.toMap(), options, ImageToPromptResponse.class);
  }
}
