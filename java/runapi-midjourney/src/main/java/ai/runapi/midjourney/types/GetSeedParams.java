package ai.runapi.midjourney.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for get seed operations. */
public final class GetSeedParams {
  private final String imageId;

  private GetSeedParams(Builder builder) {
    this.imageId = builder.imageId;
  }

  /** Creates a new GetSeedParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "midjourney/get-seed";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("image_id", MidjourneyParamUtils.wireValue(imageId));
    return MidjourneyParamUtils.compact(raw);
  }



  /** Builder for {@link GetSeedParams}. */
  public static final class Builder {
    private String imageId;

    private Builder() {}

    /** Sets the image ID. */
    public Builder imageId(String value) {
      this.imageId = value;
      return this;
    }

    /** Builds immutable get seed parameters. */
    public GetSeedParams build() {
      return new GetSeedParams(this);
    }
  }
}
