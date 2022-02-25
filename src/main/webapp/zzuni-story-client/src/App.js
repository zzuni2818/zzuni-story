import React, { useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import jwt_decode from 'jwt-decode';
import './App.css';
import MainHeader from './components/MainHeader/MainHeader';
import Login from './components/pages/Login';
import Signup from './components/pages/Signup';
import AuthService from './components/services/AuthService';

function App() {
  const [mainCategories, setMainCategories] = useState([
    { name: 'Home', selected: true },
    { name: 'Tech', selected: false },
    { name: 'Life', selected: false },
    { name: 'Portfolio', selected: false },
  ]);
  const [username, setUsername] = useState('');

  const selectMainCategoryHandler = (categoryName) => {
    console.log('on selectMainCategoryHandler.categoryName: ', categoryName);
    const selectedCategoryName = categoryName;
    const categories = mainCategories.map((category) => {
      let newCategory = { ...category };
      if (newCategory.name === selectedCategoryName) {
        newCategory.selected = true;
      } else {
        newCategory.selected = false;
      }
      return newCategory;
    });
    setMainCategories(categories);
  };

  const loginSuccessHandler = (response) => {
    const decoded = jwt_decode(response.data.accessToken);

    setUsername(decoded.sub);
    AuthService.setup(response.data);
    selectMainCategoryHandler(mainCategories[0].name);
  };
  const loginFailHandler = (response) => {};

  const logoutSuccessHandler = (response) => {
    setUsername('');
    AuthService.reset();
  };

  const logoutFailHandler = (response) => {};

  return (
    <div className='container'>
      <MainHeader
        username={username}
        categories={mainCategories}
        onSelectedMainCategory={selectMainCategoryHandler}
        onSuccessLogout={logoutSuccessHandler}
        onFailLogout={logoutFailHandler}
      />
      <Routes>
        <Route
          path='/login'
          element={
            <Login
              onSuccessLogin={loginSuccessHandler}
              onFailLogin={loginFailHandler}
            />
          }
        />
        <Route path='/signup' element={<Signup />} />
      </Routes>
      {/* <main>main</main> */}
      <footer>footer</footer>
    </div>
  );
}

export default App;
