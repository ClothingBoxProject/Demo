import React, { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";

import "../../styles/ClothingCSS/ProductCard.css";

import clothImage from "../../assets/cloth.jpg";

const ProductCard = ({ item }) => {
  const navigate = useNavigate();
  const cardRef = useRef(null);

  // IntersectionObserver를 사용하여 스크롤 시 나타나도록 구현
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("visible");
          } else {
            entry.target.classList.remove("visible");
          }
        });
      },
      { threshold: 0.5 }
    );

    if (cardRef.current) {
      observer.observe(cardRef.current);
    }

    return () => {
      if (cardRef.current) {
        observer.unobserve(cardRef.current);
      }
    };
  }, []);

  return (
    <div
      ref={cardRef}
      className="product-card"
      onClick={() => navigate(`/product/${item.id}`)}
    >
      {/* ClothImage를 사용하도록 수정 */}
      <img src={clothImage} alt={item.name} />
      <h3>{item.name} | {item.desc}</h3>
    </div>
  );
};

export default ProductCard;
