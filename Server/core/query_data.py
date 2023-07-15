"""Create a ChatVectorDBChain for question/answering."""
from __future__ import annotations

from langchain.callbacks.base import AsyncCallbackHandler
from langchain.callbacks.manager import AsyncCallbackManager

# from langchain.callbacks.tracers.langchain import LangChainTracer
from langchain.callbacks.tracers.langchain_v1 import LangChainTracerV1
from langchain.chains import ConversationalRetrievalChain
from langchain.chains.chat_vector_db.prompts import CONDENSE_QUESTION_PROMPT, QA_PROMPT
from langchain.chains.llm import LLMChain
from langchain.chains.question_answering import load_qa_chain
from langchain.chat_models import ChatOpenAI
from langchain.vectorstores.base import VectorStore
from langchain.prompts.prompt import PromptTemplate


def get_chain(
    vectorstore: VectorStore,
    question_handler: AsyncCallbackHandler,
    stream_handler: AsyncCallbackHandler,
    tracing: bool = False,
) -> LLMChain:
    """Create a ConversationalRetrievalChain for question/answering."""
    # Construct a ConversationalRetrievalChain with a streaming llm for
    # combine docs and a separate, non-streaming llm for question generation
    manager = AsyncCallbackManager([])
    question_manager = AsyncCallbackManager([question_handler])
    stream_manager = AsyncCallbackManager([stream_handler])
    if tracing:
        # tracer = LangChainTracer()
        tracer = LangChainTracerV1()
        tracer.load_default_session()
        manager.add_handler(tracer)
        question_manager.add_handler(tracer)
        stream_manager.add_handler(tracer)

    question_gen_llm = ChatOpenAI(
        temperature=0,
        verbose=True,
        callbacks=[question_handler],
    )  # type: ignore

    streaming_llm = ChatOpenAI(
        streaming=True,
        callbacks=[stream_handler],
        verbose=True,
        temperature=0,
    )  # type: ignore

    question_generator = LLMChain(
        llm=question_gen_llm,
        prompt=CONDENSE_QUESTION_PROMPT,
        verbose=True,
        callbacks=[stream_handler],
    )

    return LLMChain(
        llm=streaming_llm,
        prompt=PromptTemplate.from_template("""Assistant is a large language model trained by OpenAI.

Assistant is designed to be able to assist with a wide range of tasks, from answering simple questions to providing in-depth explanations and discussions on a wide range of topics. As a language model, Assistant is able to generate human-like text based on the input it receives, allowing it to engage in natural-sounding conversations and provide responses that are coherent and relevant to the topic at hand.

Assistant is constantly learning and improving, and its capabilities are constantly evolving. It is able to process and understand large amounts of text, and can use this knowledge to provide accurate and informative responses to a wide range of questions. Additionally, Assistant is able to generate its own text based on the input it receives, allowing it to engage in discussions and provide explanations and descriptions on a wide range of topics.

Overall, Assistant is a powerful tool that can help with a wide range of tasks and provide valuable insights and information on a wide range of topics. Whether you need help with a specific question or just want to have a conversation about a particular topic, Assistant is here to assist.

{chat_history}
Human: {question}
Assistant:"""),
        verbose=True,
    )
