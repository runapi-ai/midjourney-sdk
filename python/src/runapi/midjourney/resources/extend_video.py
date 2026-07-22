"""Midjourney first-video extension resource."""

from typing import Any, Dict, Optional

from runapi.core import RequestOptions, Resource

from ..contract_gen import CONTRACT
from ..types import CompletedVideoTaskResponse, VideoTaskResponse


class ExtendVideo(Resource):
    """Extend the first video from a completed Midjourney image-to-video task."""

    ENDPOINT = "/api/v1/midjourney/extend_video"
    MODEL = "midjourney-image-to-video"
    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)

    def _validate_params(self, params: Dict[str, Any]) -> None:
        self._validate_contract(CONTRACT["extend-video"], {**params, "model": self.MODEL})
