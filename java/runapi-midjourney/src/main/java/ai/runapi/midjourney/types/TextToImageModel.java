package ai.runapi.midjourney.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to image operations. */
public final class TextToImageModel extends MidjourneyValue {
  /** midjourney-v8.1 model slug. */
  public static final TextToImageModel MIDJOURNEY_V8_1 = new TextToImageModel("midjourney-v8.1");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToImageModel(String value) {
    super(value);
  }
}
