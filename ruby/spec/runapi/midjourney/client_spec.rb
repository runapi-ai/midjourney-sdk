# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Midjourney::Client do
  before do
    allow(ConnectionPool).to receive(:new).and_return(instance_double(ConnectionPool))
  end

  after { RunApi.api_key = nil }

  it "exposes all Midjourney resources" do
    client = described_class.new(api_key: "test-key")

    expect(client.text_to_image).to be_a(RunApi::Midjourney::Resources::TextToImage)
    expect(client.edit_image).to be_a(RunApi::Midjourney::Resources::EditImage)
    expect(client.image_to_video).to be_a(RunApi::Midjourney::Resources::ImageToVideo)
    expect(client.image_to_prompt).to be_a(RunApi::Midjourney::Resources::ImageToPrompt)
    expect(client.get_seed).to be_a(RunApi::Midjourney::Resources::GetSeed)
  end
end
