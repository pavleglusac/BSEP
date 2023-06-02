from generic_device import Device
from copy import deepcopy
import random

class Thermometer(Device):

    def __init__(self, id, name) -> None:
        super().__init__(id, name)
        self.value = 20.0
        self.goal = 20.0
        self.modes = {
            "NORMAL": [[self.state_normal, 1]],
            "HIGH_TEMPERATURE": [[self.state_high_temperature, 1]],
            "LOW_TEMPERATURE": [[self.state_low_temperature, 1]]
        }
        self.current_mode = deepcopy(self.modes["NORMAL"])
        self.current_mode_index = 0

    def state_normal(self):
        self.log("INFO", "Measured temperature", self.value)


    def state_high_temperature(self):
        self.log("ALARM", "High temperature", self.value)

    
    def state_low_temperature(self):
        self.log("ALARM", "Low temperature", self.value)


    def shift_to_normal(self):
        random_normal_temperature = random.randint(5, 35)
        self.goal = random_normal_temperature
        self.shift_temperature_to(random_normal_temperature)

    
    def shift_to_high(self):
        random_high_temperature = random.randint(35, 50)
        self.goal = random_high_temperature
        self.shift_temperature_to(random_high_temperature)


    def shift_to_low(self):
        random_low_temperature = random.randint(-20, 5)
        self.goal = random_low_temperature
        self.shift_temperature_to(random_low_temperature)


    def shift_temperature_to(self, value):
        if self.value < value:
            self.value += 1.2
            return False
        elif self.value > value:
            self.value -= 1.2
            return False
        return True


    def pick_a_state(self):
        states_odds = {
            "NORMAL": 0.5,
            "HIGH_TEMPERATURE": 0.25,
            "LOW_TEMPERATURE": 0.25
        }
        if abs(self.goal - self.value) <= 2:
            picked_state = random.choices(list(states_odds.keys()), list(states_odds.values()))[0]
            self.current_mode = deepcopy(self.modes[picked_state])
            func = self.current_mode[0]
            func()
        else:
            self.shift_temperature_to(self.goal)
            

