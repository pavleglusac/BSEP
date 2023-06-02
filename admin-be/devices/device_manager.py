from fastapi import FastAPI
from fastapi_utils.tasks import repeat_every
from lamp import Lamp


app = FastAPI()

devices = {}


@app.get("/")
def read_root():
    # list all devices and their states
    return devices


@app.get("/start_device")
def start_device(type: str = "", id: str = ""):
    if type == "lamp":
        devices[id] = Lamp(id, id)
        devices[id].pick_a_state()
        return {"status": "ok"}
    else:
        return {"status": "error", "message": "Unknown device type"}


@app.get("/stop_device")
def stop_device(id: str = ""):
    if id in devices:
        del devices[id]
        return {"status": "ok"}
    else:
        return {"status": "error", "message": "Unknown device"}


@app.on_event("startup")
@repeat_every(seconds=3)
def run_states():
    for device in devices.values():
        device.run_state()


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=5000)