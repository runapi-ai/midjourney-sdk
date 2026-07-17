# Midjourney Ruby SDK for RunAPI

Use `runapi-midjourney` for Midjourney image generation, editing, image-to-video, image-to-prompt, and seed lookup in Ruby applications and workers.

## Install

```bash
gem install runapi-midjourney
```

## Quick Start

```ruby
require "runapi/midjourney"

client = RunApi::Midjourney::Client.new
result = client.text_to_image.run(
  model: "midjourney-v8.1",
  prompt: "A cobalt blue ceramic vase on white marble"
)
puts result.images.first.url
```

Use `create`, `get`, and `run` for `text_to_image`, `edit_image`, and `image_to_video`. Use `run` directly for `image_to_prompt` and `get_seed`. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs#midjourney
- SDK docs: https://runapi.ai/docs#sdk-midjourney
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
