from generic_device import Device
from copy import deepcopy
import random

class Lock(Device):
    def __init__(self, id, name) -> None:
        super().__init__(id, name)

        self.modes = {
            "UNLOCKED": [[self.state_unlocked, 20], [self.state_locked, 20]],
            "LOCKED": [[self.state_locked, 20], [self.state_unlocked, 20]],
            "ALARM_FORCE": [[self.state_alarm_force, 1]],
            "ALARM_TAMPER": [[self.state_alarm_tamper, 1]]
        }
        

    def state_unlocked(self):
        self.log("INFO", "Lock is unlocked", 1)

    def state_locked(self):
        self.log("INFO", "Lock is locked", 0)

    def state_alarm_force(self):
        self.log("ALARM", "Lock forced", 1)

    def state_alarm_tamper(self):
        self.log("ALARM", "Lock tampered", 1)

    def pick_a_state(self):
        states_odds = {
            "UNLOCKED": 0.5,
            "LOCKED": 0.5,
            "ALARM_FORCE": 0.25,
            "ALARM_TAMPER": 0.25
        }

        picked_state = random.choices(list(states_odds.keys()), list(states_odds.values()))[0]
        self.current_mode = deepcopy(self.modes[picked_state])
        self.current_mode_index = 0