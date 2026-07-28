<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/midjourney-sdk">Midjourney API SDK for RunAPI</a>
</h3>

<p align="center">
  Midjourney API SDKs for JavaScript, Python, Ruby, Go, Java, and PHP on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/midjourney)](https://www.npmjs.com/package/@runapi.ai/midjourney)
[![PyPI](https://img.shields.io/pypi/v/runapi-midjourney)](https://pypi.org/project/runapi-midjourney/)
[![RubyGems](https://img.shields.io/gem/v/runapi-midjourney)](https://rubygems.org/gems/runapi-midjourney)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/midjourney-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/midjourney-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-midjourney)](https://central.sonatype.com/artifact/ai.runapi/runapi-midjourney)
[![License](https://img.shields.io/github/license/runapi-ai/midjourney-sdk)](https://github.com/runapi-ai/midjourney-sdk/blob/main/LICENSE)

</div>
<br/>

The Midjourney SDK provides typed clients for text-to-image generation, prompt-guided image editing, image-to-video generation, first-video extension, image-to-prompt analysis, prompt shortening, and seed lookup. The public `midjourney-sdk` repository groups the non-PHP language packages, examples, CI, and language release tags. The PHP package is released from a split Composer repository.

## Install

```bash
npm install @runapi.ai/midjourney
pip install runapi-midjourney
gem install runapi-midjourney
go get github.com/runapi-ai/midjourney-sdk/go@latest
```

Java applications use `ai.runapi:runapi-midjourney`. PHP applications use the split [`runapi-ai/midjourney`](https://github.com/runapi-ai/midjourney-php) Composer package.

## JavaScript quick start

```typescript
import { MidjourneyClient } from '@runapi.ai/midjourney';

const client = new MidjourneyClient();
const result = await client.textToImage.run({
  model: 'midjourney-v8.1',
  prompt: 'A cobalt blue ceramic vase on white marble',
});

console.log(result.images[0].url);
```

## Resources

- `textToImage`: create, get, or create-and-poll a `midjourney-v8.1` image task.
- `editImage`: create, get, or create-and-poll a prompt-guided image edit.
- `imageToVideo`: create, get, or create-and-poll a video task from a source image.
- `extendVideo`: extend the first video from an account-owned completed `imageToVideo` task. Extension tasks cannot be chained.
- `imageToPrompt`: synchronously derive prompt suggestions from a source image.
- `shortenPrompt`: synchronously turn a prompt into 1 to 5 concise suggestions.
- `getSeed`: synchronously retrieve the seed for an account-owned completed Midjourney image.

Generated media URLs are temporary. Download and store results in your own durable storage.

## Links

- Model page: https://runapi.ai/models/midjourney
- Product docs: https://runapi.ai/docs/api/midjourney/text-to-image
- SDK docs: https://runapi.ai/docs/resources/sdks
- V8.1 text-to-image details: https://runapi.ai/models/midjourney/v8.1
- Image editing details: https://runapi.ai/models/midjourney/edit-image
- Image-to-video details: https://runapi.ai/models/midjourney/image-to-video
- Provider page: https://runapi.ai/providers/midjourney
- Full catalog: https://runapi.ai/models

## License

Licensed under the Apache License, Version 2.0.
