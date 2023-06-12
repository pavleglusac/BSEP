from fastapi import FastAPI
from fastapi_utils.tasks import repeat_every
from lamp import Lamp
from gate import Gate
from motion_detector import MotionDetector
from thermometer import Thermometer
from lock import Lock

import logging


logger = logging.getLogger("api")
logger.setLevel(logging.DEBUG)


app = FastAPI()

devices = {}


@app.get("/")
def read_root():
    # list all devices and their states
    return devices


@app.get("/start_device")
def start_device(type: str = "", id: str = ""):
    id = id.replace("%2D", "-")
    if type == "LAMP":
        # url decode id that is uuid4
        devices[id] = Lamp(id, id)
        devices[id].pick_a_state()
        return {"status": "ok"}
    elif type == "GATE":
        devices[id] = Gate(id, id)
        devices[id].pick_a_state()
        return {"status": "ok"}
    elif type == "MOTION_DETECTOR":
        devices[id] = MotionDetector(id, id)
        devices[id].pick_a_state()
        return {"status": "ok"}
    elif type == "THERMOMETER":
        devices[id] = Thermometer(id, id)
        devices[id].pick_a_state()
        return {"status": "ok"}
    elif type == "LOCK":
        devices[id] = Lock(id, id)
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
@repeat_every(seconds=8)
def run_states():
    for device in devices.values():
        try:
            print(f"Running state for device {device.id}")
            device.run_state()
        except Exception as e:
            # print stack trace
            print(e.with_traceback())
            print(f"Error running state for device {device.id} {e}")


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="localhost", port=5000)