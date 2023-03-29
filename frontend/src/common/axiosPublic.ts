import axios from "axios";

export const axiosPublic = axios.create({
  withCredentials: false,
  headers: {
    "Content-Type": "application/json",
  },
});
