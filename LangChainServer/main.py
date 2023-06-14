import uvicorn

if __name__  == "__main__":
    log_config = uvicorn.config.LOGGING_CONFIG
    log_config["root"] = log_config["loggers"]["uvicorn"]
    uvicorn.run("api.main:app", host="localhost", port=9000, reload=True, env_file=".env")