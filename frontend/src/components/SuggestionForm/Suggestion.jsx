import React from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/SuggestionFormCSS/Suggestion.css"; // 스타일 추가

const Suggestion = () => {
  const navigate = useNavigate();

  return (
    <div className="suggestion-container">
      <p className="suggestion_text">수정하고 싶은 부분이 있나요?</p>
      <p className="suggestion" onClick={() => navigate("/suggestion")}>수정 제안</p>
    </div>
  );
};

export default Suggestion;
