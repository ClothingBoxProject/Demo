import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/MenuCSS/Dropdown.css";

const Dropdown = ({ title, items, mainLink }) => {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const handleMainClick = (e) => {
    e.preventDefault(); // 기본 동작 방지
    navigate(mainLink); // 메인 링크로 이동
  };

  return (
    <div
      className="dropdown"
      onMouseEnter={() => setIsOpen(true)}
      onMouseLeave={() => setIsOpen(false)}
    >
      {/* 기부 가능한 의류 클릭 시 /clothing-donation 이동 */}
      <a href={mainLink} onClick={handleMainClick}>
        {title}
      </a>

      {isOpen && (
        <div className="dropdown-content">
          {items.map((item, index) => (
            <a key={index} href={item.link}>{item.label}</a>
          ))}
        </div>
      )}
    </div>
  );
};

export default Dropdown;
