import type { ActionSchema, HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { CompletedMidjourneyVideoTaskResponse, MidjourneyExtendVideoParams, MidjourneyVideoTaskResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/extend_video';
const MODEL = 'midjourney-image-to-video';

/** Extend the first video from a completed Midjourney image-to-video task. */
export class ExtendVideo {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyExtendVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedMidjourneyVideoTaskResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<MidjourneyVideoTaskResponse>(() => this.get(id, options), options) as Promise<CompletedMidjourneyVideoTaskResponse>;
  }

  async create(params: MidjourneyExtendVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['extend-video'] as ActionSchema, { ...body, model: MODEL } as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body, ...options });
  }

  async get(id: string, options?: RequestOptions): Promise<MidjourneyVideoTaskResponse> {
    return this.http.request<MidjourneyVideoTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
