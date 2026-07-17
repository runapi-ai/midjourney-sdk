CONTRACT = {
    "edit-image": {
        "models": ["midjourney-edit-image"],
        "fields_by_model": {
            "midjourney-edit-image": {
                "prompt": {
                    "required": True
                },
                "source_image_url": {
                    "required": True
                }
            }
        }
    },
    "get-seed": {
        "models": [],
        "fields_by_model": {
            "_": {
                "image_id": {
                    "required": True
                }
            }
        }
    },
    "image-to-prompt": {
        "models": [],
        "fields_by_model": {
            "_": {
                "source_image_url": {
                    "required": True
                }
            }
        }
    },
    "image-to-video": {
        "models": ["midjourney-image-to-video"],
        "fields_by_model": {
            "midjourney-image-to-video": {
                "output_resolution": {
                    "enum": ["480p"]
                },
                "source_image_url": {
                    "required": True
                }
            }
        }
    },
    "text-to-image": {
        "models": ["midjourney-v8.1"],
        "fields_by_model": {
            "midjourney-v8.1": {
                "prompt": {
                    "required": True
                }
            }
        }
    }
}
