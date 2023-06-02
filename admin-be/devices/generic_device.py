import datetime
from abc import ABC, abstractmethod

class Device(ABC):

    def __init__(self, id, name) -> None:
        self.id = id
        self.name = name
        self.modes = {}
        self.current_mode = None
        self.current_mode_index = -1


    def log(self, type, text, value):
        with open(f"./logs/{self.id}.log", "a") as f:
            curr_iso_time = datetime.datetime.now().isoformat()
            f.write(f"{type},{text},{value},{curr_iso_time}\n")
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
