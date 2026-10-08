package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.Map;

/** Parameters for prompt shortening operations. */
public final class ShortenPromptParams {
  private final String prompt;

  private ShortenPromptParams(Builder builder) {
    this.prompt = builder.prompt;
  }

  /** Creates a new ShortenPromptParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/shorten-prompt";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("prompt", MidjourneyParamUtils.wireValue(prompt));
    return MidjourneyParamUtils.compact(raw);
  }

  /** Builder for {@link ShortenPromptParams}. */
  public static final class Builder {
    private String prompt;

    private Builder() {}

    /** Sets the prompt to shorten. */
    public Builder prompt(String value) {
      this.prompt = value;
      return this;
    }

    /** Builds immutable prompt shortening parameters. */
    public ShortenPromptParams build() {
      return new ShortenPromptParams(this);
    }
  }
}
