import './MainHeader.css';

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
              <a
                key={category.name}
                className='main-header-active'
                href='#'
                onClick={categoryHandler}
              >
                {category.name}
              </a>
            ) : (
              <a key={category.name} href='#' onClick={categoryHandler}>
                {category.name}
              </a>
            );
          })}
        </nav>
      </div>
      <nav>
        <a href='#'>Login</a>
      </nav>
    </header>
  );
};

export default MainHeader;
