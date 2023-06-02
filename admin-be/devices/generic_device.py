import datetime
from abc import ABC, abstractmethod
import uuid
import logging

class Device(ABC):

    def __init__(self, id, name) -> None:
        self.id = id
        self.name = name
        self.modes = {}
        self.current_mode = None
        self.current_mode_index = -1


    def log(self, type, text, value):
        print(f"Logging message {type} {text} {value}")
        logging.info(f"Logging message {type} {text} {value}")
        # create file if not exists


        with open(f"./devices/logs/{self.id}.log", "a") as f:
            curr_iso_time = datetime.datetime.now().isoformat()
            # create new message uuid
            message_uuid = uuid.uuid4()
            f.write(f"{message_uuid},{type},{text},{value},{curr_iso_time}\n")
            f.flush()


    @abstractmethod
    def pick_a_state(self):
        pass


    def run_state(self):
        running_mode = self.current_mode[self.current_mode_index]
        func = running_mode[0]
        amount_of_seconds = running_mode[1]
        if amount_of_seconds == 0:
            self.current_mode_index = self.current_mode_index + 1
            if self.current_mode_index >= len(self.current_mode):
                self.pick_a_state()
                return
            else:
                self.run_state()
        else:
            func()
            self.current_mode[self.current_mode_index][1] = amount_of_seconds - 1
