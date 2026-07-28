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
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/image_to_prompt", body: prompt_params)
      .and_return("prompts" => %w[one two three four], "billing" => {"reservation" => {"amount_cents" => 5}, "settlement" => {"charged_amount_cents" => 5, "amount_micro_cents" => 5_000_000}, "refund" => nil})
    image_to_prompt_result = image_to_prompt.run(**prompt_params)
    expect(image_to_prompt_result.prompts).to eq(%w[one two three four])
    expect(image_to_prompt_result.billing).to be_a(RunApi::Core::TaskBillingFacts)
    expect(image_to_prompt_result.billing.reservation.amount_cents).to eq(5)

    get_seed = RunApi::Midjourney::Resources::GetSeed.new(http)
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/get_seed", body: {image_id: "image-1"})
      .and_return("seed" => 8675309, "billing" => {"reservation" => nil, "settlement" => {"charged_amount_cents" => 1, "amount_micro_cents" => 1_000_000}, "refund" => nil})
    get_seed_result = get_seed.run(image_id: "image-1")
    expect(get_seed_result.seed).to eq(8675309)
    expect(get_seed_result.billing).to be_a(RunApi::Core::TaskBillingFacts)
    expect(get_seed_result.billing.settlement.charged_amount_cents).to eq(1)

    shorten_prompt = RunApi::Midjourney::Resources::ShortenPrompt.new(http)
    expect(http).to receive(:request).with(
      :post,
      "/api/v1/midjourney/shorten_prompt",
      body: {prompt: "A detailed cinematic mountain landscape"}
    ).and_return("prompts" => ["Concise mountain landscape"], "billing" => {"reservation" => nil, "settlement" => nil, "refund" => {"refunded_at" => "2026-07-27T00:00:00.000000Z"}})
    shorten_prompt_result = shorten_prompt.run(prompt: "A detailed cinematic mountain landscape")
    expect(shorten_prompt_result.prompts)
      .to eq(["Concise mountain landscape"])
    expect(shorten_prompt_result.billing).to be_a(RunApi::Core::TaskBillingFacts)
    expect(shorten_prompt_result.billing.refund.refunded_at).to eq("2026-07-27T00:00:00.000000Z")
  end

  it "preserves get seed error responses without requiring a seed" do
    get_seed = RunApi::Midjourney::Resources::GetSeed.new(http)
    expect(http).to receive(:request).with(:post, "/api/v1/midjourney/get_seed", body: {image_id: "image-1"})
      .and_return("error" => "Seed lookup failed")

    result = get_seed.run(image_id: "image-1")
    expect(result.seed).to be_nil
    expect(result.error).to eq("Seed lookup failed")
  end
end
