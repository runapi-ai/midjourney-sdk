export const contract = {
  "edit-image": {
    "models": [
      "midjourney-edit-image"
    ],
    "fields_by_model": {
      "midjourney-edit-image": {
        "prompt": {
          "required": true
        },
        "source_image_url": {
          "required": true
        }
      }
    }
  },
  "extend-video": {
    "models": [
      "midjourney-image-to-video"
    ],
    "fields_by_model": {
      "midjourney-image-to-video": {
        "source_task_id": {
          "required": true
        }
      }
    }
  },
  "get-seed": {
    "models": [],
    "fields_by_model": {
      "_": {
        "image_id": {
          "required": true
        }
      }
    }
  },
  "image-to-prompt": {
    "models": [],
    "fields_by_model": {
      "_": {
        "source_image_url": {
          "required": true
        }
      }
    }
  },
  "image-to-video": {
    "models": [
      "midjourney-image-to-video"
    ],
    "fields_by_model": {
      "midjourney-image-to-video": {
        "output_resolution": {
          "enum": [
            "480p"
          ]
        },
        "source_image_url": {
          "required": true
        }
      }
    }
  },
  "shorten-prompt": {
    "models": [],
    "fields_by_model": {
      "_": {
        "prompt": {
          "required": true
        }
      }
    }
  },
  "text-to-image": {
    "models": [
      "midjourney-v8.1"
    ],
    "fields_by_model": {
      "midjourney-v8.1": {
        "prompt": {
          "required": true
        }
      }
    }
  }
} as const;
