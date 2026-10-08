import type { HttpClient, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import type { MidjourneyGetSeedParams, MidjourneyGetSeedResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/get_seed';

export class GetSeed {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyGetSeedParams, options?: RequestOptions): Promise<MidjourneyGetSeedResponse> {
    const body = compactParams(params);
    return this.http.request<MidjourneyGetSeedResponse>('POST', ENDPOINT, { body, ...options });
  }
}
