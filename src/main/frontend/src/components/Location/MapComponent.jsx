import React, { useEffect } from "react";

const MapComponent = () => {
  useEffect(() => {
    const KAKAO_API_KEY = import.meta.env.VITE_KAKAO_API_KEY
    console.log(KAKAO_API_KEY); // Kakao API 키 출력

    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_API_KEY}&autoload=false`;
    script.async = true;
    document.head.appendChild(script);

    script.onload = () => {
      window.kakao.maps.load(() => {
        const mapContainer = document.getElementById("map");
        const mapOption = {
          center: new window.kakao.maps.LatLng(37.50972, 126.96355), // 동작구 중심 좌표
          level: 5,
        };
        const map = new window.kakao.maps.Map(mapContainer, mapOption);
      });
    };
  }, []);

  return <div id="map" className="map-container" style={{ width: "100%", height: "500px" }}></div>;
};

export default MapComponent;