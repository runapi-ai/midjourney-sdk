package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for image to video operations. */
public final class ImageToVideoParams {
  private final String model;
  private final String prompt;
  private final String sourceImageUrl;
  private final String lastFrameImageUrl;
  private final String outputResolution;
  private final Boolean enableLoop;
  private final String callbackUrl;

  private ImageToVideoParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.sourceImageUrl = builder.sourceImageUrl;
    this.lastFrameImageUrl = builder.lastFrameImageUrl;
    this.outputResolution = builder.outputResolution;
    this.enableLoop = builder.enableLoop;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new ImageToVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/image-to-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", MidjourneyParamUtils.wireValue(model));
    raw.put("prompt", MidjourneyParamUtils.wireValue(prompt));
    raw.put("source_image_url", MidjourneyParamUtils.wireValue(sourceImageUrl));
    raw.put("last_frame_image_url", MidjourneyParamUtils.wireValue(lastFrameImageUrl));
    raw.put("output_resolution", MidjourneyParamUtils.wireValue(outputResolution));
    raw.put("enable_loop", MidjourneyParamUtils.wireValue(enableLoop));
    raw.put("callback_url", MidjourneyParamUtils.wireValue(callbackUrl));
    return MidjourneyParamUtils.compact(raw);
  }



  /** Builder for {@link ImageToVideoParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String sourceImageUrl;
    private String lastFrameImageUrl;
    private String outputResolution;
    private Boolean enableLoop;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(ImageToVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = value;
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = value;
      return this;
    }

    /** Sets the source image URL. */
    public Builder sourceImageUrl(String value) {
      this.sourceImageUrl = value;
      return this;
    }

    /** Sets the last frame image URL. */
    public Builder lastFrameImageUrl(String value) {
      this.lastFrameImageUrl = value;
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = value;
      return this;
    }

    /** Sets the enable loop. */
    public Builder enableLoop(boolean value) {
      this.enableLoop = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = value;
      return this;
    }

    /** Builds immutable image to video parameters. */
    public ImageToVideoParams build() {
      return new ImageToVideoParams(this);
    }
  }
}
