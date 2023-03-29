import axios from "axios";

axios.interceptors.request.use(
  async (config) => {
    const credentials: string | null = localStorage.getItem("jwt");
    if (credentials) {
      const jwt = JSON.parse(credentials);
      config.headers['authorization'] = `Bearer ${jwt.accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const config = error?.config;
    if (error?.response?.status === 401 && !config?.sent) {
      localStorage.removeItem("jwt");
      localStorage.removeItem("session");
      return axios(config);
    }
    return Promise.reject(error);
  }
);

export const axiosPrivate = axios;
