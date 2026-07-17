import type { ActionSchema, HttpClient, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { contract } from '../contract_gen';
import type { MidjourneyImageToPromptParams, MidjourneyImageToPromptResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/image_to_prompt';

export class ImageToPrompt {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyImageToPromptParams, options?: RequestOptions): Promise<MidjourneyImageToPromptResponse> {
    const body = compactParams(params);
    validateParams(contract['image-to-prompt'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<MidjourneyImageToPromptResponse>('POST', ENDPOINT, { body, ...options });
  }
}
