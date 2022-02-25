import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';
import './Login.css';
const Login = (props) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  const usernameChangeHandler = (event) => {
    setUsername(event.target.value);
  };
  const passwordChangeHandler = (event) => {
    setPassword(event.target.value);
  };
  const submitHandler = (event) => {
    event.preventDefault();

    AuthService.login('http://localhost:8081/auth/login', username, password)
      .then((response) => {
        props.onSuccessLogin(response);
        navigate('/');
      })
      .catch((error) => {
        props.onFailLogin(error.response);
      });

    setUsername('');
    setPassword('');
  };
  return (
    <div className='d-flex align-items-center login-container'>
      <form
        className='form-login'
        action='#'
        method='post'
        onSubmit={submitHandler}
      >
        <input
          className='form-control'
          type='text'
          name='username'
          placeholder='Username'
          value={username}
          onChange={usernameChangeHandler}
          autoFocus
        ></input>
        <input
          className='form-control'
          type='password'
          name='password'
          placeholder='Password'
          value={password}
          onChange={passwordChangeHandler}
        ></input>
        {/* <p id='loginMessage' className='loginMessage'>Id or Password is not correct. </p> */}
        <button
          className='btn btn-lg btn-primary btn-block w-100 mb-2'
          type='submit'
        >
          Login
        </button>
        <div className='d-flex justify-content-end p-1 login-helper-tools'>
          <Link to='/signup'>Sign Up</Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
