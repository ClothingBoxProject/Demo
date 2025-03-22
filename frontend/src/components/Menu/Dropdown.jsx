import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/MenuCSS/Dropdown.css";

const Dropdown = ({ title, items, mainLink }) => {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();

  const handleMainClick = () => {
    navigate(mainLink);
  };

  const handleItemClick = (link) => {
    navigate(link);
  };

  return (
    <div
      className="dropdown"
      onMouseEnter={() => setIsOpen(true)}
      onMouseLeave={() => setIsOpen(false)}
    >
      <button className="dropdown-main" onClick={handleMainClick}>
        {title}
      </button>

      {isOpen && (
        <div className="dropdown-content">
          {items.map((item, index) => (
            <button
              key={index}
              className="dropdown-item"
              onClick={() => handleItemClick(item.link)}
            >
              {item.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default Dropdown;