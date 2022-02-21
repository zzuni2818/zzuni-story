import React, { useState } from 'react';
import './App.css';
import MainHeader from './components/MainHeader/MainHeader';

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
      <main>main</main>
      <footer>footer</footer>
    </div>
  );
}

export default App;
