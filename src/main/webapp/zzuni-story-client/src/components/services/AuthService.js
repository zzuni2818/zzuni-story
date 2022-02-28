import axios from 'axios';
import jwt_decode from 'jwt-decode';

class AuthService {
  AuthService() {
    this.accessToken = '';
  }
  formatJwtToken(token) {
    return 'Bearer ' + token;
  }

  setupAxiosInterceptors() {
    //setup interceptors for request
    axios.interceptors.request.use(
      (config) => {
        if (this.accessToken) {
          config.headers.Authorization = this.formatJwtToken(this.accessToken);
        }
        return config;
      },
      (error) => {}
    );

    //setupinterceptors for response
    axios.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        if (error.response && error.response.status == 401) {
          try {
            const originalRequest = error.config;
            this.keepLoggedIn(
              (response) => {
                this.accessToken = response.data.accessToken;
                originalRequest.headers.Authorization = this.formatJwtToken(
                  this.accessToken
                );
                return axios.request(originalRequest);
              },
              (error) => {
                console.log(error);
              }
            );
          } catch (error) {
            console.log(error);
          }
          return Promise.reject(error);
        }
        return Promise.reject(error);
      }
    );
  }

  setup(data) {
    this.accessToken = data.accessToken;
    this.setupAxiosInterceptors();
  }

  reset() {
    this.accessToken = '';
  }

  login(username, password, onSuccess, onFail) {
    axios
      .post('http://localhost:8081/auth/login', {
        username: username,
        password: password,
      })
      .then((response) => {
        onSuccess(response);
      })
      .catch((error) => {
        onFail(error);
      });
  }

  logout(onSuccess, onFail) {
    if (!this.accessToken) return;
    const decoded = jwt_decode(this.accessToken);
    console.log('on logout, decoded: ', decoded);
    axios
      .post('http://localhost:8081/auth/logout', {
        userId: decoded.userId,
      })
      .then((response) => {
        onSuccess(response);
      })
      .catch((error) => {
        onFail(error);
      });
  }

  keepLoggedIn(onSuccess, onFail) {
    if (this.accessToken) {
      console.log('on keepLoggedIn, already loggedin');
      return;
    }

    axios
      .post('http://localhost:8081/auth/refresh_token')
      .then((response) => {
        console.log('on keepLoggedIn, response: ', response);
        onSuccess(response);
      })
      .catch((error) => {
        console.log('on keepLoggedIn, error: ', error);
        onFail(error);
      })
      .finally(() => {
        if (!this.accessToken) {
          this.reset();
        }
      });
  }

  execute(url, method, data) {
    if (method === 'GET') {
      return axios({ url: url, method: method, params: data });
    }
    return axios({ url: url, method: method, data: data });
  }
}

export default new AuthService();
