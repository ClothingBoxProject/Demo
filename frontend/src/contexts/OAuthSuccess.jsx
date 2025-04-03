import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { setAccessToken } from "../utils/db";

const OAuthSuccess = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");

    if (token) {
      console.log("[OAuthSuccess] ✅ JWT 저장 중...");
      setAccessToken(token);
      navigate("/"); // ✅ 홈으로 이동
    } else {
      console.error("[OAuthSuccess] ❌ 토큰이 없습니다.");
      navigate("/login"); // ✅ 로그인 페이지로 이동
    }
  }, [navigate]);

  return <div>🔄 로그인 중...</div>;
};

export default OAuthSuccess;
