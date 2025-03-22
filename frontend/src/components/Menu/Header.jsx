// Header.jsx
import {useContext} from "react";
import { useNavigate } from "react-router-dom"; 
import "../../styles/MenuCSS/Header.css";
import Dropdown from "./Dropdown.jsx";
import { AuthContext } from "../../contexts/AuthContext";

const Header = () => {
  const { authStatus, logout} = useContext(AuthContext); 
  const navigate = useNavigate();
  
  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <div className="header">
      {authStatus === "loggedIn" ? (
        <button onClick={logout}>로그아웃</button>
      ) : (
        <button onClick={() => handleNavigation("/login")}>로그인</button>
      )}

      <button onClick={() => handleNavigation("/box-location")}>
        의류수거함 위치
      </button>
      <Dropdown
        title="기부 가능한 의류"
        mainLink="/clothing-donation"
        items={[
          { label: "옷", link: "/clothes" },
          { label: "모자", link: "/hats" },
          { label: "신발", link: "/shoes" },
          { label: "이불", link: "/blankets" },
          { label: "기타", link: "/others" },
        ]}
      />
      {authStatus === "loggedIn" ? (
        <button onClick={() => handleNavigation("/mypage")}>마이페이지</button>
      ) : (
        <button onClick={() => handleNavigation("/clothing-impact")}>더보기</button>
      )}
    </div>
  );
};

export default Header;
