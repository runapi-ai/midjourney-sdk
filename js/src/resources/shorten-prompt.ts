import type { ActionSchema, HttpClient, HybridTaskListener, HybridTaskOptions } from '@runapi.ai/core';
import { compactParams, createHybridTask, validateParams } from '@runapi.ai/core';
import { contract } from '../contract_gen';
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
    validateParams(contract['shorten-prompt'] as ActionSchema, body as Record<string, unknown>);
    return createHybridTask<MidjourneyShortenPromptResponse>(this.http, ENDPOINT, { body, ...options });
  }
}
