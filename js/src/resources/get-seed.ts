import type { ActionSchema, HttpClient, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { contract } from '../contract_gen';
import type { MidjourneyGetSeedParams, MidjourneyGetSeedResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/get_seed';

export class GetSeed {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyGetSeedParams, options?: RequestOptions): Promise<MidjourneyGetSeedResponse> {
    const body = compactParams(params);
    validateParams(contract['get-seed'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<MidjourneyGetSeedResponse>('POST', ENDPOINT, { body, ...options });
  }
}
