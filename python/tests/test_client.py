import pytest

from runapi.core import ApiResponse, config
from runapi.core.errors import AuthenticationError
from runapi.midjourney import MidjourneyClient
from runapi.midjourney.resources import EditImage, ExtendVideo, GetSeed, ImageToPrompt, ImageToVideo, ShortenPrompt, TextToImage
from runapi.midjourney.types import GetSeedResponse, ImageToPromptResponse, ShortenPromptResponse


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []
        self.options = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        self.options.append(options)
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "processing"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


def test_requires_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        MidjourneyClient()


def test_exposes_resources_with_injected_http():
    fake = FakeHttp()
    client = MidjourneyClient(api_key="k", http_client=fake)

    assert isinstance(client.text_to_image, TextToImage)
    assert isinstance(client.edit_image, EditImage)
    assert isinstance(client.image_to_video, ImageToVideo)
    assert isinstance(client.extend_video, ExtendVideo)
    assert isinstance(client.image_to_prompt, ImageToPrompt)
    assert isinstance(client.shorten_prompt, ShortenPrompt)
    assert isinstance(client.get_seed, GetSeed)
    assert client.text_to_image._http is fake


def test_async_resources_use_public_paths_and_flat_bodies():
    fake = FakeHttp()
    client = MidjourneyClient(api_key="k", http_client=fake)

    client.text_to_image.create(
        model="midjourney-v8.1",
        prompt="A cobalt vase",
        include_split_images=False,
    )
    client.text_to_image.get("task_1")
    client.edit_image.create(
        model="midjourney-edit-image",
        prompt="Change the vase color",
        source_image_url="https://cdn.runapi.ai/public/samples/source.png",
        mask_url="https://cdn.runapi.ai/public/samples/mask.png",
    )
    client.image_to_video.create(
        model="midjourney-image-to-video",
        source_image_url="https://cdn.runapi.ai/public/samples/source.png",
        output_resolution="480p",
        enable_loop=False,
    )
    client.extend_video.create(
        source_task_id="task_source",
        prompt="Continue the camera orbit",
        callback_url="https://example.test/callback",
    )

    assert fake.calls == [
        (
            "post",
            "/api/v1/midjourney/text_to_image",
            {"model": "midjourney-v8.1", "prompt": "A cobalt vase", "include_split_images": False},
        ),
        ("get", "/api/v1/midjourney/text_to_image/task_1", None),
        (
            "post",
            "/api/v1/midjourney/edit_image",
            {
                "model": "midjourney-edit-image",
                "prompt": "Change the vase color",
                "source_image_url": "https://cdn.runapi.ai/public/samples/source.png",
                "mask_url": "https://cdn.runapi.ai/public/samples/mask.png"},
        ),
        (
            "post",
            "/api/v1/midjourney/image_to_video",
            {
                "model": "midjourney-image-to-video",
                "source_image_url": "https://cdn.runapi.ai/public/samples/source.png",
                "output_resolution": "480p",
                "enable_loop": False},
        ),
        (
            "post",
            "/api/v1/midjourney/extend_video",
            {
                "source_task_id": "task_source",
                "prompt": "Continue the camera orbit",
                "callback_url": "https://example.test/callback"},
        )]


def test_hybrid_helpers_decode_direct_responses():
    fake = FakeHttp(
        {"prompts": ["one", "two", "three", "four"]},
        {"prompts": ["Concise mountain landscape"]},
        {"seed": 8675309},
    )
    client = MidjourneyClient(api_key="k", http_client=fake)

    prompts = client.image_to_prompt.run(source_image_url="https://cdn.runapi.ai/public/samples/source.png")
    shortened = client.shorten_prompt.run(prompt="A detailed cinematic mountain landscape")
    seed = client.get_seed.run(image_id="image-1")

    assert isinstance(prompts, ImageToPromptResponse)
    assert prompts.prompts == ["one", "two", "three", "four"]
    assert isinstance(shortened, ShortenPromptResponse)
    assert shortened.prompts == ["Concise mountain landscape"]
    assert fake.calls[1] == (
        "post",
        "/api/v1/midjourney/shorten_prompt",
        {"prompt": "A detailed cinematic mountain landscape"},
    )
    assert isinstance(seed, GetSeedResponse)
    assert seed.seed == 8675309


@pytest.mark.parametrize(
    ("resource_name", "params", "endpoint", "terminal_body", "response_class"),
    [
        (
            "image_to_prompt",
            {"source_image_url": "https://cdn.runapi.ai/public/samples/source.png"},
            "/api/v1/midjourney/image_to_prompt",
            {"prompts": ["one", "two", "three", "four"]},
            ImageToPromptResponse,
        ),
        (
            "shorten_prompt",
            {"prompt": "A detailed cinematic mountain landscape"},
            "/api/v1/midjourney/shorten_prompt",
            {"prompts": ["Concise mountain landscape"]},
            ShortenPromptResponse,
        ),
        (
            "get_seed",
            {"image_id": "image-1"},
            "/api/v1/midjourney/get_seed",
            {"seed": 8675309},
            GetSeedResponse,
        )],
)
def test_hybrid_helpers_follow_accepted_task_result(
    resource_name, params, endpoint, terminal_body, response_class
):
    location = "https://runapi.ai/api/v1/tasks/task_1"
    fake = FakeHttp(
        ApiResponse({"id": "task_1", "status": "processing"}, {"Location": location}, status_code=202),
        ApiResponse(
            {
                "id": "task_1",
                "status": "completed", "usage": {"cost": 0.05},
                "response": {
                    "status": 200,
                    "content_type": "application/json",
                    "headers": {},
                    "body": terminal_body}}
        ),
    )
    client = MidjourneyClient(api_key="k", http_client=fake)

    result = getattr(client, resource_name).run(**params)

    assert isinstance(result, response_class)
    assert [call[:2] for call in fake.calls] == [("post", endpoint), ("get", location)]
    assert fake.options[0].headers["Idempotency-Key"]
    assert fake.options[1].headers == fake.options[0].headers


def test_get_seed_preserves_error_response_without_seed():
    fake = FakeHttp({"error": "Seed lookup failed"})
    client = MidjourneyClient(api_key="k", http_client=fake)

    result = client.get_seed.run(image_id="image-1")

    assert isinstance(result, GetSeedResponse)
    assert result.seed is None
    assert result.error == "Seed lookup failed"
