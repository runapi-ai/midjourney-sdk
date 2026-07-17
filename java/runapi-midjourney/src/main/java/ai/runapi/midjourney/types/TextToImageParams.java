package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for text to image operations. */
public final class TextToImageParams {
  private final String model;
  private final String prompt;
  private final Boolean enablePromptTranslation;
  private final Boolean includeSplitImages;
  private final String callbackUrl;

  private TextToImageParams(Builder builder) {
    this.model = builder.model;
    this.prompt = MidjourneyParamUtils.requireNonBlank(builder.prompt, "prompt");
    this.enablePromptTranslation = builder.enablePromptTranslation;
    this.includeSplitImages = builder.includeSplitImages;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new TextToImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/text-to-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", MidjourneyParamUtils.wireValue(model));
    raw.put("prompt", MidjourneyParamUtils.wireValue(prompt));
    raw.put("enable_prompt_translation", MidjourneyParamUtils.wireValue(enablePromptTranslation));
    raw.put("include_split_images", MidjourneyParamUtils.wireValue(includeSplitImages));
    raw.put("callback_url", MidjourneyParamUtils.wireValue(callbackUrl));
    return MidjourneyParamUtils.compact(raw);
  }



  /** Builder for {@link TextToImageParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private Boolean enablePromptTranslation;
    private Boolean includeSplitImages;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(TextToImageModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = MidjourneyParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = MidjourneyParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the enable prompt translation. */
    public Builder enablePromptTranslation(boolean value) {
      this.enablePromptTranslation = value;
      return this;
    }

    /** Sets the include split images. */
    public Builder includeSplitImages(boolean value) {
      this.includeSplitImages = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = MidjourneyParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable text to image parameters. */
    public TextToImageParams build() {
      return new TextToImageParams(this);
    }
  }
}
