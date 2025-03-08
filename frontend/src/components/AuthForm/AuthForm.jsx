import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";

const AuthForm = ({ type }) => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // 폼 제출 처리
  const handleSubmit = (e) => {
    e.preventDefault();
    const users = JSON.parse(localStorage.getItem("users")) || [];

    if (type === "findId") {
          // 아이디 찾기 로직
          const user = users.find((user) => user.email === email);
          if (user) {
            navigate("/find-id-result", { state: { foundId: user.email } });
          } else {
            setError("해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
          }
          return;
    }

    if (type === "login") {
      const user = users.find((user) => user.email === email && user.password === password);
      if (user) {
        alert("로그인 성공!");
        navigate("/main");
      } else {
        setError("이메일 또는 비밀번호가 잘못되었습니다.");
      }
    } else if (type === "register") {
      if (users.some((user) => user.email === email)) {
        setError("이미 존재하는 이메일입니다.");
      } else {
        users.push({ email, password });
        localStorage.setItem("users", JSON.stringify(users));
        alert("회원가입 성공! 로그인 해주세요.");
        navigate("/login");
      }
    } else if (type === "findId") {
      const user = users.find((user) => user.email === email);
      if (user) {
        alert(`아이디는 ${user.email}입니다.`);
      } else {
        setError("해당 이메일로 등록된 아이디가 없습니다.");
      }
    } else if (type === "resetPassword") {
      const userIndex = users.findIndex((user) => user.email === email);
      if (userIndex !== -1) {
        users[userIndex].password = password;
        localStorage.setItem("users", JSON.stringify(users));
        alert("비밀번호 변경 완료! 로그인 해주세요.");
        navigate("/login");
      } else {
        setError("이메일을 찾을 수 없습니다.");
      }
    }
  };

  return (
    <div className="page-container">
      <Header />
      <div className="content">
        <div className="form-container">
          <p class="form-top-text">
            {type === "login" && "로그인"}
            {type === "register" && "회원가입"}
            {type === "findId" && "아이디 찾기"}
            {type === "resetPassword" && "비밀번호 재설정"}
          </p>

          <form onSubmit={handleSubmit}>
            {(type === "login" || type === "register" || type === "findId" || type === "resetPassword") && (
              <input type="email" placeholder="이메일 주소" value={email} onChange={(e) => setEmail(e.target.value)} required />
            )}
            {(type === "login" || type === "register" || type === "resetPassword") && (
              <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} required />
            )}
            {error && <p className="error">{error}</p>}
            <button type="submit">
              {type === "login" && "로그인"}
              {type === "register" && "회원가입"}
              {type === "findId" && "다음"}
              {type === "resetPassword" && "비밀번호 변경"}
            </button>
          </form>

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
