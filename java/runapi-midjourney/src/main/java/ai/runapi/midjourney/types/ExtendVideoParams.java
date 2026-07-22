package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.Map;

/** Parameters for extending the first video from a completed image-to-video task. */
public final class ExtendVideoParams {
  private final String sourceTaskId;
  private final String prompt;
  private final String callbackUrl;

  private ExtendVideoParams(Builder builder) {
    this.sourceTaskId = MidjourneyParamUtils.requireNonBlank(builder.sourceTaskId, "sourceTaskId");
    this.prompt = builder.prompt;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new ExtendVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/extend-video";
  }

  /** Converts these parameters to the public JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("source_task_id", MidjourneyParamUtils.wireValue(sourceTaskId));
    raw.put("prompt", MidjourneyParamUtils.wireValue(prompt));
    raw.put("callback_url", MidjourneyParamUtils.wireValue(callbackUrl));
    return MidjourneyParamUtils.compact(raw);
  }

  /** Builder for {@link ExtendVideoParams}. */
  public static final class Builder {
    private String sourceTaskId;
    private String prompt;
    private String callbackUrl;

    private Builder() {}

    /** Sets the completed image-to-video source task ID. */
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = MidjourneyParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the optional continuation prompt. */
    public Builder prompt(String value) {
      this.prompt = MidjourneyParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = MidjourneyParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable first-video extension parameters. */
    public ExtendVideoParams build() {
      return new ExtendVideoParams(this);
    }
  }
}
