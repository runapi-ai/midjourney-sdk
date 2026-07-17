import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { EditImage } from '../../src/resources/edit-image';
import { GetSeed } from '../../src/resources/get-seed';
import { ImageToPrompt } from '../../src/resources/image-to-prompt';
import { ImageToVideo } from '../../src/resources/image-to-video';
import { TextToImage } from '../../src/resources/text-to-image';

describe('Midjourney resources', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => vi.clearAllMocks());

  it('creates and gets a text-to-image task', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1', status: 'processing' });
    const resource = new TextToImage(mockHttp);

    await resource.create({
      model: 'midjourney-v8.1',
      prompt: 'A cobalt vase on white marble',
      enable_prompt_translation: true,
      include_split_images: false,
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/midjourney/text_to_image', {
      body: {
        model: 'midjourney-v8.1',
        prompt: 'A cobalt vase on white marble',
        enable_prompt_translation: true,
        include_split_images: false,
      },
    });

    await resource.get('task-1');
    expect(mockHttp.request).toHaveBeenLastCalledWith('GET', '/api/v1/midjourney/text_to_image/task-1', {});
  });

  it('creates image editing and image-to-video tasks with public fields', async () => {
    vi.mocked(mockHttp.request).mockResolvedValue({ id: 'task-2', status: 'processing' });

    await new EditImage(mockHttp).create({
      model: 'midjourney-edit-image',
      prompt: 'Change the vase color to cobalt blue',
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
      mask_url: 'https://cdn.runapi.ai/public/samples/mask.png',
    });
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/edit_image', {
      body: {
        model: 'midjourney-edit-image',
        prompt: 'Change the vase color to cobalt blue',
        source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
        mask_url: 'https://cdn.runapi.ai/public/samples/mask.png',
      },
    });

    await new ImageToVideo(mockHttp).create({
      model: 'midjourney-image-to-video',
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
      output_resolution: '480p',
      enable_loop: false,
    });
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/image_to_video', {
      body: {
        model: 'midjourney-image-to-video',
        source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
        output_resolution: '480p',
        enable_loop: false,
      },
    });
  });

  it('runs synchronous image helpers and returns decoded results', async () => {
    vi.mocked(mockHttp.request)
      .mockResolvedValueOnce({ prompts: ['one', 'two', 'three', 'four'] })
      .mockResolvedValueOnce({ seed: 8675309 });

    const prompts = await new ImageToPrompt(mockHttp).run({
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
    });
    expect(prompts.prompts).toEqual(['one', 'two', 'three', 'four']);
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/image_to_prompt', {
      body: { source_image_url: 'https://cdn.runapi.ai/public/samples/source.png' },
    });

    const seed = await new GetSeed(mockHttp).run({ image_id: 'image-1' });
    expect(seed.seed).toBe(8675309);
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/get_seed', {
      body: { image_id: 'image-1' },
    });
  });
});
