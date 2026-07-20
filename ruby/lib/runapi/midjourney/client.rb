# frozen_string_literal: true

module RunApi
  module Midjourney
    # Midjourney image generation, editing, image-to-video, and helper client.
    class Client < RunApi::Core::Client
      attr_reader :text_to_image, :edit_image, :image_to_video, :image_to_prompt, :shorten_prompt, :get_seed

      def initialize(api_key: nil, **options)
        super
        @text_to_image = Resources::TextToImage.new(http)
        @edit_image = Resources::EditImage.new(http)
        @image_to_video = Resources::ImageToVideo.new(http)
        @image_to_prompt = Resources::ImageToPrompt.new(http)
        @shorten_prompt = Resources::ShortenPrompt.new(http)
        @get_seed = Resources::GetSeed.new(http)
      end
    end
  end
end
