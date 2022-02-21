import { useState } from 'react';
import { Link } from 'react-router-dom';
import './Login.css';
const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const usernameChangeHandler = (event) => {
    setUsername(event.target.value);
  };
  const passwordChangeHandler = (event) => {
    setPassword(event.target.value);
  };
  const submitHandler = (event) => {
    event.preventDefault();
    //TODO
    //add post data
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
          id='inputUserName'
          placeholder='Username'
          onChange={usernameChangeHandler}
          autoFocus
        ></input>
        <input
          className='form-control'
          type='text'
          name='password'
          id='inputPassword'
          placeholder='Password'
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
          <Link to='#'>Sign Up</Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
