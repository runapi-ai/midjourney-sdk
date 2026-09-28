import { beforeEach, describe, expect, it, vi } from 'vitest';
import { createHttpClient, type HttpClient } from '@runapi.ai/core';
import { EditImage } from '../../src/resources/edit-image';
import { ExtendVideo } from '../../src/resources/extend-video';
import { GetSeed } from '../../src/resources/get-seed';
import { ImageToPrompt } from '../../src/resources/image-to-prompt';
import { ImageToVideo } from '../../src/resources/image-to-video';
import { ShortenPrompt } from '../../src/resources/shorten-prompt';
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

  it('creates and gets a first-video extension without sending fixed provider fields', async () => {
    vi.mocked(mockHttp.request).mockResolvedValue({ id: 'task-extend', status: 'processing' });
    const resource = new ExtendVideo(mockHttp);

    await resource.create({
      source_task_id: 'task-source',
      prompt: 'Continue the camera orbit',
      callback_url: 'https://example.test/callback',
    });
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/extend_video', {
      body: {
        source_task_id: 'task-source',
        prompt: 'Continue the camera orbit',
        callback_url: 'https://example.test/callback',
      },
    });

    await resource.get('task-extend');
    expect(mockHttp.request).toHaveBeenLastCalledWith('GET', '/api/v1/midjourney/extend_video/task-extend', {});
  });

  it('runs synchronous image helpers and returns decoded results', async () => {
    vi.mocked(mockHttp.request)
      .mockResolvedValueOnce({
        prompts: ['one', 'two', 'three', 'four'],
      })
      .mockResolvedValueOnce({
        seed: 8675309,
      });

    const prompts = await new ImageToPrompt(mockHttp).run({
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
    });
    expect(prompts.prompts).toEqual(['one', 'two', 'three', 'four']);
    expect(prompts).not.toHaveProperty('billing');
    expect(prompts).not.toHaveProperty('usage');
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/image_to_prompt', {
      body: { source_image_url: 'https://cdn.runapi.ai/public/samples/source.png' },
    });

    const seed = await new GetSeed(mockHttp).run({ image_id: 'image-1' });
    expect(seed.seed).toBe(8675309);
    expect(seed).not.toHaveProperty('billing');
    expect(seed).not.toHaveProperty('usage');
    expect(mockHttp.request).toHaveBeenLastCalledWith('POST', '/api/v1/midjourney/get_seed', {
      body: { image_id: 'image-1' },
    });
  });

  it('shortens a prompt and returns decoded suggestions', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({
      prompts: ['Concise mountain landscape'],
    });

    const result = await new ShortenPrompt(mockHttp).run({
      prompt: 'A detailed cinematic mountain landscape',
    });

    expect(result.prompts).toEqual(['Concise mountain landscape']);
    expect(result).not.toHaveProperty('billing');
    expect(result).not.toHaveProperty('usage');
    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/midjourney/shorten_prompt', expect.objectContaining({
      body: { prompt: 'A detailed cinematic mountain landscape' },
    }));
  });

  it('runs a hybrid prompt task through its opaque Task Result location', async () => {
    const fetchMock = vi.fn()
      .mockResolvedValueOnce(new Response(JSON.stringify({
        id: 'task-shorten-1',
        status: 'processing',
      }), {
        status: 202,
        headers: {
          'content-type': 'application/json',
          location: '/api/v1/tasks/task-shorten-1',
          'retry-after': '0',
        },
      }))
      .mockResolvedValueOnce(new Response(JSON.stringify({
        id: 'task-shorten-1',
        status: 'processing',
      }), {
        headers: {
          'content-type': 'application/json',
          'retry-after': '0',
        },
      }))
      .mockResolvedValueOnce(new Response(JSON.stringify({
        id: 'task-shorten-1',
        status: 'completed',
        response: {
          status: 200,
          content_type: 'application/json',
          headers: {},
          body: {
            prompts: ['Concise mountain landscape'],
          },
        },
      }), {
        headers: { 'content-type': 'application/json' },
      }));
    const resource = new ShortenPrompt(createHttpClient({
      apiKey: 'test-key',
      fetch: fetchMock as typeof fetch,
      maxRetries: 0,
    }));
    const updates: string[] = [];

    const result = await resource.subscribe(
      { prompt: 'A detailed cinematic mountain landscape' },
      (task) => updates.push(task.status),
    );

    expect(result.prompts).toEqual(['Concise mountain landscape']);
    expect(updates).toEqual(['processing', 'completed']);
    expect(fetchMock).toHaveBeenNthCalledWith(1, expect.stringContaining('/api/v1/midjourney/shorten_prompt'), expect.objectContaining({
      method: 'POST',
      headers: expect.objectContaining({ 'Idempotency-Key': expect.any(String) }),
    }));
    expect(fetchMock.mock.calls.slice(1).map(([url]) => url)).toEqual([
      'https://runapi.ai/api/v1/tasks/task-shorten-1',
      'https://runapi.ai/api/v1/tasks/task-shorten-1',
    ]);
  });

  it('returns an immediate terminal prompt response without querying Task Result', async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({
      prompts: ['Concise mountain landscape'],
    }), { headers: { 'content-type': 'application/json' } }));
    const resource = new ShortenPrompt(createHttpClient({
      apiKey: 'test-key',
      fetch: fetchMock as typeof fetch,
      maxRetries: 0,
    }));

    await expect(resource.run({ prompt: 'A detailed cinematic mountain landscape' })).resolves.toMatchObject({
      prompts: ['Concise mountain landscape'],
    });
    expect(fetchMock).toHaveBeenCalledOnce();
  });
});
