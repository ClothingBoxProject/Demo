import React from "react";
import "../../styles/LocationCSS/LocationBin.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import SearchBar from "../Menu/SearchBar.jsx";
import Suggestion from "../SuggestionForm/Suggestion.jsx";
import MapComponent from "./MapComponent.jsx";

const LocationBin = () => {
  return (
    <div className="page-container">
      <Header />

      {/* 메인 콘텐츠 */}
      <div className="main-content">
        <h1>동작구 의류 수거함 위치</h1>
        <p>동작구 곳곳에 설치된 의류 수거함 위치를 안내합니다. 아래 지도에서 가까운 수거함 위치를 확인하세요.</p>
        <p>의류 수거함은 지역 사회의 자원 순환과 환경 보호에 기여하는 중요한 역할을 하고 있습니다.</p>

        {/* Kakao 지도 */}
        <MapComponent />

        {/* 추가 설명 */}
        <div className="description">
          가까운 의류 수거함을 확인하고, 기부를 통해 자원 순환과 환경 보호에 동참하세요.
          기부하신 의류는 필요한 이웃들에게 전달되어 가치 있는 재사용의 기회를 얻게 됩니다.
        </div>
      </div>

      {/* 수정 제안 */}
      <Suggestion />

      <Footer />
    </div>
  );
};

export default LocationBin;
