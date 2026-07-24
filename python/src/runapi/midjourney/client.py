"""Midjourney client."""

from typing import Any, Optional

from runapi.core import ProviderClient

from .resources.edit_image import EditImage
from .resources.extend_video import ExtendVideo
from .resources.get_seed import GetSeed
from .resources.image_to_prompt import ImageToPrompt
from .resources.image_to_video import ImageToVideo
from .resources.shorten_prompt import ShortenPrompt
from .resources.text_to_image import TextToImage


class MidjourneyClient(ProviderClient):
    """Midjourney image generation, editing, image-to-video, and helper client."""

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        super().__init__(api_key, **options)
        http = self._http
        self.text_to_image = TextToImage(http)
        self.edit_image = EditImage(http)
        self.extend_video = ExtendVideo(http)
        self.image_to_video = ImageToVideo(http)
        self.image_to_prompt = ImageToPrompt(http)
        self.shorten_prompt = ShortenPrompt(http)
        self.get_seed = GetSeed(http)
