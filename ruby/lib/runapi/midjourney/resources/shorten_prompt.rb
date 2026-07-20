# frozen_string_literal: true

module RunApi
  module Midjourney
    module Resources
      # Derives concise suggestions from a prompt.
      class ShortenPrompt
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/midjourney/shorten_prompt"
        RESPONSE_CLASS = Types::ShortenPromptResponse

        def initialize(http) = (@http = http)

        def run(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["shorten-prompt"], params)
          request(:post, ENDPOINT, body: params, options: options)
        end
      end
    end
  end
end
