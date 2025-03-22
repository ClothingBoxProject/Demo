import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/UserCSS/MyPage.css";
import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import clothImage from "../../assets/cloth.jpg";

const MyPage = () => {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [user, setUser] = useState({
    id: null,
    name: "",
    email: "",
    profileImage: clothImage,
  });

  // 기부 내역 데이터
  const donationHistory = [
    { date: "2024-01-15", item: "옷 10벌" },
    { date: "2024-01-15", item: "신발 3켤레" },
    { date: "2024-01-15", item: "모자 2개" },
  ];

  // 활동 통계
  const totalDonations = donationHistory.length;
  const totalItems = donationHistory.reduce((acc, cur) => acc + parseInt(cur.item.match(/\d+/)[0]), 0);

  // useEffect 함수 : React의 Hook 함수, 컴포넌트가 렌더링 될 때 실행
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetchWithAuth("/api/users/me", { method: "GET" }, navigate);
        if (!response.ok) {
          throw new Error("사용자 정보를 불러오는 데 실패했습니다.");
        }
        const data = await response.json();
        setUser({
          id: data.id,
          name: data.userName,
          email: data.email,
          profileImage: clothImage,
        });
      } catch (error) {
        console.error("Error:", error);
      }
    };
    fetchUserData();
  }, [navigate]);

  // 사용자 정보 수정
  const handleEdit = async () => {
    if (isEditing) {
      if (window.confirm("정보를 수정하시겠습니까?")) {
        try {
          const response = await fetchWithAuth("/api/users/me", {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userName: user.name, email: user.email }),
          }, navigate);

          if (!response.ok) {
            throw new Error("사용자 정보를 업데이트하는 데 실패했습니다.");
          }

          const updatedUser = await response.json();
          setUser(updatedUser);
          alert("정보가 성공적으로 수정되었습니다.");
          setIsEditing(false);
        } catch (error) {
          console.error("Error:", error);
        }
      }
    } else {
      setIsEditing(true);
    }
  };

  return (
    <div className="page-container">
      <Header />
      <div className="user-profile">
        <div className="profile-section">
          <div className="profile-image">
              <label htmlFor="file-upload">
                <img src={user.profileImage} alt="프로필 사진" className="profile-img" />
              </label>
              <input type="file" id="file-upload" accept="image/*" style={{ display: "none" }}/>
          </div>

          <div className="profile-info">
              <h2>프로필 정보</h2>
              <label>이름
              <input type="text" value={user.name} onChange={(e) => setUser({ ...user, name: e.target.value })} disabled={!isEditing} />
              </label>
              <label>이메일
              <input type="email" value={user.email} onChange={(e) => setUser({ ...user, email: e.target.value })} disabled={!isEditing} />
              </label>
              <div className="edit-container">
                <button className="edit-button" onClick={handleEdit}>
                      {isEditing ? "저장" : "정보 수정"}
                </button>
              </div>
            </div>
        </div>

        {/* 활동 통계 */}
        <div className="stats-section">
          <div className="stats-box">
            <h3>총 기부 횟수</h3>
            <p>{totalDonations}회</p>
          </div>
          <div className="stats-box">
            <h3>총 기부 물품</h3>
            <p>{totalItems}개</p>
          </div>
        </div>

        {/* 기부 내역 */}
        <div className="donation-history">
          <h3>기부 내역</h3>
          <ul>
            {donationHistory.map((donation, index) => (
              <li key={index}>
                <strong>{donation.date}:</strong> {donation.item}
              </li>
            ))}
          </ul>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default MyPage;
