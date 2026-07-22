# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-midjourney"
    spec.version = "0.3.0"
    spec.metadata["runapi_slug"] = "midjourney"
    spec.authors = ["RunAPI"]
    spec.email = ["contact@runapi.ai"]

    spec.summary = "Midjourney Ruby SDK for RunAPI"
    spec.description = "Use `runapi-midjourney` for Midjourney image generation, editing, image-to-video, first-video extension, image-to-prompt, prompt shortening, and seed lookup in Ruby applications and workers."
    spec.homepage = "https://runapi.ai/models/midjourney"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"
    spec.metadata["homepage_uri"] = "https://runapi.ai/models/midjourney"
    spec.metadata["documentation_uri"] = "https://github.com/runapi-ai/midjourney-sdk/blob/main/ruby/README.md"
    spec.metadata["source_code_uri"] = "https://github.com/runapi-ai/midjourney-sdk"
    spec.metadata["bug_tracker_uri"] = "https://github.com/runapi-ai/midjourney-sdk/issues"
    spec.metadata["changelog_uri"] = "https://github.com/runapi-ai/midjourney-sdk/blob/main/CHANGELOG.md"


    spec.files = Dir.glob("lib/**/*") + %w[LICENSE README.md]
    spec.extra_rdoc_files = ["README.md"]
        spec.require_paths = ["lib"]

    spec.add_dependency "runapi-core", "~> 0.2.14"
  end
end
