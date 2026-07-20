import type { ActionSchema, HttpClient, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { contract } from '../contract_gen';
import type { MidjourneyShortenPromptParams, MidjourneyShortenPromptResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/shorten_prompt';

export class ShortenPrompt {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyShortenPromptParams, options?: RequestOptions): Promise<MidjourneyShortenPromptResponse> {
    const body = compactParams(params);
    validateParams(contract['shorten-prompt'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<MidjourneyShortenPromptResponse>('POST', ENDPOINT, { body, ...options });
  }
}
