package ai.runapi.midjourney.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for edit image operations. */
public final class EditImageModel extends MidjourneyValue {
  /** midjourney-edit-image model slug. */
  public static final EditImageModel MIDJOURNEY_EDIT_IMAGE = new EditImageModel("midjourney-edit-image");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public EditImageModel(String value) {
    super(value);
  }
}
