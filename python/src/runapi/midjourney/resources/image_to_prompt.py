"""Midjourney image-to-prompt resource."""

from typing import Any, Optional

from runapi.core import RequestOptions, Resource

from ..types import ImageToPromptResponse


class ImageToPrompt(Resource):
    ENDPOINT = "/api/v1/midjourney/image_to_prompt"
    RESPONSE_CLASS = ImageToPromptResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        return self._run_hybrid("post", self.ENDPOINT, body=compacted, options=options)
