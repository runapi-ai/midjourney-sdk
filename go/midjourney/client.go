// Package midjourney provides the Midjourney image and video API client.
//
//	client, err := midjourney.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.TextToImage.Run(ctx, midjourney.TextToImageParams{
//	    Model: midjourney.ModelV81,
//	    Prompt: "A cobalt vase on white marble",
//	})
package midjourney

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	textToImagePath   = "/api/v1/midjourney/text_to_image"
	editImagePath     = "/api/v1/midjourney/edit_image"
	imageToVideoPath  = "/api/v1/midjourney/image_to_video"
	extendVideoPath   = "/api/v1/midjourney/extend_video"
	imageToPromptPath = "/api/v1/midjourney/image_to_prompt"
	shortenPromptPath = "/api/v1/midjourney/shorten_prompt"
	getSeedPath       = "/api/v1/midjourney/get_seed"
)

// Client provides access to Midjourney image, video, and helper endpoints.
type Client struct {
	base.Base
	TextToImage   *TextToImage
	EditImage     *EditImage
	ImageToVideo  *ImageToVideo
	ExtendVideo   *ExtendVideo
	ImageToPrompt *ImageToPrompt
	ShortenPrompt *ShortenPrompt
	GetSeed       *GetSeed
}

// NewClient creates a Midjourney client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates a Midjourney client using a pre-configured HTTP client.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:          base.New(httpClient),
		TextToImage:   &TextToImage{http: httpClient},
		EditImage:     &EditImage{http: httpClient},
		ImageToVideo:  &ImageToVideo{http: httpClient},
		ExtendVideo:   &ExtendVideo{http: httpClient},
		ImageToPrompt: &ImageToPrompt{http: httpClient},
		ShortenPrompt: &ShortenPrompt{http: httpClient},
		GetSeed:       &GetSeed{http: httpClient},
	}
}

// TextToImage generates images from a text prompt.
type TextToImage struct{ http core.HTTPClient }

// Create submits a text-to-image task and returns immediately with a task ID.
func (r *TextToImage) Create(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	return createAsync(ctx, r.http, textToImagePath, params, opts...)
}

// Get retrieves the current status and result of a text-to-image task.
func (r *TextToImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	return getAsync[ImageTaskResponse](ctx, r.http, textToImagePath, id, opts...)
}

