# Midjourney Go SDK for RunAPI

Use the Midjourney Go module for typed image generation, editing, image-to-video, image-to-prompt, and seed lookup in Go services and command-line tools.

## Install

```bash
go get github.com/runapi-ai/midjourney-sdk/go@latest
```

## Quick Start

```go
client, err := midjourney.NewClient()
result, err := client.TextToImage.Run(context.Background(), midjourney.TextToImageParams{
  Model:  midjourney.ModelV81,
  Prompt: "A cobalt blue ceramic vase on white marble",
})
fmt.Println(result.Images[0].URL)
```

Use `Create`, `Get`, and `Run` for `TextToImage`, `EditImage`, and `ImageToVideo`. Use `Run` directly for `ImageToPrompt` and `GetSeed`. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs#midjourney
- SDK docs: https://runapi.ai/docs#sdk-midjourney
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
