"""Midjourney first-video extension resource."""

from typing import Any, Optional

from runapi.core import RequestOptions, Resource

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
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
