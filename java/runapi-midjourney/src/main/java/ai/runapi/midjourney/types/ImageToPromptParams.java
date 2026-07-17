package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for image to prompt operations. */
public final class ImageToPromptParams {
  private final String sourceImageUrl;

  private ImageToPromptParams(Builder builder) {
    this.sourceImageUrl = MidjourneyParamUtils.requireNonBlank(builder.sourceImageUrl, "sourceImageUrl");
  }

  /** Creates a new ImageToPromptParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/image-to-prompt";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("source_image_url", MidjourneyParamUtils.wireValue(sourceImageUrl));
    return MidjourneyParamUtils.compact(raw);
  }



  /** Builder for {@link ImageToPromptParams}. */
  public static final class Builder {
    private String sourceImageUrl;

    private Builder() {}

    /** Sets the source image URL. */
    public Builder sourceImageUrl(String value) {
      this.sourceImageUrl = MidjourneyParamUtils.requireNonBlank(value, "sourceImageUrl");
      return this;
    }

    /** Builds immutable image to prompt parameters. */
    public ImageToPromptParams build() {
      return new ImageToPromptParams(this);
    }
  }
}
