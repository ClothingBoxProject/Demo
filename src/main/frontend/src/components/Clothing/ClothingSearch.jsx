import React, { useState, useEffect } from "react";
import "../../styles/ClothingCSS/ClothingSearch.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import SearchBar from "../Menu/SearchBar.jsx";

import example from "../../assets/cloth.jpg";
import example2 from "../../assets/cloth2.png";

const slidesData = [
  { img: example, title: "의류 1", description: "분류: 재사용 가능 \n 버리는방법 : 세탁 후 기부" },
  { img: example2, title: "의류 2", description: "분류: 재사용 가능 | 세탁 후 기부" },
];

// 첫 번째와 마지막 슬라이드를 복제하여 추가
const extendedSlides = [
  slidesData[slidesData.length - 1], // 마지막 슬라이드를 맨 앞에 추가
  ...slidesData,
  slidesData[0], // 첫 번째 슬라이드를 맨 뒤에 추가
];

const ClothingSearch = () => {
  const [currentIndex, setCurrentIndex] = useState(1); // 첫 번째 슬라이드부터 시작
  const [isAnimating, setIsAnimating] = useState(false);

  // 슬라이드 이동 함수
  const moveSlide = (direction) => {
    if (isAnimating) return;
    setIsAnimating(true);

    setCurrentIndex((prevIndex) => prevIndex + direction);

    setTimeout(() => {
      setIsAnimating(false);
    }, 500);
  };

  // 자동 슬라이드 (3초마다 이동)
  useEffect(() => {
    const interval = setInterval(() => {
      moveSlide(1);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  // 애니메이션 끝나면 첫 번째 슬라이드와 마지막 슬라이드를 자연스럽게 처리
  useEffect(() => {
    if (currentIndex === extendedSlides.length - 1) {
      setTimeout(() => {
        setIsAnimating(false);
        setCurrentIndex(1);
      }, 500);
    }
    if (currentIndex === 0) {
      setTimeout(() => {
        setIsAnimating(false);
        setCurrentIndex(extendedSlides.length - 2);
      }, 500);
    }
  }, [currentIndex]);

  return (
    <div className="page-container">
      <Header />

      {/* 메인 콘텐츠 */}
      <div className="main-content">
        <h1>헷갈리시나요?</h1>

        {/* 검색창 */}
        <SearchBar />
        {/* 슬라이드 */}
        <div className="slider-container">
          <button className="slider-btn left" onClick={() => moveSlide(-1)}>&#10094;</button>
          <div
            className="slides"
            style={{
              transform: `translateX(-${currentIndex * 100}%)`,
              transition: isAnimating ? "transform 0.5s ease-in-out" : "none",
            }}
          >
            {extendedSlides.map((slide, index) => (
              <div key={index} className="slide">
                <img src={slide.img} alt={slide.title} />
                <div className="slide-text">
                  <h2>{slide.title}</h2>
                  <p>{slide.description}</p>
                </div>
              </div>
            ))}
          </div>
          <button className="slider-btn right" onClick={() => moveSlide(1)}>&#10095;</button>
        </div>

        {/* 인디케이터 */}
        <div className="indicators">
          {slidesData.map((_, index) => (
            <div
              key={index}
              className={`indicator ${index === currentIndex - 1 ? "active" : ""}`}
              onClick={() => setCurrentIndex(index + 1)}
            />
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ClothingSearch;
