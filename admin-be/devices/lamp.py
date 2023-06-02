from generic_device import Device
from copy import deepcopy
import random

class Lamp(Device):
    def __init__(self, id, name) -> None:
        super().__init__(id, name)

        self.modes = {
            "NORMAL": [[self.state_on, 20], [self.state_off, 20]],
            "NORMAL_FLICKER": [[self.state_flicker, 20], [self.state_off, 20]],
            "ALARM_VOLTAGE_DROP": [[self.alarming_voltage_drop, 1]],
            "ALARM_VOLTAGE_SPIKE": [[self.alarming_voltage_spike, 1]],
            "ALARM_OVERHEATING": [[self.alarming_overheating, 1]]
        }

        self.current_mode = deepcopy(self.modes["NORMAL"])
        self.current_mode_index = 0
    
        
    def state_on(self):
        self.state = "on"
        self.log("INFO", "Lamp is on", 1)

    def state_off(self):
        self.state = "off"
        self.log("INFO", "Lamp is off", 0)

    def state_flicker(self):
        # 5 times off and on again
        for i in range(5):
            self.state_off()
            self.state_on()

    def alarming_voltage_drop(self):
        self.log("ALARM", "Voltage drop", 0)


    def alarming_voltage_spike(self):
        self.log("ALARM", "Voltage spike", 1)

    
    def alarming_overheating(self):
        self.log("ALARM", "Overheating", 1)


    def pick_a_state(self):
        states_odds = {
            "NORMAL": 0.5,
            "NORMAL_FLICKER": 0.2,
            "ALARM_VOLTAGE_DROP": 0.1,
            "ALARM_VOLTAGE_SPIKE": 0.1,
            "ALARM_OVERHEATING": 0.1
        }

        picked_state = random.choices(list(states_odds.keys()), list(states_odds.values()))[0]
        self.current_mode = deepcopy(self.modes[picked_state])
        self.current_mode_index = 0



