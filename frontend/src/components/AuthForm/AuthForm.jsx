// AuthForm.jsx
import React, { useState, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { setAccessToken } from "../../utils/db";
import { AuthContext } from "../../contexts/AuthContext";
import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import axios from "axios";
import SocialLoginButtons from "./SocialLoginButtons";

const AuthForm = ({ type }) => {
  const navigate = useNavigate();
  const { setAuthStatus } = useContext(AuthContext); 
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // 회원가입 요청 (백엔드 API 연결)
  const handleRegister = async () => {
      try {
        await axios.post("/api/auth/signup", { email, password }, { withCredentials: true });
        alert("회원가입 성공. 로그인 페이지로 이동합니다.");
        navigate("/login");
      } catch (error) {
        console.error("회원가입 중 오류:", error);
        const msg = error.response?.data || "중복된 이메일입니다.";
        setError(msg);
      }
  };

  // 로그인 요청 (백엔드 API 연결)
  const handleLogin = async () => {
    try {
      const response = await axios.post(
        "/api/auth/login",
        { email, password },
        { withCredentials: true }
      );

      const { accessToken } = response.data;
      await setAccessToken(accessToken);
      setAuthStatus("loggedIn");
      navigate("/");
    } catch (error) {
      console.error("로그인 오류:", error);
      const msg = error.response?.data || "이메일 또는 비밀번호가 잘못되었습니다.";
      setError(msg);
    }
  };

  const handleOAuthLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  useEffect(() => {
    const checkOAuthLogin = async () => {
      try {
        const token = await getAccessToken(); // ✅ IndexedDB에서 accessToken 가져오기
        if (token) {
          setAuthStatus("loggedIn");
          navigate("/"); // ✅ 홈으로 이동
        }
      } catch (error) {
        console.error("OAuth 로그인 상태 확인 실패:", error);
      }
    };

    checkOAuthLogin();
  }, []);

    if (provider === "kakao") {
      const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_REST_API_KEY;
      const REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI;
      const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${KAKAO_CLIENT_ID}&redirect_uri=${encodeURIComponent(REDIRECT_URI)}`;
      // 사용자가 카카오로 이동하기 위한 URL 
      window.location.href = kakaoAuthUrl;
    }
    // TODO: Google, Naver도 비슷하게 구현
  };

  // 폼 제출 처리
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (type === "register") {
      await handleRegister();
      return;
    }
    else if (type === "login") {
      await handleLogin();
    }
  };

  return (
    <div className="page-container">
      <Header />
      <div className="content">
        <div className="form-container">
          <p className="form-top-text">
            {type === "login" && "로그인"}
            {type === "register" && "회원가입"}
            {type === "findId" && "아이디 찾기"}
            {type === "resetPassword" && "비밀번호 재설정"}
          </p>

          <form onSubmit={handleSubmit}>
            {(type === "login" || type === "register" || type === "findId" || type === "resetPassword") && (
              <input 
                type="email" 
                placeholder="이메일 주소" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                required />
            )}
            {(type === "login" || type === "register" || type === "resetPassword") && (
              <input 
              type="password" 
              placeholder="비밀번호" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required />
            )}
            {error && <p className="error">{error}</p>}
            <button type="submit">
              {type === "login" && "로그인"}
              {type === "register" && "회원가입"}
              {type === "findId" && "다음"}
              {type === "resetPassword" && "비밀번호 변경"}
            </button>
          </form>

          {type === "login" && (
          <SocialLoginButtons handleOAuthLogin={handleOAuthLogin} />
          )}

          <div className="toggle">
            {type === "login" && (
              <>
                <a onClick={() => navigate("/find-id")}>아이디 찾기</a>
                <a onClick={() => navigate("/reset-password")}>비밀번호 재설정</a>
                <a onClick={() => navigate("/register")}>회원가입</a>
              </>
            )}
            {type === "register" && (
              <a onClick={() => navigate("/login")}>로그인</a>
            )}
            {(type === "findId" || type === "resetPassword") && (
              <a onClick={() => navigate("/login")}>로그인</a>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default AuthForm;
