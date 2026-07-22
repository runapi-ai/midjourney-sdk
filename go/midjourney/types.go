// Package midjourney provides the Midjourney image and video API client.
package midjourney

// TextToImageModel identifies the Midjourney text-to-image model.
type TextToImageModel string

// EditImageModel identifies the Midjourney image editing model.
type EditImageModel string

// ImageToVideoModel identifies the Midjourney image-to-video model.
type ImageToVideoModel string

// OutputResolution controls image-to-video output dimensions.
type OutputResolution string

// TaskStatus represents the processing state of an asynchronous task.
type TaskStatus string

const (
	// ModelV81 generates images with Midjourney V8.1.
	ModelV81 TextToImageModel = "midjourney-v8.1"
	// ModelEditImage applies prompt-guided edits to an image.
	ModelEditImage EditImageModel = "midjourney-edit-image"
	// ModelImageToVideo animates a source image.
	ModelImageToVideo ImageToVideoModel = "midjourney-image-to-video"

	// Resolution480P produces 480p video output.
	Resolution480P OutputResolution = "480p"
)

// TextToImageParams configures a text-to-image request.
type TextToImageParams struct {
	Model                   TextToImageModel `json:"model" help:"required; model slug"`
	Prompt                  string           `json:"prompt" help:"required; image description"`
	EnablePromptTranslation *bool            `json:"enable_prompt_translation,omitempty" help:"optional; translate the prompt before generation; default false"`
	IncludeSplitImages      *bool            `json:"include_split_images,omitempty" help:"optional; return split image outputs when available; default false"`
	CallbackURL             string           `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// EditImageParams configures a prompt-guided image edit request.
type EditImageParams struct {
	Model              EditImageModel `json:"model" help:"required; model slug"`
	Prompt             string         `json:"prompt" help:"required; edit instructions"`
	SourceImageURL     string         `json:"source_image_url" help:"required; source image URL"`
	MaskURL            string         `json:"mask_url,omitempty" help:"optional; mask image URL"`
	IncludeSplitImages *bool          `json:"include_split_images,omitempty" help:"optional; return split image outputs when available; default false"`
	CallbackURL        string         `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// ImageToVideoParams configures an image-to-video request.
type ImageToVideoParams struct {
	Model             ImageToVideoModel `json:"model" help:"required; model slug"`
	Prompt            string            `json:"prompt,omitempty" help:"optional; motion description"`
	SourceImageURL    string            `json:"source_image_url" help:"required; source image URL"`
	LastFrameImageURL string            `json:"last_frame_image_url,omitempty" help:"optional; final frame image URL"`
	OutputResolution  OutputResolution  `json:"output_resolution,omitempty" help:"optional; 480p; default 480p"`
	EnableLoop        *bool             `json:"enable_loop,omitempty" help:"optional; make the generated video loop; default false"`
	CallbackURL       string            `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// ExtendVideoParams configures a first-video extension request.
type ExtendVideoParams struct {
	SourceTaskID string `json:"source_task_id" help:"required; completed image-to-video task ID"`
	Prompt       string `json:"prompt,omitempty" help:"optional; continuation description"`
	CallbackURL  string `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// ImageToPromptParams configures a synchronous image description request.
type ImageToPromptParams struct {
	SourceImageURL string `json:"source_image_url" help:"required; source image URL"`
}

// ShortenPromptParams configures a synchronous prompt shortening request.
type ShortenPromptParams struct {
	Prompt string `json:"prompt" help:"required; prompt to shorten"`
}

// GetSeedParams configures a synchronous seed lookup for an account-owned image.
type GetSeedParams struct {
	ImageID string `json:"image_id" help:"required; image ID from a completed Midjourney image task"`
}

// MediaURL contains a generated media URL.
type MediaURL struct {
	URL string `json:"url"`
}

// AsyncTaskResponse contains fields shared by Midjourney asynchronous responses.
type AsyncTaskResponse struct {
	ID       string     `json:"id"`
	Status   TaskStatus `json:"status"`
	Progress *int       `json:"progress,omitempty"`
	Error    string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// ImageTaskResponse is the status and result of a Midjourney image task.
type ImageTaskResponse struct {
	AsyncTaskResponse
	Images  []MediaURL `json:"images,omitempty"`
	ImageID string     `json:"image_id,omitempty"`
	Actions []string   `json:"actions,omitempty"`
}

// VideoTaskResponse is the status and result of a Midjourney video task.
type VideoTaskResponse struct {
	AsyncTaskResponse
	Videos  []MediaURL `json:"videos,omitempty"`
	VideoID string     `json:"video_id,omitempty"`
}

// ImageToPromptResponse contains prompt suggestions derived from an image.
type ImageToPromptResponse struct {
	Prompts []string `json:"prompts,omitempty"`
	Error   string   `json:"error,omitempty"`
}

// ShortenPromptResponse contains concise prompt suggestions.
type ShortenPromptResponse struct {
	Prompts []string `json:"prompts,omitempty"`
	Error   string   `json:"error,omitempty"`
}

// GetSeedResponse contains the seed associated with a generated image.
type GetSeedResponse struct {
	Seed  int64  `json:"seed,omitempty"`
	Error string `json:"error,omitempty"`
}
