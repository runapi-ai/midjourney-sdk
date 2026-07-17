# frozen_string_literal: true

module RunApi
  module Midjourney
    module Resources
      # Derives prompt suggestions from an image.
      class ImageToPrompt
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/midjourney/image_to_prompt"
        RESPONSE_CLASS = Types::ImageToPromptResponse

        def initialize(http) = (@http = http)

        def run(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["image-to-prompt"], params)
          request(:post, ENDPOINT, body: params, options: options)
        end
      end
    end
  end
end
