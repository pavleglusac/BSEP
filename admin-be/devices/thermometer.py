from generic_device import Device
from copy import deepcopy
import random

class Thermometer(Device):

    def __init__(self, id, name) -> None:
        super().__init__(id, name)
        self.value = 20.0
        self.goal = 20.0
        self.modes = {
            "NORMAL": [[self.pick_a_state, 1]],
        }
        self.current_mode = [[self.pick_a_state, 1]]
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
            self.value += 0.3
            return False
        elif self.value > value:
            self.value -= 0.3
            return False
        if self.value >= 35:
            self.state_high_temperature()
        elif self.value <= -5:
            self.state_low_temperature()
        else:
            self.state_normal()
        return True


    def pick_a_state(self):
        temperature_odds = {
            self.shift_to_high: 0.25,
            self.shift_to_normal: 0.5,
            self.shift_to_low: 0.25
        }
        self.current_mode = [[self.pick_a_state, 1]]
        self.current_mode_index = 0
        if abs(self.goal - self.value) <= 2:
            picked_state = random.choices(list(temperature_odds.keys()), list(temperature_odds.values()))[0]
            # invoke the function
            picked_state()
#             func = self.current_mode[0]
#             func()
        else:
            self.shift_temperature_to(self.goal)
            

