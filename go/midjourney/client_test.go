package midjourney

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method   string
	path     string
	body     any
	response json.RawMessage
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	if s.response != nil {
		return s.response, nil
	}
	return json.RawMessage(`{"id":"task_123","status":"processing"}`), nil
}

func TestTextToImageCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	translate := true
	split := false

	_, err := client.TextToImage.Create(context.Background(), TextToImageParams{
		Model:                   ModelV81,
		Prompt:                  "A cobalt vase on white marble",
		EnablePromptTranslation: &translate,
		IncludeSplitImages:      &split,
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/midjourney/text_to_image" {
		t.Fatalf("unexpected create request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != string(ModelV81) || body["prompt"] != "A cobalt vase on white marble" {
		t.Fatalf("unexpected body: %#v", body)
	}
	if body["translation"] != nil || body["split_images"] != nil || body["enable_prompt_translation"] != true || body["include_split_images"] != false {
		t.Fatalf("expected public snake_case body without provider fields: %#v", body)
	}

	_, err = client.TextToImage.Get(context.Background(), "task_abc")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/midjourney/text_to_image/task_abc" {
		t.Fatalf("unexpected get request: %s %s", stub.method, stub.path)
	}
}

func TestEditImageCreateSendsSourceAndMask(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)

	_, err := client.EditImage.Create(context.Background(), EditImageParams{
		Model:          ModelEditImage,
		Prompt:         "Change the vase color to cobalt blue",
		SourceImageURL: "https://cdn.runapi.ai/public/samples/source.png",
		MaskURL:        "https://cdn.runapi.ai/public/samples/mask.png",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/midjourney/edit_image" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["source_image_url"] == nil || body["mask_url"] == nil || body["image_url"] != nil || body["mask"] != nil {
		t.Fatalf("unexpected body: %#v", body)
	}
}

func TestImageToVideoCreateAndDecode(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"task_123","status":"completed","video_id":"video_1","videos":[{"url":"https://media.runapi.ai/video.mp4"}]}`)}
	client := NewClientWithHTTP(stub)
	loop := false

	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:            ModelImageToVideo,
		Prompt:           "Slow cinematic orbit",
		SourceImageURL:   "https://cdn.runapi.ai/public/samples/source.png",
		OutputResolution: Resolution480P,
		EnableLoop:       &loop,
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/midjourney/image_to_video" {
		t.Fatalf("unexpected create request: %s %s", stub.method, stub.path)
	}

	result, err := client.ImageToVideo.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	if result.VideoID != "video_1" || len(result.Videos) != 1 || result.Videos[0].URL != "https://media.runapi.ai/video.mp4" {
		t.Fatalf("unexpected response: %#v", result)
	}
}

func TestSynchronousHelpersPostAndDecode(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"prompts":["Prompt one","Prompt two","Prompt three","Prompt four"]}`)}
	client := NewClientWithHTTP(stub)

	promptResult, err := client.ImageToPrompt.Run(context.Background(), ImageToPromptParams{
		SourceImageURL: "https://cdn.runapi.ai/public/samples/source.png",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/midjourney/image_to_prompt" || len(promptResult.Prompts) != 4 {
		t.Fatalf("unexpected image-to-prompt result: %#v", promptResult)
	}

	stub.response = json.RawMessage(`{"seed":8675309}`)
	seedResult, err := client.GetSeed.Run(context.Background(), GetSeedParams{ImageID: "image_123"})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/midjourney/get_seed" || seedResult.Seed != 8675309 {
		t.Fatalf("unexpected seed result: %#v", seedResult)
	}
	body := stub.body.(map[string]any)
	if body["image_id"] != "image_123" {
		t.Fatalf("unexpected seed body: %#v", body)
	}
}
