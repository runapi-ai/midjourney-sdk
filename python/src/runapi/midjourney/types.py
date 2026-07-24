"""Midjourney response models."""

from runapi.core import BaseModel, TaskResponse, optional, required


class MediaUrl(BaseModel):
    url = optional(str)


class ImageTaskResponse(TaskResponse):
    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    images = optional([lambda: MediaUrl])
    image_id = optional(str)
    actions = optional([str])
    progress = optional(int)
    error = optional(str)


class CompletedImageTaskResponse(ImageTaskResponse):
    images = required([lambda: MediaUrl])


class VideoTaskResponse(TaskResponse):
    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    videos = optional([lambda: MediaUrl])
    video_id = optional(str)
    progress = optional(int)
    error = optional(str)


class CompletedVideoTaskResponse(VideoTaskResponse):
    videos = required([lambda: MediaUrl])


class ImageToPromptResponse(TaskResponse):
    prompts = required([str])
    error = optional(str)


class ShortenPromptResponse(TaskResponse):
    prompts = required([str])
    error = optional(str)


class GetSeedResponse(TaskResponse):
    seed = optional(int)
    error = optional(str)
