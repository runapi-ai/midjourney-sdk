# Midjourney Python SDK for RunAPI

Use `runapi-midjourney` for Midjourney image generation, editing, image-to-video, first-video extension, image-to-prompt, prompt shortening, and seed lookup in Python applications and workers.

## Install

```bash
pip install runapi-midjourney
```

## Quick Start

```python
from runapi.midjourney import MidjourneyClient

client = MidjourneyClient()
result = client.text_to_image.run(
    model="midjourney-v8.1",
    prompt="A cobalt blue ceramic vase on white marble",
)
print(result.images[0].url)
```

Use `create`, `get`, and `run` for `text_to_image`, `edit_image`, `image_to_video`, and `extend_video`. `extend_video` accepts the task ID of an account-owned completed direct image-to-video task and continues its first video; extension tasks cannot be chained. Use `run` directly for `image_to_prompt`, `shorten_prompt`, and `get_seed`. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs#midjourney
- SDK docs: https://runapi.ai/docs#sdk-midjourney
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
