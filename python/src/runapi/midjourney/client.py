"""Midjourney client."""

from typing import Any, Optional

from runapi.core import ClientOptions, HttpClient, resolve_api_key

from .resources.edit_image import EditImage
from .resources.get_seed import GetSeed
from .resources.image_to_prompt import ImageToPrompt
from .resources.image_to_video import ImageToVideo
from .resources.text_to_image import TextToImage


class MidjourneyClient:
    """Midjourney image generation, editing, image-to-video, and helper client."""

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        resolved_api_key = resolve_api_key(api_key)
        client_options = ClientOptions(api_key=resolved_api_key, **options)
        http = client_options.http_client or HttpClient(client_options)
        self.text_to_image = TextToImage(http)
        self.edit_image = EditImage(http)
        self.image_to_video = ImageToVideo(http)
        self.image_to_prompt = ImageToPrompt(http)
        self.get_seed = GetSeed(http)
