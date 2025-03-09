import React from "react";
import "../../styles/MenuCSS/Header.css";
import Dropdown from "./Dropdown.jsx";

const Header = () => {
  return (
    <div className="header">
      <a href="/login">로그인</a>
      <a href="/box-location">의류수거함 위치</a>

      <Dropdown
        title="기부 가능한 의류"
        mainLink="/clothing-donation"  // 🔹 추가된 부분
        items={[
          { label: "옷", link: "#clothes" },
          { label: "모자", link: "#hats" },
          { label: "신발", link: "#shoes" },
          { label: "이불", link: "#blankets" },
          { label: "기타", link: "#others" },
        ]}
      />

      <a href="/about">About</a>
    </div>
  );
};

export default Header;
