# frozen_string_literal: true

require "runapi/core"
require_relative "midjourney/types"
require_relative "midjourney/contract_gen"
require_relative "midjourney/resources/text_to_image"
require_relative "midjourney/resources/edit_image"
require_relative "midjourney/resources/image_to_video"
require_relative "midjourney/resources/image_to_prompt"
require_relative "midjourney/resources/shorten_prompt"
require_relative "midjourney/resources/get_seed"
require_relative "midjourney/client"

module RunApi
  module Midjourney
    AuthenticationError = RunApi::Core::AuthenticationError
    RateLimitError = RunApi::Core::RateLimitError
    InsufficientCreditsError = RunApi::Core::InsufficientCreditsError
    NotFoundError = RunApi::Core::NotFoundError
    ValidationError = RunApi::Core::ValidationError
    TaskFailedError = RunApi::Core::TaskFailedError
    TaskTimeoutError = RunApi::Core::TaskTimeoutError
  end
end
