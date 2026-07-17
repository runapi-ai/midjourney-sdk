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
	imageToPromptPath = "/api/v1/midjourney/image_to_prompt"
	getSeedPath       = "/api/v1/midjourney/get_seed"
)

// Client provides access to Midjourney image, video, and helper endpoints.
type Client struct {
	base.Base
	TextToImage   *TextToImage
	EditImage     *EditImage
	ImageToVideo  *ImageToVideo
	ImageToPrompt *ImageToPrompt
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
		ImageToPrompt: &ImageToPrompt{http: httpClient},
		GetSeed:       &GetSeed{http: httpClient},
	}
}

// TextToImage generates images from a text prompt.
type TextToImage struct{ http core.HTTPClient }

// Create submits a text-to-image task and returns immediately with a task ID.
func (r *TextToImage) Create(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	return createAsync(ctx, r.http, "text-to-image", textToImagePath, params, opts...)
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
	return createAsync(ctx, r.http, "edit-image", editImagePath, params, opts...)
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
	return createAsync(ctx, r.http, "image-to-video", imageToVideoPath, params, opts...)
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

// ImageToPrompt derives prompt suggestions from an image.
type ImageToPrompt struct{ http core.HTTPClient }

// Run sends a synchronous image-to-prompt request.
func (r *ImageToPrompt) Run(ctx context.Context, params ImageToPromptParams, opts ...option.RequestOption) (*ImageToPromptResponse, error) {
	return runSync[ImageToPromptResponse](ctx, r.http, "image-to-prompt", imageToPromptPath, params, opts...)
}

// GetSeed looks up the seed for an account-owned generated image.
type GetSeed struct{ http core.HTTPClient }

// Run sends a synchronous seed lookup request.
func (r *GetSeed) Run(ctx context.Context, params GetSeedParams, opts ...option.RequestOption) (*GetSeedResponse, error) {
	return runSync[GetSeedResponse](ctx, r.http, "get-seed", getSeedPath, params, opts...)
}

func createAsync[T any](ctx context.Context, httpClient core.HTTPClient, action, path string, params T, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema[action], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, httpClient, path, body, requestOptions)
}

func getAsync[T any](ctx context.Context, httpClient core.HTTPClient, path, id string, opts ...option.RequestOption) (*T, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[T](ctx, httpClient, core.ResourcePath(path, id), requestOptions)
}

func runSync[R any, P any](ctx context.Context, httpClient core.HTTPClient, action, path string, params P, opts ...option.RequestOption) (*R, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema[action], body); err != nil {
		return nil, err
	}
	return core.PostJSON[R](ctx, httpClient, path, body, requestOptions)
}
