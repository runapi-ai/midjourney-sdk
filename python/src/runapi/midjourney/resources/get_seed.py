"""Midjourney get-seed resource."""

from typing import Any, Optional

from runapi.core import RequestOptions, Resource

from ..contract_gen import CONTRACT
from ..types import GetSeedResponse


class GetSeed(Resource):
    ENDPOINT = "/api/v1/midjourney/get_seed"
    RESPONSE_CLASS = GetSeedResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["get-seed"], compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)
