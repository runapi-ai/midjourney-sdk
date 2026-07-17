# Midjourney Java SDK for RunAPI

Use `ai.runapi:runapi-midjourney` for typed Midjourney request builders, task polling, synchronous helpers, and response models in Java applications.

## Install

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-midjourney:0.1.0")
}
```

## Quick Start

```java
MidjourneyClient client = MidjourneyClient.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

CompletedTextToImageResponse result = client.textToImage().run(
    TextToImageParams.builder()
        .model(TextToImageModel.MIDJOURNEY_V8_1)
        .prompt("A cobalt blue ceramic vase on white marble")
        .build()
);
```

Use `create`, `get`, and `run` for image and video tasks. `imageToPrompt().run(...)` and `getSeed().run(...)` return synchronously. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs#midjourney
- SDK docs: https://runapi.ai/docs#sdk-midjourney
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
