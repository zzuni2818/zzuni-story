import axios from 'axios';
import Cookies from 'universal-cookie/es6';

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
      (error) => {}
    );
  }

  setup(data) {
    this.accessToken = data.accessToken;

    const cookies = new Cookies();
    cookies.set('refreshToken', data.refreshToken);

    this.setupAxiosInterceptors();
  }

  reset() {
    this.accessToken = '';

    const cookies = new Cookies();
    cookies.remove('refreshToken');
  }

  login(url, username, password) {
    return axios.post(url, {
      username: username,
      password: password,
    });
  }

  logout(url, username) {
    return axios.post(url, {
      username: username,
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
