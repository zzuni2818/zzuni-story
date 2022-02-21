import React, { useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import './App.css';
import MainHeader from './components/MainHeader/MainHeader';
import Login from './components/Login/Login';

function App() {
  const [mainCategories, setMainCategories] = useState([
    { name: 'Home', selected: true },
    { name: 'Tech', selected: false },
    { name: 'Life', selected: false },
    { name: 'Portfolio', selected: false },
  ]);
  const selectMainCategoryHandler = (categoryName) => {
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

  return (
    <div className='container'>
      <MainHeader
        categories={mainCategories}
        onSelectedMainCategory={selectMainCategoryHandler}
      />
      <Routes>
        <Route path='/login' element={<Login />} />
      </Routes>
      {/* <main>main</main> */}
      <footer>footer</footer>
    </div>
  );
}

export default App;
