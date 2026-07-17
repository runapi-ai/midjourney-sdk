package ai.runapi.midjourney;

import ai.runapi.core.BaseClient;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import ai.runapi.midjourney.resources.EditImageResource;
import ai.runapi.midjourney.resources.GetSeedResource;
import ai.runapi.midjourney.resources.ImageToPromptResource;
import ai.runapi.midjourney.resources.ImageToVideoResource;
import ai.runapi.midjourney.resources.TextToImageResource;

/** Midjourney model-family Java SDK client. */
public final class MidjourneyClient extends BaseClient {
  private final EditImageResource editImage;
  private final GetSeedResource getSeed;
  private final ImageToPromptResource imageToPrompt;
  private final ImageToVideoResource imageToVideo;
  private final TextToImageResource textToImage;

  private MidjourneyClient(ClientOptions options) {
    super(options);
    this.editImage = new EditImageResource(transport(), options());
    this.getSeed = new GetSeedResource(transport(), options());
    this.imageToPrompt = new ImageToPromptResource(transport(), options());
    this.imageToVideo = new ImageToVideoResource(transport(), options());
    this.textToImage = new TextToImageResource(transport(), options());
  }

  /** Creates a new MidjourneyClient builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Edit Image operations. */
  public EditImageResource editImage() {
    return editImage;
  }

  /** Get Seed operations. */
  public GetSeedResource getSeed() {
    return getSeed;
  }

  /** Image To Prompt operations. */
  public ImageToPromptResource imageToPrompt() {
    return imageToPrompt;
  }

  /** Image To Video operations. */
  public ImageToVideoResource imageToVideo() {
    return imageToVideo;
  }

  /** Text To Image operations. */
  public TextToImageResource textToImage() {
    return textToImage;
  }

  /** Builder for {@link MidjourneyClient}. */
  public static final class Builder extends BaseClient.Builder<Builder> {
    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    @Override
    public Builder apiKey(String value) {
      return super.apiKey(value);
    }

    /** Sets the RunAPI base URL. If omitted, the SDK reads {@code RUNAPI_BASE_URL}. */
    @Override
    public Builder baseUrl(String value) {
      return super.baseUrl(value);
    }

    /** Sets the RunAPI base URL from a URI. */
    @Override
    public Builder baseUrl(URI value) {
      return super.baseUrl(value);
    }

    /** Sets a custom HTTP transport. User-provided transports are not closed by SDK clients. */
    @Override
    public Builder transport(HttpTransport value) {
      return super.transport(value);
    }

    /** Builds an immutable MidjourneyClient. */
    @Override
    public MidjourneyClient build() {
      return new MidjourneyClient(options.build());
    }
  }
}
