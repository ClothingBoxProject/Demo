import React, { useEffect } from "react";
import "../../styles/ClothingCSS/ClothingImpact.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";
import clothGraph1 from "../../assets/cloth-graph1.png";
import clothGraph2 from "../../assets/cloth-graph2.png";

const ClothingImpact = () => {
  useEffect(() => {
    const sections = document.querySelectorAll(".section");
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("visible");
          }
        });
      },
      { threshold: 0.1 }
    );

    sections.forEach((section) => observer.observe(section));

    return () => {
      sections.forEach((section) => observer.unobserve(section));
    };
  }, []);

  return (
    <div className="page-container">
      <Header />

      {/* 섹션 1: 환경 보호 효과 */}
      <div className="section">
        <div className="content">
          <h2>환경 보호 기여</h2>
          <p>
            의류 기부는 환경 보호에 큰 기여를 합니다. 예를 들어, 아름다운가게는 2020년 한 해 동안 물품 재사용으로
            총 1억 2,993만 6,026kg의 탄소 배출을 저감했으며, 이는 4,677만 6,969그루의 소나무를 심는 효과와 같습니다.
          </p>
        </div>
        <div className="image">
          <img src={clothGraph1} alt="의류 소비량 그래프" />
        </div>
        <div className="source">
          <p>
            <a href="https://h21.hani.co.kr/arti/society/environment/50746.html" target="_blank" rel="noopener noreferrer">
              출처: 한겨레21, 헌 옷 줄게, 환경 지켜다오~
            </a>
          </p>
        </div>
      </div>

      {/* 섹션 2: 의류 생산의 환경적 비용 */}
      <div className="section">
        <div className="image">
          <img src={clothGraph2} alt="의류 생산 비용 그래프" />
        </div>
        <div className="content">
          <h2>의류 생산의 환경적 비용</h2>
          <p>
            청바지 한 벌을 만드는데 물이 약 7000리터, 티셔츠 한 장을 만드는데 약 2700리터가 필요합니다.
            한국환경공단에 따르면, 2020년 한 해에 버려진 섬유폐기물은 37만 664톤이며, 그중 재활용된 양은 단 6%에 불과합니다.
          </p>
        </div>
        <div className="source">
          <p>
            <a href="https://www.catholictimes.org/article/20240624500195" target="_blank" rel="noopener noreferrer">
              출처: 가톨릭타임즈, 지구적 재앙된 의류 폐기물…덜 버리고 오래 입는 것이 해결책
            </a>
          </p>
        </div>
      </div>

      {/* 기부 업체 소개 */}
      <div className="three-sections">
        {/* 아름다운 가게 */}
        <div className="section">
          <div className="content">
            <h2>아름다운 가게</h2>
            <p>아름다운 가게는 기부된 물품을 판매하여 사회적 가치가 있는 다양한 사업에 사용합니다.</p>
            <div className="source">
              <p>
                <a href="https://www.beautifulstore.org/" target="_blank" rel="noopener noreferrer">
                  아름다운 가게 홈페이지 가기
                </a>
              </p>
            </div>
          </div>
        </div>

        {/* 굿윌스토어 */}
        <div className="section">
          <div className="content">
            <h2>굿윌스토어</h2>
            <p>굿윌스토어는 기부된 의류를 판매하여 고용 창출과 자립 지원을 위한 프로그램을 제공합니다.</p>
            <div className="source">
              <p>
                <a href="https://goodwillstore.org/" target="_blank" rel="noopener noreferrer">
                  굿윌스토어 홈페이지 가기
                </a>
              </p>
            </div>
          </div>
        </div>

        {/* 옷캔 */}
        <div className="section">
          <div className="content">
            <h2>옷캔</h2>
            <p>옷캔은 기부된 의류를 재활용하여 저소득층 지원에 활용됩니다.</p>
            <div className="source">
              <p>
                <a href="https://otcan.org/" target="_blank" rel="noopener noreferrer">
                  옷캔 홈페이지 가기
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default ClothingImpact;
