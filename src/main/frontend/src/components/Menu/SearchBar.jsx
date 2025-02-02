import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../styles/MenuCSS/SearchBar.css";

const SearchBar = ({ defaultQuery = "" }) => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState(defaultQuery);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim() === "") return;
    navigate(`/search/${searchQuery}`);
  };

  return (
    <div className="search-container">
      <form onSubmit={handleSearch}>
        <input
          type="text"
          className="search-input"
          placeholder="기부 가능한 의류를 검색하세요"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <button type="submit" className="search-btn">검색</button>
      </form>
    </div>
  );
};

export default SearchBar;
