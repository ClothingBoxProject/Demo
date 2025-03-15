import React, { useEffect, useState } from "react";
import axios from "axios";

const MapComponent = () => {
  const [locations, setLocations] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/collection-bins");
        setLocations(response.data);
      } catch (error) {
        console.error("수거함 위치 데이터를 가져오는 데 오류가 발생했습니다.", error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (locations.length > 0) {
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_API_KEY}&autoload=false&libraries=services`;
      script.async = true;
      document.head.appendChild(script);

      script.onload = () => {
        window.kakao.maps.load(() => {
          const mapContainer = document.getElementById("map");
          const mapOption = {
            center: new window.kakao.maps.LatLng(37.50972, 126.96355),
            level: 5,
          };
          const map = new window.kakao.maps.Map(mapContainer, mapOption);

          locations.forEach((location) => {
            let position;
            if (location.latitude !== 0 && location.longitude !== 0) {
              position = new window.kakao.maps.LatLng(location.latitude, location.longitude);
              createMarker(position, location);
            } else {
              // 주소로 좌표 변환
              const geocoder = new window.kakao.maps.services.Geocoder();
              geocoder.addressSearch(location.address, (result, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                  position = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                  createMarker(position, location);
                }
              });
            }
          });

          // 위도 경도가 0일 때, 주소를 통해 좌표를 구하고 마커를 생성하는 함수
          function createMarker(position, location) {
            const marker = new window.kakao.maps.Marker({
              position: position,
              map: map,
            });

            const infowindow = new window.kakao.maps.InfoWindow({
              content: `<div style="padding:5px;font-size:14px;">${location.locationName}</div>`,
            });

            window.kakao.maps.event.addListener(marker, "click", function () {
              infowindow.open(map, marker);
            });
          }
        });
      };
    }
  }, [locations]);

  return <div id="map" className="map-container"></div>;
};

export default MapComponent;