// Run submits a text-to-image task and polls until completion.
func (r *TextToImage) Run(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*ImageTaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

// EditImage applies prompt-guided edits to a source image.
type EditImage struct{ http core.HTTPClient }

// Create submits an image edit task and returns immediately with a task ID.
func (r *EditImage) Create(ctx context.Context, params EditImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	return createAsync(ctx, r.http, editImagePath, params, opts...)
}

// Get retrieves the current status and result of an image edit task.
func (r *EditImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	return getAsync[ImageTaskResponse](ctx, r.http, editImagePath, id, opts...)
}

// Run submits an image edit task and polls until completion.
func (r *EditImage) Run(ctx context.Context, params EditImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*ImageTaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

// ImageToVideo generates video from a source image.
type ImageToVideo struct{ http core.HTTPClient }

// Create submits an image-to-video task and returns immediately with a task ID.
func (r *ImageToVideo) Create(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	return createAsync(ctx, r.http, imageToVideoPath, params, opts...)
}

// Get retrieves the current status and result of an image-to-video task.
func (r *ImageToVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	return getAsync[VideoTaskResponse](ctx, r.http, imageToVideoPath, id, opts...)
}

// Run submits an image-to-video task and polls until completion.
func (r *ImageToVideo) Run(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*VideoTaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

// ExtendVideo continues the first video from a completed image-to-video task.
type ExtendVideo struct{ http core.HTTPClient }

// Create submits a first-video extension task and returns immediately with a task ID.
func (r *ExtendVideo) Create(ctx context.Context, params ExtendVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, extendVideoPath, body, requestOptions)
}

// Get retrieves the current status and result of a first-video extension task.
func (r *ExtendVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	return getAsync[VideoTaskResponse](ctx, r.http, extendVideoPath, id, opts...)
}

// Run submits a first-video extension task and polls until completion.
func (r *ExtendVideo) Run(ctx context.Context, params ExtendVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*VideoTaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

// ImageToPrompt derives prompt suggestions from an image.
type ImageToPrompt struct{ http core.HTTPClient }

// Create submits image-to-prompt and returns either its terminal response or
// an accepted Task that can be resumed through Subscribe.
func (r *ImageToPrompt) Create(ctx context.Context, params ImageToPromptParams, opts ...option.RequestOption) (*core.HybridCreateResponse[ImageToPromptResponse], error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.CreateHybrid[ImageToPromptResponse](ctx, r.http, imageToPromptPath, body, requestOptions)
}

// Subscribe follows an accepted image-to-prompt Task to its terminal response.
func (r *ImageToPrompt) Subscribe(ctx context.Context, acceptance *core.HybridAcceptance, opts ...option.RequestOption) (*ImageToPromptResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.SubscribeHybrid[ImageToPromptResponse](ctx, r.http, acceptance, requestOptions, pollingOptions)
}

// Run returns the same image-to-prompt response whether the endpoint completes
// directly or first returns 202 Accepted.
func (r *ImageToPrompt) Run(ctx context.Context, params ImageToPromptParams, opts ...option.RequestOption) (*ImageToPromptResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.RunHybrid[ImageToPromptResponse](ctx, r.http, imageToPromptPath, body, requestOptions, pollingOptions)
}

// ShortenPrompt derives concise suggestions from a prompt.
type ShortenPrompt struct{ http core.HTTPClient }

// Create submits prompt shortening and returns either its terminal response or
// an accepted Task that can be resumed through Subscribe.
func (r *ShortenPrompt) Create(ctx context.Context, params ShortenPromptParams, opts ...option.RequestOption) (*core.HybridCreateResponse[ShortenPromptResponse], error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.CreateHybrid[ShortenPromptResponse](ctx, r.http, shortenPromptPath, body, requestOptions)
}

// Subscribe follows an accepted prompt-shortening Task to its terminal response.
func (r *ShortenPrompt) Subscribe(ctx context.Context, acceptance *core.HybridAcceptance, opts ...option.RequestOption) (*ShortenPromptResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.SubscribeHybrid[ShortenPromptResponse](ctx, r.http, acceptance, requestOptions, pollingOptions)
}

// Run sends prompt shortening and returns the same terminal response whether
// the endpoint completes directly or first returns 202 Accepted.
func (r *ShortenPrompt) Run(ctx context.Context, params ShortenPromptParams, opts ...option.RequestOption) (*ShortenPromptResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.RunHybrid[ShortenPromptResponse](ctx, r.http, shortenPromptPath, body, requestOptions, pollingOptions)
}

// GetSeed looks up the seed for an account-owned generated image.
type GetSeed struct{ http core.HTTPClient }

// Create submits a seed lookup and returns either its terminal response or an
// accepted Task that can be resumed through Subscribe.
func (r *GetSeed) Create(ctx context.Context, params GetSeedParams, opts ...option.RequestOption) (*core.HybridCreateResponse[GetSeedResponse], error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.CreateHybrid[GetSeedResponse](ctx, r.http, getSeedPath, body, requestOptions)
}

// Subscribe follows an accepted seed-lookup Task to its terminal response.
func (r *GetSeed) Subscribe(ctx context.Context, acceptance *core.HybridAcceptance, opts ...option.RequestOption) (*GetSeedResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.SubscribeHybrid[GetSeedResponse](ctx, r.http, acceptance, requestOptions, pollingOptions)
}

// Run returns the same seed response whether the endpoint completes directly
// or first returns 202 Accepted.
func (r *GetSeed) Run(ctx context.Context, params GetSeedParams, opts ...option.RequestOption) (*GetSeedResponse, error) {
	requestOptions, pollingOptions := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.RunHybrid[GetSeedResponse](ctx, r.http, getSeedPath, body, requestOptions, pollingOptions)
}

func createAsync[T any](ctx context.Context, httpClient core.HTTPClient, path string, params T, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	return core.PostJSON[core.TaskCreateResponse](ctx, httpClient, path, body, requestOptions)
}

func getAsync[T any](ctx context.Context, httpClient core.HTTPClient, path, id string, opts ...option.RequestOption) (*T, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[T](ctx, httpClient, core.ResourcePath(path, id), requestOptions)
}
