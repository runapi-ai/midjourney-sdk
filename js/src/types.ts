import type { AsyncTaskStatus, TaskResponse } from '@runapi.ai/core';

export type MidjourneyTextToImageModel = 'midjourney-v8.1';
export type MidjourneyEditImageModel = 'midjourney-edit-image';
export type MidjourneyImageToVideoModel = 'midjourney-image-to-video';
export type MidjourneyOutputResolution = '480p';

interface AsyncTaskParams {
  /** URL for completion callback notifications. */
  callback_url?: string;
}

export interface MidjourneyTextToImageParams extends AsyncTaskParams {
  model: MidjourneyTextToImageModel;
  /** Text description of the image to generate. */
  prompt: string;
  /** Translate the prompt before generation. Default: false. */
  enable_prompt_translation?: boolean;
  /** Return split image outputs when available. Default: false. */
  include_split_images?: boolean;
}

export interface MidjourneyEditImageParams extends AsyncTaskParams {
  model: MidjourneyEditImageModel;
  /** Instructions describing the desired edit. */
  prompt: string;
  /** Source image URL. */
  source_image_url: string;
  /** Optional mask image URL. */
  mask_url?: string;
  /** Return split image outputs when available. Default: false. */
  include_split_images?: boolean;
}

export interface MidjourneyImageToVideoParams extends AsyncTaskParams {
  model: MidjourneyImageToVideoModel;
  /** Optional description of the desired motion. */
  prompt?: string;
  /** Source image URL. */
  source_image_url: string;
  /** Optional image URL for the final video frame. */
  last_frame_image_url?: string;
  /** Output video resolution. Default: 480p. */
  output_resolution?: MidjourneyOutputResolution;
  /** Make the generated video loop. Default: false. */
  enable_loop?: boolean;
}

export interface MidjourneyExtendVideoParams extends AsyncTaskParams {
  /** Task ID of a completed Midjourney image-to-video task in the current account. */
  source_task_id: string;
  /** Optional description of the continuation. */
  prompt?: string;
}

export interface MidjourneyImageToPromptParams {
  /** Source image URL to describe. */
  source_image_url: string;
}

export interface MidjourneyShortenPromptParams {
  /** Prompt to shorten into concise suggestions. */
  prompt: string;
}

export interface MidjourneyGetSeedParams {
  /** Image ID from a completed Midjourney image task in the current account. */
  image_id: string;
}

export interface TaskCreateResponse {
  id: string;
  status?: AsyncTaskStatus;
}

export interface MediaUrl {
  url: string;
}

export interface MidjourneyImageTaskResponse extends TaskResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: MediaUrl[];
  image_id?: string;
  actions?: string[];
  progress?: number;
  error?: string;
  [key: string]: unknown;
}

export type CompletedMidjourneyImageTaskResponse = MidjourneyImageTaskResponse & {
  status: 'completed';
  images: MediaUrl[];
};

export interface MidjourneyVideoTaskResponse extends TaskResponse {
  id: string;
  status: AsyncTaskStatus;
  videos?: MediaUrl[];
  video_id?: string;
  progress?: number;
  error?: string;
  [key: string]: unknown;
}

export type CompletedMidjourneyVideoTaskResponse = MidjourneyVideoTaskResponse & {
  status: 'completed';
  videos: MediaUrl[];
};

export interface MidjourneyImageToPromptResponse {
  prompts: string[];
  error?: string;
}

export interface MidjourneyShortenPromptResponse {
  prompts: string[];
  error?: string;
}

export interface MidjourneyGetSeedResponse {
  seed: number;
  error?: string;
}
