import './MainHeader.css';
import { Link } from 'react-router-dom';

const MainHeader = (props) => {
  const categoryHandler = (event) => {
    props.onSelectedMainCategory(event.target.innerHTML);
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
        <Link to='/login'>Login</Link>
      </nav>
    </header>
  );
};

export default MainHeader;
