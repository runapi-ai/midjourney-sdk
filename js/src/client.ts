import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { EditImage } from './resources/edit-image';
import { ExtendVideo } from './resources/extend-video';
import { GetSeed } from './resources/get-seed';
import { ImageToPrompt } from './resources/image-to-prompt';
import { ImageToVideo } from './resources/image-to-video';
import { ShortenPrompt } from './resources/shorten-prompt';
import { TextToImage } from './resources/text-to-image';

/** Midjourney image generation, editing, image-to-video, and helper API client. */
export class MidjourneyClient extends BaseClient {
  public readonly textToImage: TextToImage;
  public readonly editImage: EditImage;
  public readonly extendVideo: ExtendVideo;
  public readonly imageToVideo: ImageToVideo;
  public readonly imageToPrompt: ImageToPrompt;
  public readonly shortenPrompt: ShortenPrompt;
  public readonly getSeed: GetSeed;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.textToImage = new TextToImage(this.http);
    this.editImage = new EditImage(this.http);
    this.extendVideo = new ExtendVideo(this.http);
    this.imageToVideo = new ImageToVideo(this.http);
    this.imageToPrompt = new ImageToPrompt(this.http);
    this.shortenPrompt = new ShortenPrompt(this.http);
    this.getSeed = new GetSeed(this.http);
  }
}
