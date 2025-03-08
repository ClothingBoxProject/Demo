import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/SuggestionFormCSS/SuggestionForm.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";

const SuggestionForm = () => {
  const navigate = useNavigate();
  const [category, setCategory] = useState("의류 정보-옷");
  const [suggestion, setSuggestion] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    if (suggestion.trim() === "") {
      alert("수정 제안을 입력해주세요.");
      return;
    }

    console.log("카테고리:", category);
    console.log("제안 내용:", suggestion);

    alert("제출되었습니다. 제안 감사합니다!");

    // 입력값 초기화
    setSuggestion("");
  };

  return (
    <div className="page-container">
      <Header />

      <div className="content">
        <h1>수정 제안</h1>
        <p>수정 제안을 입력해주세요.</p>

        <div class="revise-section1">
          <h2>상세 분류</h2>
          <form id="suggestionForm" action="#" method="post">
            <select name="category" class="category-dropdown">
              <option value="의류 정보-옷">의류 정보-옷</option>
              <option value="의류 정보-모자">의류 정보-모자</option>
              <option value="의류 정보-신발">의류 정보-신발</option>
              <option value="의류 정보-이불">의류 정보-이불</option>
              <option value="의류 정보-기타">의류 정보-기타</option>
              <option value="수거함 위치 정보">수거함 위치 정보</option>
              <option value="기타">기타</option>
            </select>
          </form>
       </div>

       <div class="revise-section2">
        <h2>수정 제안 내용</h2>
        <textarea name="suggestion" class="text-area" placeholder="여기에 수정 제안을 작성하세요..."></textarea>
        <button type="submit" class="submit-button">제출</button>
       </div>
      </div>

      <Footer />
    </div>
  );
};

export default SuggestionForm;
