import { useState, useEffect } from 'react';
import './MainHeader.css';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

const MainHeader = (props) => {
  const [username, setUsername] = useState(props.username);
  const navigate = useNavigate();

  const categoryHandler = (event) => {
    props.onSelectedMainCategory(event.target.innerHTML);
  };

  useEffect(() => {
    setUsername(props.username);
  }, [props.username]); // when username of props updated, update username of state

  const loginHandler = () => {
    props.onSelectedMainCategory('');
  };

  const LogoutSuccessHandler = (response) => {
    props.onSuccessLogout(response);
    navigate('/login');
  };
  const LogoutFailHandler = (error) => {
    props.onFailLogout(error);
  };

  const logoutHandler = () => {
    props.onSelectedMainCategory('');
    AuthService.logout(LogoutSuccessHandler, LogoutFailHandler);
  };

  return (
    <header className='d-flex justify-content-between align-items-end main-header mt-3'>
      <div className='d-flex align-items-end'>
        <div className='main-header-title'>ZzuniStory</div>
        <nav className='d-flex gap-5 ms-5'>
          {props.categories.map((category) => {
            return category.selected ? (
              <Link
                key={category.name}
                className='main-header-active'
                to='/'
                onClick={categoryHandler}
              >
                {category.name}
              </Link>
            ) : (
              <Link key={category.name} to='#' onClick={categoryHandler}>
                {category.name}
              </Link>
            );
          })}
        </nav>
      </div>
      <nav>
        {!username && (
          <Link to='/login' onClick={loginHandler}>
            Login
          </Link>
        )}
        {username && <span onClick={logoutHandler}>{username}</span>}
        {/* onClick login and logout button remove effect which is selected category before */}
      </nav>
    </header>
  );
};

export default MainHeader;
