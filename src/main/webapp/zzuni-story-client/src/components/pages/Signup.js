import { useState } from 'react';
import './Signup.css';

const Signup = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [usernameErrorMessage, setUsernameErrorMessage] = useState('');
  const [passwordErrorMessage, setPasswordErrorMessage] = useState('');
  const [passwordConfirmErrorMessage, setPasswordConfirmErrorMessage] =
    useState('');

  const usernameChangeHandler = (event) => {
    setUsernameErrorMessage('');
    setUsername(event.target.value);
  };
  const passwordChangeHandler = (event) => {
    setPasswordErrorMessage('');
    setPassword(event.target.value);
  };
  const passwordConfirmChangeHandler = (event) => {
    setPasswordConfirmErrorMessage('');
    setPasswordConfirm(event.target.value);
  };

  const submitHandler = (event) => {
    event.preventDefault();
    if (!username.length) {
      setUsernameErrorMessage('please enter username.');
      return;
    }
    //TODO
    // check username is duplicated or not
    // setUsernameErrorMessage('username is already exist.');
    // return
    if (!password.length) {
      setPasswordErrorMessage('please enter password.');
      return;
    }
    if (password !== passwordConfirm) {
      setPasswordConfirmErrorMessage('please check password.');
      return;
    }

    //TODO
    //POST
  };
  return (
    <div className='d-flex align-items-center signup-container'>
      <form
        className='form-signup'
        action='#'
        method='post'
        onSubmit={submitHandler}
      >
        <input
          type='text'
          className='form-control'
          placeholder='Username'
          autoFocus
          onChange={usernameChangeHandler}
        ></input>
        {!usernameErrorMessage && (
          <p className='form-signup-error-message-hide'>
            
          </p>
        )}
        {usernameErrorMessage && (
          <p className='form-signup-error-message-show'>
            {usernameErrorMessage}
          </p>
        )}
        <input
          type='password'
          className='form-control'
          placeholder='Password'
          onChange={passwordChangeHandler}
        ></input>
        {!passwordErrorMessage && (
          <p className='form-signup-error-message-hide'></p>
        )}
        {passwordErrorMessage && (
          <p className='form-signup-error-message-show'>
            {passwordErrorMessage}
          </p>
        )}
        <input
          type='password'
          className='form-control'
          placeholder='Password Confirm'
          onChange={passwordConfirmChangeHandler}
        ></input>
        {!passwordConfirmErrorMessage && (
          <p className='form-signup-error-message-hide'></p>
        )}
        {passwordConfirmErrorMessage && (
          <p className='form-signup-error-message-show'>
            {passwordConfirmErrorMessage}
          </p>
        )}
        <button
          className='btn btn-lg btn-primary btn-block w-100'
          type='submit'
        >
          Sign up
        </button>
      </form>
    </div>
  );
};

export default Signup;
