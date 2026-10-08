# frozen_string_literal: true

module RunApi
  module Midjourney
    module Resources
      # Looks up the seed for an account-owned generated image.
      class GetSeed
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/midjourney/get_seed"
        RESPONSE_CLASS = Types::GetSeedResponse

        def initialize(http) = (@http = http)

        def run(options: nil, **params)
          params = compact_params(params)
          run_hybrid(ENDPOINT, body: params, options: options, response_class: RESPONSE_CLASS)
        end

        def subscribe(options: nil, **params)
          params = compact_params(params)
          subscribe_hybrid(ENDPOINT, body: params, options: options, response_class: RESPONSE_CLASS)
        end
      end
    end
  end
end
