import authApi from "../apis/authApi"; 

export function applyAuthInterceptors(apiClient) {
  let isRefreshing = false;
  let failedQueue = [];

  const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
      if (error) {
        prom.reject(error);
      } else {
        prom.resolve(token);
      }
    });
    failedQueue = [];
  };

  apiClient.interceptors.request.use(config => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  });

  apiClient.interceptors.response.use(
    response => response,
    async error => {
      const originalRequest = error.config;

      if (
        error.response?.status === 401 &&
        !originalRequest._retry
      ) {
        if (isRefreshing) {
          return new Promise(function (resolve, reject) {
            failedQueue.push({ resolve, reject });
          })
            .then(token => {
              originalRequest.headers["Authorization"] = "Bearer " + token;
              return apiClient(originalRequest);
            })
            .catch(err => Promise.reject(err));
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
          const refreshToken = localStorage.getItem("refreshToken");
          if (!refreshToken) throw new Error("No refresh token");

          const response = await authApi.post("/auth/refresh", { refreshToken });
           
          const newAccessToken = response.data.accessToken;
          const newRefreshToken = response.data.refreshToken;

          localStorage.setItem("token", newAccessToken);
          if (newRefreshToken) {
            localStorage.setItem("refreshToken", newRefreshToken);
          }

          apiClient.defaults.headers.common["Authorization"] =
            "Bearer " + newAccessToken;
          originalRequest.headers["Authorization"] = "Bearer " + newAccessToken;

          processQueue(null, newAccessToken);
          return apiClient(originalRequest);
        } catch (err) {
          processQueue(err, null);
          localStorage.removeItem("token");
          localStorage.removeItem("refreshToken");
          window.location.href = "/login";
          return Promise.reject(err);
        } finally {
          isRefreshing = false;
        }
      }

      return Promise.reject(error);
    }
  );
}
