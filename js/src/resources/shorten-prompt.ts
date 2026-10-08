import type { HttpClient, HybridTaskListener, HybridTaskOptions } from '@runapi.ai/core';
import { compactParams, createHybridTask } from '@runapi.ai/core';
import type { MidjourneyShortenPromptParams, MidjourneyShortenPromptResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/shorten_prompt';

export class ShortenPrompt {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyShortenPromptParams, options?: HybridTaskOptions): Promise<MidjourneyShortenPromptResponse> {
    return (await this.create(params, options)).run();
  }

  async subscribe(
    params: MidjourneyShortenPromptParams,
    listener: HybridTaskListener,
    options?: HybridTaskOptions,
  ): Promise<MidjourneyShortenPromptResponse> {
    return (await this.create(params, options)).subscribe(listener);
  }

  private async create(params: MidjourneyShortenPromptParams, options?: HybridTaskOptions) {
    const body = compactParams(params);
    return createHybridTask<MidjourneyShortenPromptResponse>(this.http, ENDPOINT, { body, ...options });
  }
}
