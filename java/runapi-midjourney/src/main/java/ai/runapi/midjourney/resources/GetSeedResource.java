package ai.runapi.midjourney.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.midjourney.types.GetSeedParams;
import ai.runapi.midjourney.types.GetSeedResponse;

/** Get Seed operations. */
public final class GetSeedResource extends MidjourneyResource {
  /** API endpoint path for get seed operations. */
  public static final String ENDPOINT = "/api/v1/midjourney/get_seed";

  /** Creates a resource bound to the supplied transport and client options. */
  public GetSeedResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Runs get seed and returns the response. */
  public GetSeedResponse run(GetSeedParams params) {
    return run(params, RequestOptions.none());
  }

  /** Runs get seed with per-request options and returns the response. */
  public GetSeedResponse run(GetSeedParams params, RequestOptions options) {
    return runSync(params.action(), params.toMap(), options, GetSeedResponse.class);
  }
}
