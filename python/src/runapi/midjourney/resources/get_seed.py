"""Midjourney get-seed resource."""

from typing import Any, Optional

from runapi.core import RequestOptions, Resource

from ..types import GetSeedResponse


class GetSeed(Resource):
    ENDPOINT = "/api/v1/midjourney/get_seed"
    RESPONSE_CLASS = GetSeedResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        return self._run_hybrid("post", self.ENDPOINT, body=compacted, options=options)
