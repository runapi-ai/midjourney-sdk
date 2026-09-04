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
          run_hybrid(ENDPOINT, body: params, options: options, response_class: RESPONSE_CLASS)
        end

        def subscribe(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["shorten-prompt"], params)
          subscribe_hybrid(ENDPOINT, body: params, options: options, response_class: RESPONSE_CLASS)
        end
      end
    end
  end
end
