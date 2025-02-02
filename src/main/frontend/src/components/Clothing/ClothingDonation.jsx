import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import "../../styles/ClothingCSS/ClothingDonation.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import ProductCard from "./ProductCard.jsx";
import SearchBar from "../Menu/SearchBar.jsx"
import DonationBanner from "../Menu/DonationBanner.jsx";
import Suggestion from "../SuggestionForm/Suggestion.jsx"

import clothImage from "../../assets/cloth.jpg";

const ClothingDonation = () => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/search?query=${searchQuery}`);
  };

  return (
    <div className="page-container">
      <Header />
      <div className="content">
        <h1>기부 가능한 의류</h1>
        <p>의류를 올바르게 배출하여 환경을 보호해보세요.</p>

        {/* 검색창 */}
        <SearchBar />

        {/* 제품 목록 */}
        <div className="product-grid">
          {[
            { id: 1, img: clothImage, name: "스웨터", desc: "따뜻한 마음" },
            { id: 2, img: clothImage, name: "후드", desc: "마음을 담아" },
            { id: 3, img: clothImage, name: "바지", desc: "따뜻한 겨울" },
            { id: 4, img: clothImage, name: "셔츠", desc: "부드러운 터치" },
            { id: 5, img: clothImage, name: "바람막이", desc: "따뜻한 마음" },
            { id: 6, img: clothImage, name: "티셔츠", desc: "마음을 담아" },
            { id: 7, img: clothImage, name: "치마", desc: "따뜻한 겨울" },
            { id: 8, img: clothImage, name: "패딩", desc: "부드러운 터치" },
          ].map((item) => (
            <ProductCard key={item.id} item={item} />
          ))}
        </div>

        <DonationBanner />
      </div>

      <Suggestion /> {/* ✅ Suggestion 컴포넌트 추가 */}

      <Footer />
    </div>
  );
};

export default ClothingDonation;
