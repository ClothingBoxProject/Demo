import React from "react";
import kakaoIcon from "../../assets/kakao.png";
import googleIcon from "../../assets/google.png";
import naverIcon from "../../assets/naver.png";
import "../../styles/AuthFormCSS/SocialLoginButtons.css";

const SocialLoginButtons = ({ handleOAuthLogin }) => {
  return (
    <div className="social-login-section">
     소셜 계정으로 로그인
     <div className="social-btn-wrapper">
      <button className="social-btn google" onClick={() => handleOAuthLogin("google")}>
        <div className="icon-wrapper">
          <img src={googleIcon} alt="Google" />
        </div>
        <span className="btn-text">Sign in with Google</span>
      </button>

      <button className="social-btn kakao" onClick={() => handleOAuthLogin("kakao")}>
        <div className="icon-wrapper">
          <img src={kakaoIcon} alt="Kakao" />
        </div>
        <span className="btn-text">Login with Kakao</span>
      </button>

      <button className="social-btn naver" onClick={() => handleOAuthLogin("naver")}>
        <div className="icon-wrapper">
          <img src={naverIcon} alt="Naver" />
        </div>
        <span className="btn-text">Log in with Naver</span>
      </button>
    </div>
    </div>
  );
};

export default SocialLoginButtons;
