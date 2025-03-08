import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import "../../styles/MainCSS/Main.css";

import Header from "../Menu/Header.jsx";
import Footer from "../Menu/Footer.jsx";

const Main = () => {
  const navigate = useNavigate();

  // 스크롤 애니메이션 효과
  useEffect(() => {
    const sections = document.querySelectorAll('.section');
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
        }
      });
    }, { threshold: 0.1 });

    sections.forEach(section => observer.observe(section));

    return () => {
      sections.forEach(section => observer.unobserve(section));
    };
  }, []);

  return (
    <div className="page-container">
      <Header />
      <div className="main-intro">
        <h1>동작구 의류수거함 프로젝트</h1>
        <p>의류를 재활용하여 환경을 보호하고, 도움이 필요한 사람들에게 전달하는 의미 있는 동작구 의류수거함 프로젝트에 참여하세요.</p>
        <div className="scroll-arrow">▶</div> {/* 오른쪽 방향 기호를 아래 방향처럼 보이게 */}
      </div>

      {/* 섹션 1: 목적 */}
      <div className="section" id="purpose">
        <div className="text-container">
          <h2>동작구 의류수거함 프로젝트의 목적</h2>
          <p>동작구 의류수거함 프로젝트는 의류 기부와 재활용을 통해 환경 보호와 사회적 가치를 창출하는 것을 목표로 합니다.</p>
        </div>
        <img src="/assets/purpose.jpg" alt="의류수거함 이미지" />
      </div>

      {/* 섹션 2: 기부 가능한 의류 */}
      <div className="section" id="clothes-types">
        <img src="/assets/clothes.jpg" alt="기부 가능한 의류 이미지" />
        <div className="text-container">
          <h2>기부 가능한 의류 종류</h2>
          <p>기부할 수 있는 의류 종류는 다음과 같습니다:</p>
          <ul>
            <li>겨울용 의류 (코트, 패딩)</li>
            <li>여름용 의류 (반팔 티셔츠, 반바지)</li>
            <li>아동용 의류 (아이들을 위한 옷)</li>
          </ul>
          <button className="button" onClick={() => navigate("/clothing-donation")}>더 알아보기</button>
        </div>
      </div>

      {/* 섹션 3: 의류 수거 효과 */}
      <div className="section" id="benefits">
        <div className="text-container">
          <h2>의류 수거의 효과</h2>
          <p>의류를 기부하면 필요한 사람들에게 전달될 뿐 아니라, 환경에 미치는 영향을 줄이는 데 큰 기여를 합니다. 기부를 통해 자원을 재활용하고 사회적 가치를 창출하세요.</p>
        </div>
        <img src="/assets/benefits.jpg" alt="의류 수거 효과 이미지" />
      </div>

      <Footer />
    </div>
  );
};

export default Main;