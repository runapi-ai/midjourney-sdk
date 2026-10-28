# Changelog

## [js/v0.4.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.4.1) - 2026-10-28

### Changed
- Update development test tooling to Vitest 3 with Vite 6.
- Update development test tooling to Vitest 4.1.11 with Vite 6.4.4.


## [js/v0.4.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.4.0), [ruby/v0.5.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.5.0), [go/v0.5.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.5.0), [python/v0.6.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.6.0), [java/v0.4.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/java%2Fv0.4.0) - 2026-10-08

### Changed
- Send request parameters to the service without local validation. Model ids and parameter values the service supports work without an SDK upgrade; static types and enum constants remain for completion.
  Migration: Invalid parameters now fail with the validation error built from the service's 400 response, including its status and message, instead of a validation error raised locally before the request. The error type is unchanged: `ValidationError` in JavaScript, Python, and Ruby, `ValidationException` in Java and PHP, and `ErrValidation` in Go.


## [js/v0.3.3](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.3.3), [ruby/v0.4.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.4.1), [go/v0.4.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.4.1) - 2026-09-28

### Added
- Return usage.cost as a float USD amount on completed async Task query and webhook envelopes.

### Removed
- Remove the public Task billing object from Task envelopes.
  Migration: Read usage.cost on completed Task envelopes. Create, processing, and failed envelopes omit usage.


## [python/v0.5.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.5.0) - 2026-09-04

### Added
- Automatically follow accepted Task results for image-to-prompt, prompt shortening, and seed lookup.

## [go/v0.4.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.4.0) - 2026-09-04

### Added
- Add Create, Subscribe, and automatic Run support for image-to-prompt, prompt shortening, and seed lookup when a Task is accepted.

## [js/v0.3.2](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.3.2) - 2026-09-04

### Added
- Let prompt shortening run through accepted Task Results and report lifecycle updates with subscribe().

## [java/v0.3.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/java%2Fv0.3.1) - 2026-09-04

### Changed
- Let Java prompt shortening return the same typed terminal result after an immediate response or an accepted Task Result subscription.

## [ruby/v0.4.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.4.0) - 2026-09-04

### Added
- Add run and subscribe support for hybrid Task responses.


## [ruby/v0.3.2](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.3.2) - 2026-08-18

### Changed
- Allow Ruby clients to install the core SDK release that adds persistent Files and multipart Uploads alongside this model SDK.


## [python/v0.4.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.4.1) - 2026-07-29

### Fixed
- Point package documentation metadata to the current RunAPI Developer Docs.


## [go/v0.3.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.3.1) - 2026-07-28

### Added
- Expose persisted billing facts on task responses.

## [js/v0.3.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.3.1), [ruby/v0.3.1](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.3.1) - 2026-07-28

### Added
- Type task billing facts on task-backed responses.


## [python/v0.4.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.4.0) - 2026-07-24

### Added
- Expose shared Files, Account, and Pricing resources plus typed Task Billing Facts through the Provider Client.


## [js/v0.3.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.3.0), [ruby/v0.3.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.3.0), [go/v0.3.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.3.0), [python/v0.3.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.3.0), [java/v0.3.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/java%2Fv0.3.0) - 2026-07-22

### Added
- Add typed first-video extension resources with create, get, and run operations.


## [js/v0.2.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.2.0), [ruby/v0.2.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.2.0), [go/v0.2.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.2.0), [python/v0.2.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.2.0), [java/v0.2.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/java%2Fv0.2.0) - 2026-07-20

### Added
- Add typed synchronous prompt shortening resources, request parameters, response models, and examples to every Midjourney SDK package.


## [js/v0.1.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/js%2Fv0.1.0), [ruby/v0.1.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/ruby%2Fv0.1.0), [go/v0.1.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/go%2Fv0.1.0), [python/v0.1.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/python%2Fv0.1.0), [java/v0.1.0](https://github.com/runapi-ai/midjourney-sdk/releases/tag/java%2Fv0.1.0) - 2026-07-17

### Added
- Publish the initial Midjourney SDK packages for JavaScript, Ruby, Go, Python, and Java.
- Support image generation, editing, image-to-video, image-to-prompt, and seed lookup workflows.
