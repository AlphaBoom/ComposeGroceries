import logging
from fastapi import FastAPI, Request
from fastapi.templating import Jinja2Templates
from .routers import chat
import os

app = FastAPI()
app.include_router(chat.router)

templates = Jinja2Templates(directory="api/templates")


@app.on_event("startup")
async def startup_event():
    logging.info("on startup")

@app.get("/alive")
async def alive():
    return os.getpid()


@app.get("/")
async def get(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})
