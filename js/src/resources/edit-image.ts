import type { ActionSchema, HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { CompletedMidjourneyImageTaskResponse, MidjourneyEditImageParams, MidjourneyImageTaskResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/midjourney/edit_image';

export class EditImage {
  constructor(private readonly http: HttpClient) {}

  async run(params: MidjourneyEditImageParams, options?: RequestOptions & PollingOptions): Promise<CompletedMidjourneyImageTaskResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<MidjourneyImageTaskResponse>(() => this.get(id, options), options) as Promise<CompletedMidjourneyImageTaskResponse>;
  }

  async create(params: MidjourneyEditImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['edit-image'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body, ...options });
  }

  async get(id: string, options?: RequestOptions): Promise<MidjourneyImageTaskResponse> {
    return this.http.request<MidjourneyImageTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
