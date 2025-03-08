import React from "react";
import { useNavigate } from "react-router-dom";

import "../../styles/MenuCSS/DonationBanner.css";

const DonationBanner = () => {
  const navigate = useNavigate();

  return (
    <div className="donation-value-section">
      <h2>기부의 가치</h2>
      <p id="donation-text">
        {"의류 기부를 통해 환경 보호와 함께 도움이 필요한 사람들에게\n나눔의 가치를 전달할 수 있습니다."}
      </p>
      <button className="learn-more-button" onClick={() => navigate("/clothing-impact")}>
        기부 방법 알아보기
      </button>
    </div>
  );
};

export default DonationBanner;
