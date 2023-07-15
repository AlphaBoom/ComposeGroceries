from uuid import UUID
from fastapi import APIRouter, WebSocket, WebSocketDisconnect, Request
from typing import Any, Coroutine, Dict, List, Optional
from langchain.schema import LLMResult
from langchain.vectorstores import VectorStore
from core.callback import QuestionGenCallbackHandler, StreamingLLMCallbackHandler
from core.query_data import get_chain
from core.schemas import ChatResponse
from langchain.chat_models import ChatOpenAI
from langchain.chains import LLMChain
from langchain.prompts import PromptTemplate
from langchain.callbacks.base import AsyncCallbackHandler
from sse_starlette.sse import EventSourceResponse
import asyncio
import logging

vectorstore: Optional[VectorStore] = None

router = APIRouter(
    prefix="/chat"
)

class StreamingCallbackHandler(AsyncCallbackHandler):

    def __init__(self) -> None:
        super().__init__()
        self.tokens = asyncio.Queue()

    async def on_llm_new_token(self, token: str, **kwargs: Any) -> Coroutine[Any, Any, None]:
        await self.tokens.put(token)

    async def on_llm_end(self, response: LLMResult, *, run_id: UUID, parent_run_id: UUID | None = None, **kwargs: Any) -> Coroutine[Any, Any, None]:
        await self.tokens.put(StopIteration)
        

@router.get("/{message}")
async def message_stream(message:str, request:Request):
    callback = StreamingCallbackHandler()
    llm = ChatOpenAI(
        temperature=0,
        streaming=True,
    )
    prompt = PromptTemplate.from_template("Human:{message}")
    chain = LLMChain(
        llm=llm,
        prompt=prompt,
        verbose=True
    )
    loop = asyncio.get_event_loop()
    asyncio.run_coroutine_threadsafe(chain.aapply([{"message":message}],callbacks=[callback]), loop)
    async def event_generator():
        while True:
            if await request.is_disconnected():
                break
            token = await callback.tokens.get()
            if token is StopIteration:
                break
            yield token
    return EventSourceResponse(event_generator())

@router.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    question_handler = QuestionGenCallbackHandler(websocket)
    stream_handler = StreamingLLMCallbackHandler(websocket)
    chat_history = []
    # qa_chain = get_chain(vectorstore, question_handler, stream_handler)
    # Use the below line instead of the above line to enable tracing
    # Ensure `langchain-server` is running
    qa_chain = get_chain(vectorstore, question_handler, stream_handler, tracing=True)

    while True:
        try:
            # Receive and send back the client message
            question = await websocket.receive_text()
            logging.info(f"question:{question}")
            resp = ChatResponse(sender="you", message=question, type="stream")
            await websocket.send_json(resp.dict())

            # Construct a response
            start_resp = ChatResponse(sender="bot", message="", type="start")
            await websocket.send_json(start_resp.dict())

            result = await qa_chain.acall(
                {"question": question, "chat_history": chat_history}
            )
            logging.info(result)
            chat_history.append((question, result["text"]))

            end_resp = ChatResponse(sender="bot", message="", type="end")
            await websocket.send_json(end_resp.dict())
        except WebSocketDisconnect:
            logging.info("websocket disconnect")
            break
        except Exception as e:
            logging.error(e)
            resp = ChatResponse(
                sender="bot",
                message="Sorry, something went wrong. Try again.",
                type="error",
            )
            await websocket.send_json(resp.dict())