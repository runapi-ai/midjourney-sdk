# Midjourney JavaScript SDK for RunAPI

Use `@runapi.ai/midjourney` for typed Midjourney image generation, editing, image-to-video, image-to-prompt, prompt shortening, and seed lookup in JavaScript or TypeScript applications.

## Install

```bash
npm install @runapi.ai/midjourney
```

## Quick Start

```typescript
import { MidjourneyClient } from '@runapi.ai/midjourney';

const client = new MidjourneyClient();
const result = await client.textToImage.run({
  model: 'midjourney-v8.1',
  prompt: 'A cobalt blue ceramic vase on white marble',
});
console.log(result.images[0].url);
```

Use `create`, `get`, and `run` for `textToImage`, `editImage`, and `imageToVideo`. Use `run` directly for the synchronous `imageToPrompt`, `shortenPrompt`, and `getSeed` resources. Generated media URLs are temporary and should be stored in durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs#midjourney
- SDK docs: https://runapi.ai/docs#sdk-midjourney
- V8.1 details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Repository: https://github.com/runapi-ai/midjourney-sdk

Licensed under the Apache License, Version 2.0.
