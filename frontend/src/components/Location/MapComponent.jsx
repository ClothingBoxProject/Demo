import React, { useEffect, useState } from "react";

// API 호출을 위한 axios 임포트 (혹은 fetch 사용 가능)
import axios from "axios";

const MapComponent = () => {
  const [locations, setLocations] = useState([]);

  // 백엔드에서 수거함 위치 데이터를 받아오는 함수
  useEffect(() => {
    const fetchData = async () => {
      try {
        // 백엔드 API에서 모든 수거함 데이터를 받아오기
        const response = await axios.get("http://localhost:8080/api/collection-bins");
        setLocations(response.data);  // 받은 데이터 저장
      } catch (error) {
        console.error("수거함 위치 데이터를 가져오는 데 오류가 발생했습니다.", error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (locations.length > 0) {
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_API_KEY}&autoload=false`;
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

          // locations 배열의 각 항목에 대해 마커 추가
          locations.forEach((location) => {
            const marker = new window.kakao.maps.Marker({
              position: new window.kakao.maps.LatLng(location.latitude, location.longitude),
              map: map,
            });

            const infowindow = new window.kakao.maps.InfoWindow({
              content: `<div style="padding:5px;font-size:14px;">${location.locationName}</div>`,
            });

            window.kakao.maps.event.addListener(marker, "click", function () {
              infowindow.open(map, marker);
            });
          });
        });
      };
    }
  }, [locations]); // locations 데이터가 변경될 때마다 맵 업데이트

  return <div id="map" className="map-container"></div>;
};

export default MapComponent;
