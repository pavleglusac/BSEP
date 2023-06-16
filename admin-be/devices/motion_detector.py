from generic_device import Device
from copy import deepcopy
import random

class MotionDetector(Device):
    def __init__(self, id, name) -> None:
        super().__init__(id, name)

        self.modes = {
            "NORMAL": [[self.state_normal_detection, 5], [self.state_no_detection, 5]],
            "NO_DETECTION": [[self.state_no_detection, 5]],
            "ALARM": [[self.state_alarm, 5]]
        }

        self.current_mode = deepcopy(self.modes["NORMAL"])
        self.current_mode_index = 0
        
    def state_normal_detection(self):
        self.log("INFO", "Motion detected", 1)


    def state_no_detection(self):
        self.log("INFO", "No motion detected", 0)


    def state_alarm(self):
        self.log("ALARM", "Motion detected", 1)

    
    def pick_a_state(self):
        states_odds = {
            "NORMAL": 0.2,
            "NO_DETECTION": 0.1,
            "ALARM": 0.7
        }

        picked_state = random.choices(list(states_odds.keys()), list(states_odds.values()))[0]
        self.current_mode = deepcopy(self.modes[picked_state])
        self.current_mode_index = 0

