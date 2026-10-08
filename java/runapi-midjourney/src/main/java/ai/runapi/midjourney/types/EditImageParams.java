package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for edit image operations. */
public final class EditImageParams {
  private final String model;
  private final String prompt;
  private final String sourceImageUrl;
  private final String maskUrl;
  private final Boolean includeSplitImages;
  private final String callbackUrl;

  private EditImageParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.sourceImageUrl = builder.sourceImageUrl;
    this.maskUrl = builder.maskUrl;
    this.includeSplitImages = builder.includeSplitImages;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new EditImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/edit-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", MidjourneyParamUtils.wireValue(model));
    raw.put("prompt", MidjourneyParamUtils.wireValue(prompt));
    raw.put("source_image_url", MidjourneyParamUtils.wireValue(sourceImageUrl));
    raw.put("mask_url", MidjourneyParamUtils.wireValue(maskUrl));
    raw.put("include_split_images", MidjourneyParamUtils.wireValue(includeSplitImages));
    raw.put("callback_url", MidjourneyParamUtils.wireValue(callbackUrl));
    return MidjourneyParamUtils.compact(raw);
  }



  /** Builder for {@link EditImageParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String sourceImageUrl;
    private String maskUrl;
    private Boolean includeSplitImages;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(EditImageModel value) {
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

    /** Sets the mask URL. */
    public Builder maskUrl(String value) {
      this.maskUrl = value;
      return this;
    }

    /** Sets the include split images. */
    public Builder includeSplitImages(boolean value) {
      this.includeSplitImages = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = value;
      return this;
    }

    /** Builds immutable edit image parameters. */
    public EditImageParams build() {
      return new EditImageParams(this);
    }
  }
}
