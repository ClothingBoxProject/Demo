import React from "react";
import { useParams, useNavigate } from "react-router-dom";

import "../../styles/ClothingCSS/ProductDetail.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import DonationBanner from "../Menu/DonationBanner.jsx";

import clothImage from "../../assets/cloth.jpg"; // ✅ clothImage import

const ProductDetail = () => {
  const { id } = useParams(); // URL에서 제품 ID를 가져옴
  const navigate = useNavigate();

  // 예제 데이터를 사용 (실제 API 연동 가능)
  const productData = {
    1: { name: "스웨터", img: clothImage, desc: "따뜻한 마음" },
    2: { name: "후드", img: clothImage, desc: "마음을 담아" },
    3: { name: "바지", img: clothImage, desc: "따뜻한 겨울" },
  };

  const product = productData[id] || {
    name: "알 수 없는 제품",
    img: clothImage,
    desc: "제품 정보를 찾을 수 없습니다.",
  };

  return (
    <div className="page-container">
      <Header />
      <div className="content">
        <h1>{product.name}</h1>
        <p>{product.desc}</p>
        <p>스웨터를 올바르게 버리는 방법과 상태를 판별하는 팁을 확인해보세요.</p>
        <p>기부로 이어진다면 환경을 보호하고, 도움이 필요한 사람들에게 따뜻함을 전달할 수 있습니다.</p>

        <img src={product.img} alt={product.name} className="banner-image" />

        <div className="cloth-section">
          <h2>의류를 버리는 방법</h2>
          <ul>
            <li>기부 전에 세탁을 통해 스웨터를 깨끗하게 준비하세요.</li>
            <li>의류 수거함에 넣을 때는 다른 물품과 섞이지 않도록 봉투에 담아 기부하세요.</li>
            <li>의류 수거함 위치는 웹사이트를 통해 확인할 수 있습니다.</li>
            <li>겨울철 의류는 특히 도움이 필요하므로, 따뜻한 스웨터와 같은 의류는 기부 우선 항목입니다.</li>
          </ul>
          <p className="suggestion" onClick={() => navigate("/donation-change")}>수정 제안</p>
        </div>

        <div className="cloth-section">
          <h2>의류 상태 판별 방법</h2>
          <ul>
            <li>손상된 부분이 없는지 확인하세요 (예: 구멍, 올 풀림).</li>
            <li>심한 변색이나 얼룩이 없는지 검사하세요.</li>
            <li>보풀이 많을 경우 제거하여 깔끔한 상태로 만드세요.</li>
            <li>의류의 향을 맡아 불쾌한 냄새가 나지 않는지 확인하세요.</li>
            <li>깨끗하고 재사용 가능한 상태라면 기부가 가능합니다.</li>
          </ul>
        </div>

        {/* ✅ 배너 컴포넌트 적용 */}
        <DonationBanner />

      </div>
      <Footer />
    </div>
  );
};

export default ProductDetail;
