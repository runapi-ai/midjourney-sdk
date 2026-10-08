import type { HttpClient, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import type { MidjourneyImageToPromptParams, MidjourneyImageToPromptResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/image_to_prompt';

export class ImageToPrompt {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyImageToPromptParams, options?: RequestOptions): Promise<MidjourneyImageToPromptResponse> {
    const body = compactParams(params);
    return this.http.request<MidjourneyImageToPromptResponse>('POST', ENDPOINT, { body, ...options });
  }
}
