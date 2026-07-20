# frozen_string_literal: true

module RunApi
  module Midjourney
    module Types
      # A generated media asset.
      class MediaUrl < RunApi::Core::BaseModel
        optional :url, String
      end

      # Result of an image generation or editing task.
      class ImageTaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [-> { MediaUrl }]
        optional :image_id, String
        optional :actions, [String]
        optional :progress, Integer
        optional :error, String
      end

      # Narrowed image task response returned after polling completes.
      class CompletedImageTaskResponse < ImageTaskResponse
        required :images, [-> { MediaUrl }]
      end

      # Result of an image-to-video task.
      class VideoTaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [-> { MediaUrl }]
        optional :video_id, String
        optional :progress, Integer
        optional :error, String
      end

      # Narrowed video task response returned after polling completes.
      class CompletedVideoTaskResponse < VideoTaskResponse
        required :videos, [-> { MediaUrl }]
      end

      # Prompt suggestions derived from an image.
      class ImageToPromptResponse < RunApi::Core::BaseModel
        required :prompts, [String]
        optional :error, String
      end

      # Concise suggestions derived from a prompt.
      class ShortenPromptResponse < RunApi::Core::BaseModel
        required :prompts, [String]
        optional :error, String
      end

      # Seed associated with a generated image.
      class GetSeedResponse < RunApi::Core::BaseModel
        optional :seed, Integer
        optional :error, String
      end
    end
  end
end
