# frozen_string_literal: true

require "spec_helper"

RSpec.describe "Midjourney resources" do
  let(:http) { instance_double(RunApi::Core::HttpClient) }

  it "posts and gets text-to-image tasks" do
    resource = RunApi::Midjourney::Resources::TextToImage.new(http)
    params = {model: "midjourney-v8.1", prompt: "A cobalt vase", include_split_images: false}

    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/text_to_image", body: params).and_return("id" => "t-1")
    resource.create(**params)
    expect(http).to receive(:request).with(:get, "/api/v1/midjourney/text_to_image/t-1").and_return("id" => "t-1", "status" => "processing")
    resource.get("t-1")
  end

  it "posts image editing and image-to-video requests with public fields" do
    edit = RunApi::Midjourney::Resources::EditImage.new(http)
    edit_params = {
      model: "midjourney-edit-image",
      prompt: "Change the vase color",
      source_image_url: "https://cdn.runapi.ai/public/samples/source.png",
      mask_url: "https://cdn.runapi.ai/public/samples/mask.png"
    }
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/edit_image", body: edit_params).and_return("id" => "t-2")
    edit.create(**edit_params)

    video = RunApi::Midjourney::Resources::ImageToVideo.new(http)
    video_params = {
      model: "midjourney-image-to-video",
      source_image_url: "https://cdn.runapi.ai/public/samples/source.png",
      output_resolution: "480p",
      enable_loop: false
    }
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/image_to_video", body: video_params).and_return("id" => "t-3")
    video.create(**video_params)
  end

  it "posts and gets first-video extensions with only public fields" do
    resource = RunApi::Midjourney::Resources::ExtendVideo.new(http)
    params = {
      source_task_id: "task-source",
      prompt: "Continue the camera orbit",
      callback_url: "https://example.test/callback"
    }

    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/extend_video", body: params).and_return("id" => "t-4")
    resource.create(**params)
    expect(http).to receive(:request).with(:get, "/api/v1/midjourney/extend_video/t-4").and_return("id" => "t-4", "status" => "processing")
    resource.get("t-4")
  end

  it "runs synchronous helper requests" do
    image_to_prompt = RunApi::Midjourney::Resources::ImageToPrompt.new(http)
    prompt_params = {source_image_url: "https://cdn.runapi.ai/public/samples/source.png"}
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/image_to_prompt", body: prompt_params, options: anything)
      .and_return("prompts" => %w[one two three four])
    image_to_prompt_result = image_to_prompt.run(**prompt_params)
    expect(image_to_prompt_result.prompts).to eq(%w[one two three four])

    get_seed = RunApi::Midjourney::Resources::GetSeed.new(http)
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/get_seed", body: {image_id: "image-1"}, options: anything)
      .and_return("seed" => 8675309)
    get_seed_result = get_seed.run(image_id: "image-1")
    expect(get_seed_result.seed).to eq(8675309)

    shorten_prompt = RunApi::Midjourney::Resources::ShortenPrompt.new(http)
    expect(http).to receive(:request) do |method, path, body:, options:|
      expect([method, path, body]).to eq([
        :post,
        "/api/v1/midjourney/shorten_prompt",
        {prompt: "A detailed cinematic mountain landscape"}
      ])
      expect(options.headers.fetch("Idempotency-Key")).to match(/\A[0-9a-f-]{36}\z/)
    end.and_return("prompts" => ["Concise mountain landscape"])
    shorten_prompt_result = shorten_prompt.run(prompt: "A detailed cinematic mountain landscape")
    expect(shorten_prompt_result.prompts)
      .to eq(["Concise mountain landscape"])
  end

  it "follows an accepted shorten prompt task to its terminal response" do
    client = RunApi::Core::HttpClient.new(
      RunApi::Core::ClientOptions.new(
        api_key: "test-key",
        base_url: "https://runapi.ai",
        max_retries: 1,
        retry_base_delay: 0,
        retry_max_delay: 0
      )
    )
    resource = RunApi::Midjourney::Resources::ShortenPrompt.new(client)
    location = "https://runapi.ai/api/v1/tasks/task-1/result"
    idempotency_keys = []

    stub_request(:post, "https://runapi.ai/api/v1/midjourney/shorten_prompt")
      .to_return do |request|
        idempotency_keys << request.headers["Idempotency-Key"]
        if idempotency_keys.one?
          {status: 503, body: '{"error":"temporarily unavailable"}'}
        else
          {
            status: 202,
            body: '{"id":"task-1","status":"processing"}',
            headers: {
              "Content-Type" => "application/json",
              "Location" => location,
              "Retry-After" => "0"
            }
          }
        end
      end
    stub_request(:get, location)
      .to_return(
        {
          status: 200,
          body: '{"id":"task-1","status":"processing"}',
          headers: {"Content-Type" => "application/json", "Retry-After" => "0"}
        },
        {
          status: 200,
          body: '{"id":"task-1","status":"completed","response":{"status":200,"content_type":"application/json","headers":{},"body":{"prompts":["Concise mountain landscape"]}},"usage":{"cost":0.05}}',
          headers: {"Content-Type" => "application/json"}
        }
      )

    result = resource.run(prompt: "A detailed cinematic mountain landscape")

    expect(result).to be_a(RunApi::Midjourney::Types::ShortenPromptResponse)
    expect(result.prompts).to eq(["Concise mountain landscape"])
    expect(
      a_request(:post, "https://runapi.ai/api/v1/midjourney/shorten_prompt")
        .with { |request| request.headers["Idempotency-Key"].to_s.match?(/\A[0-9a-f-]{36}\z/) }
    ).to have_been_made.twice
    expect(idempotency_keys.uniq).to have_attributes(length: 1)
  end

  it "subscribes to processing and terminal shorten prompt results" do
    client = RunApi::Core::HttpClient.new(
      RunApi::Core::ClientOptions.new(api_key: "test-key", base_url: "https://runapi.ai")
    )
    resource = RunApi::Midjourney::Resources::ShortenPrompt.new(client)
    location = "https://runapi.ai/api/v1/tasks/task-1/result"

    stub_request(:post, "https://runapi.ai/api/v1/midjourney/shorten_prompt")
      .to_return(
        status: 202,
        body: '{"id":"task-1","status":"processing"}',
        headers: {"Content-Type" => "application/json", "Location" => location}
      )
    stub_request(:get, location)
      .to_return(
        {
          status: 200,
          body: '{"id":"task-1","status":"processing"}',
          headers: {"Content-Type" => "application/json", "Retry-After" => "0"}
        },
        {
          status: 200,
          body: '{"id":"task-1","status":"completed","response":{"status":200,"content_type":"application/json","headers":{},"body":{"prompts":["Concise mountain landscape"]}},"usage":{"cost":0.05}}',
          headers: {"Content-Type" => "application/json"}
        }
      )

    updates = resource.subscribe(prompt: "A detailed cinematic mountain landscape").to_a

    expect(updates.first).to be_a(RunApi::Core::TaskResponse)
    expect(updates.first.status).to eq("processing")
    expect(updates.last).to be_a(RunApi::Midjourney::Types::ShortenPromptResponse)
    expect(updates.last.prompts).to eq(["Concise mountain landscape"])
  end

  it "raises a stable error for a failed accepted task" do
    shorten_prompt = RunApi::Midjourney::Resources::ShortenPrompt.new(http)
    location = "https://runapi.ai/api/v1/tasks/task-1/result"
    allow(http).to receive(:request).and_return(
      RunApi::Core::Response.new(
        body: {"id" => "task-1", "status" => "processing"},
        headers: {"Location" => location, "Content-Type" => "application/json"},
        status: 202
      ),
      RunApi::Core::Response.new(
        body: {
          "id" => "task-1",
          "status" => "failed",
          "response" => {
            "status" => 503,
            "content_type" => "application/json",
            "headers" => {"X-RunAPI-Task-Id" => "task-1"},
            "body" => {"error" => {"code" => "generation_failed", "message" => "Prompt rejected"}}
          }
        },
        headers: {"Content-Type" => "application/json"},
        status: 200
      )
    )

    expect { shorten_prompt.run(prompt: "A detailed cinematic mountain landscape") }
      .to raise_error(RunApi::Core::ServiceUnavailableError, "Prompt rejected") { |error|
        expect(error.status).to eq(503)
        expect(error.code).to eq("generation_failed")
        expect(error.runapi_task_id).to eq("task-1")
      }
  end

  it "preserves get seed error responses without requiring a seed" do
    get_seed = RunApi::Midjourney::Resources::GetSeed.new(http)
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/get_seed", body: {image_id: "image-1"}, options: anything)
      .and_return("error" => "Seed lookup failed")

    result = get_seed.run(image_id: "image-1")
    expect(result.seed).to be_nil
    expect(result.error).to eq("Seed lookup failed")
  end
end
