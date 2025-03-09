import React, { useState } from "react";
import "../../styles/UserCSS/MyPage.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import clothImage from "../../assets/cloth.jpg";

const MyPage = () => {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState("홍길동");
  const [email, setEmail] = useState("user@example.com");
  const [profileImage, setProfileImage] = useState(clothImage);

  // 기부 내역 데이터
  const donationHistory = [
    { date: "2024-01-15", item: "옷 10벌" },
    { date: "2024-01-15", item: "신발 3켤레" },
    { date: "2024-01-15", item: "모자 2개" },
  ];

  // 활동 통계
  const totalDonations = donationHistory.length;
  const totalItems = donationHistory.reduce((acc, cur) => acc + parseInt(cur.item.match(/\d+/)[0]), 0);

  // 프로필 이미지 업로드
  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setProfileImage(imageUrl);
    }
  };

  // 정보 수정 활성화/비활성화
  const handleEdit = () => {
    if (isEditing) {
      if (window.confirm("정보를 수정하시겠습니까?")) {
        alert("정보가 성공적으로 수정되었습니다.");
        setIsEditing(false);
      }
    } else {
      setIsEditing(true);
    }
  };

  return (
    <div className="page-container">
      <Header />

      <div className="user-profile">
        {/* 프로필 이미지 및 정보 */}
        <div className="profile-section">
          <div className="profile-image">
                      <label htmlFor="file-upload">
                        <img src={profileImage} alt="프로필 사진" className="profile-img" />
                      </label>
                      <input
                        type="file"
                        id="file-upload"
                        accept="image/*"
                        onChange={handleImageUpload}
                        style={{ display: "none" }}
                      />
                    </div>
          <div className="profile-info">
                      <h2>프로필 정보</h2>
                      <label>
                        이름
                        <input
                          type="text"
                          value={name}
                          onChange={(e) => setName(e.target.value)}
                          disabled={!isEditing} // 수정 가능 여부 설정
                        />
                      </label>
                      <label>
                        이메일
                        <input
                          type="email"
                          value={email}
                          onChange={(e) => setEmail(e.target.value)}
                          disabled={!isEditing} // 수정 가능 여부 설정
                        />
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
