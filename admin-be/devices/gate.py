from generic_device import Device
from copy import deepcopy
import random

class Gate(Device):
    def __init__(self, id, name) -> None:
        super().__init__(id, name)
        self.modes = {
            "OPEN": [[self.state_open, 20], [self.state_closed, 20]],
            "CLOSED": [[self.state_closed, 20], [self.state_open, 20]],
            "ALARM_FORCE": [[self.state_alarm_force, 1]],
            "ALARM_TAMPER": [[self.state_alarm_tamper, 1]]
        }
        

    def state_open(self):
        self.log("INFO", "Gate is open", 1)

    def state_closed(self):
        self.log("INFO", "Gate is closed", 0)

    def state_alarm_force(self):
        self.log("ALARM", "Gate forced", 1)

    def state_alarm_tamper(self):
        self.log("ALARM", "Gate tampered", 1)

    def pick_a_state(self):
        states_odds = {
            "OPEN": 0.5,
            "CLOSED": 0.5,
            "ALARM_FORCE": 0.25,
            "ALARM_TAMPER": 0.25
        }

        picked_state = random.choices(list(states_odds.keys()), list(states_odds.values()))[0]
        self.current_mode = deepcopy(self.modes[picked_state])
        self.current_mode_index = 0
