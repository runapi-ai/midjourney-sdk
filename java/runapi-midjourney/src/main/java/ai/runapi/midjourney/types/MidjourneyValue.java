package ai.runapi.midjourney.types;

import ai.runapi.core.types.RunApiValue;

abstract class MidjourneyValue extends RunApiValue {
  MidjourneyValue(String value) {
    super(value);
  }
}
