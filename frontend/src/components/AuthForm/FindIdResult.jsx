import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

import "../../styles/AuthFormCSS/AuthForm.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";

const FindIdResult = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { foundId } = location.state || {}; // 전달된 아이디 정보

  return (
    <div className="page-container">
      <Header />
      <div className="content">
        <div className="form-container">
          <h1>아이디 찾기 결과</h1>
          <p>등록된 이메일 주소로 연결된 아이디는 다음과 같습니다</p>

          <div className="id-display">
            <span className="id-text">{foundId || "찾을 수 없음"}</span>
          </div>

          <button onClick={() => navigate("/login")} className="button">
            로그인 페이지로 이동
          </button>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default FindIdResult;
