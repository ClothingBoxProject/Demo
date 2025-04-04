import { useEffect, useState, useContext} from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";
import axios from "axios";
import { setAccessToken } from "../../utils/db";

const KakaoRedirectPage = () => {
  const navigate = useNavigate();
  const { setAuthStatus } = useContext(AuthContext); 
  const [error, setError] = useState("");

  useEffect(() => {
    const handleKakaoLogin = async () => {
      const code = new URL(window.location.href).searchParams.get("code");

      if (!code) {
        setError("인가 코드가 없습니다.");
        return;
      }
      const redirectUri = import.meta.env.VITE_KAKAO_REDIRECT_URI;
      try {
        const response = await axios.get(
          `/api/oauth2/kakao/callback?code=${code}&redirect_uri=${encodeURIComponent(redirectUri)}`,
          { withCredentials: true } // refreshToken 쿠키 받기 위해 필요
        );

        const { accessToken } = response.data;

        await setAccessToken(accessToken); // DB 저장용 함수 호출
        setAuthStatus("loggedIn");
        navigate("/");

      } catch (err) {
        console.error("카카오 로그인 실패:", err);
        const msg = err.response?.data || "카카오 로그인 중 오류가 발생했습니다.";
        setError(msg);
      }
    };

    handleKakaoLogin();
  }, [navigate,setAuthStatus]);

  return (
    <div>
      {error ? <p style={{ color: "red" }}>{error}</p> : <p>카카오 로그인 처리 중입니다...</p>}
    </div>
  );
};

export default KakaoRedirectPage;
