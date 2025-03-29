import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/SuggestionFormCSS/SuggestionForm.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import { getAccessToken } from "../../utils/db";
import axios from "axios";

const SuggestionForm = () => {
  const navigate = useNavigate();
  const [category, setCategory] = useState("의류 정보-옷");
  const [suggestion, setSuggestion] = useState("");
  const [userId, setUserId] = useState(null); // 로그인한 사용자 ID 저장
  const [loading, setLoading] = useState(true); // 로딩 상태 추가

  // 로그인한 사용자의 ID를 가져오는 useEffect
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const token = await getAccessToken();
        const response = await axios.get("/api/users/me", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        });

        //console.log("로그인한 사용자 정보:", response.data); // 로그인한 사용자 정보 출력

        if (response.data && response.data.userId) {
          setUserId(response.data.userId); // 로그인한 사용자 ID 저장
        } else {
          throw new Error("사용자 정보가 없습니다.");
        }
      } catch (error) {
        //console.error("사용자 정보를 가져오는 중 오류 발생:", error);
        if (error.response && error.response.status === 401) {
          alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
          navigate("/login"); // 로그인되지 않은 경우 로그인 페이지로 이동
        } else {
          alert("사용자 정보를 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
      } finally {
        setLoading(false); // 데이터 로드 완료 후 로딩 상태 false로 변경
      }
    };
    fetchUserData();
  }, [navigate]);

  // 수정 제안 제출 처리
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (suggestion.trim() === "") {
      alert("수정 제안을 입력해주세요.");
      return;
    }

    if (!userId) {
      alert("로그인된 사용자가 없습니다.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/suggestions/${userId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          category,
          suggestionText: suggestion,
        }),
      });

      if (!response.ok) {
        throw new Error("서버 오류: 수정 제안 제출 실패");
      }

      // 수정 제안 제출 완료 후 alert 띄우고 수정 제안 이전 페이지로 이동
      alert("제출되었습니다. 제안 감사합니다!");
      setSuggestion("");
      setCategory("의류 정보-옷");
      navigate(-1);

    } catch (error) {
      console.error("제출 중 오류 발생:", error);
      alert("제출에 실패했습니다. 다시 시도해주세요.");
    }
  };

  // 로딩 상태일 때 로딩 메시지 표시
  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className="page-container">
      <Header />

      <div className="content">
        <h1>수정 제안</h1>
        <p>수정 제안을 입력해주세요.</p>

        <form id="suggestionForm" onSubmit={handleSubmit}>
          <div className="revise-section1">
            <h2>상세 분류</h2>
            <select
              name="category"
              className="category-dropdown"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
            >
              <option value="의류 정보-옷">의류 정보-옷</option>
              <option value="의류 정보-모자">의류 정보-모자</option>
              <option value="의류 정보-신발">의류 정보-신발</option>
              <option value="의류 정보-이불">의류 정보-이불</option>
              <option value="의류 정보-기타">의류 정보-기타</option>
              <option value="수거함 위치 정보">수거함 위치 정보</option>
              <option value="기타">기타</option>
            </select>
          </div>

          <div className="revise-section2">
            <h2>수정 제안 내용</h2>
            <textarea
              name="suggestion"
              className="text-area"
              value={suggestion}
              onChange={(e) => setSuggestion(e.target.value)}
              placeholder="여기에 수정 제안을 작성하세요..."
            />
            <button type="submit" className="submit-button">
              제출
            </button>
          </div>
        </form>
      </div>

      <Footer />
    </div>
  );
};

export default SuggestionForm;
