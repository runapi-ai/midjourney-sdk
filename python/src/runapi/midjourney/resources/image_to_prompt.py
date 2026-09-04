"""Midjourney image-to-prompt resource."""

from typing import Any, Dict, Optional

from runapi.core import RequestOptions, Resource

from ..contract_gen import CONTRACT
from ..types import ImageToPromptResponse


class ImageToPrompt(Resource):
    ENDPOINT = "/api/v1/midjourney/image_to_prompt"
    RESPONSE_CLASS = ImageToPromptResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["image-to-prompt"], compacted)
        return self._run_hybrid("post", self.ENDPOINT, body=compacted, options=options)
