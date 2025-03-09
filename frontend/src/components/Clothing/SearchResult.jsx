import React from "react";
import { useParams } from "react-router-dom";
import "../../styles/ClothingCSS/SearchResult.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import ProductCard from "./ProductCard";
import SearchBar from "../Menu/SearchBar.jsx";
import Suggestion from "../SuggestionForm/Suggestion.jsx";

const SearchResult = () => {
  const { query } = useParams(); // URL에서 검색어 가져오기

  // 더미 데이터 (실제 프로젝트에서는 API 호출하여 검색 결과 가져오기)
  const allProducts = [
    { id: 1, img: "/assets/cloth.jpg", name: "스웨터", desc: "따뜻한 마음" },
    { id: 2, img: "/assets/cloth.jpg", name: "후드", desc: "마음을 담아" },
    { id: 3, img: "/assets/cloth.jpg", name: "바지", desc: "따뜻한 겨울" },
  ];

  // 검색어를 포함하는 상품만 필터링
  const filteredProducts = allProducts.filter((product) =>
    product.name.includes(query)
  );

  const hasResults = filteredProducts.length > 0;

  return (
    <div className="page-container">
      <Header />

      {/* ✅ 검색창이 항상 상단에 유지되도록 */}
      <div className="search-bar-container">
        <SearchBar defaultQuery={query} />
      </div>

      <div className="find-content">

        {/* ✅ 검색 결과 없는 경우 UI 개선 */}
        {hasResults ? (
          <div className="product-grid">
            {filteredProducts.map((item) => (
              <ProductCard key={item.id} item={item} />
            ))}
          </div>
        ) : (
          <div id="no-results">
            <p id="no-results-title">검색 결과를 찾지 못했어요 :(</p>
            <p id="no-results-message">검색어를 다시 입력해주세요.</p>
          </div>
        )}

        {/* ✅ Suggestion 위치 조정하여 검색 결과가 없을 때도 보기 좋게 */}
        <Suggestion className="suggestion-wrapper" />
      </div>

      <Footer />
    </div>
  );
};

export default SearchResult;
