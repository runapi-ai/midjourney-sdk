package ai.runapi.midjourney.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for image to video operations. */
public final class ImageToVideoModel extends MidjourneyValue {
  /** midjourney-image-to-video model slug. */
  public static final ImageToVideoModel MIDJOURNEY_IMAGE_TO_VIDEO = new ImageToVideoModel("midjourney-image-to-video");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public ImageToVideoModel(String value) {
    super(value);
  }
}
