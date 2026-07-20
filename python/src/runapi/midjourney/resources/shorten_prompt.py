"""Midjourney prompt shortening resource."""

from typing import Any, Optional

from runapi.core import RequestOptions, Resource

from ..contract_gen import CONTRACT
from ..types import ShortenPromptResponse


class ShortenPrompt(Resource):
    ENDPOINT = "/api/v1/midjourney/shorten_prompt"
    RESPONSE_CLASS = ShortenPromptResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["shorten-prompt"], compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)
