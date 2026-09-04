# Midjourney Java SDK for RunAPI

Use `ai.runapi:runapi-midjourney` for typed Midjourney request builders, first-video extension, task polling, synchronous helpers, and response models in Java applications.

## Install

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-midjourney:0.3.1")
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

Use `create`, `get`, and `run` for image and video tasks, including `extendVideo()`. First-video extension accepts the task ID of an account-owned completed direct image-to-video task; extension tasks cannot be chained. `imageToPrompt().run(...)` and `getSeed().run(...)` return synchronously. `shortenPrompt().run(...)` returns the same typed result whether it completes immediately or is accepted for later retrieval. To observe an accepted result directly, call `shortenPrompt().create(...).subscribe()`. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs/api/midjourney/text-to-image
- SDK docs: https://runapi.ai/docs/resources/sdks
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
