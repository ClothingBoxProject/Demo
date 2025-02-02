import React, { useEffect } from "react";
/* Kakao API 키(=YOUR_KAKAO_API_KEY)를 본인의 키로 변경하여 실제로 사용*/
const MapComponent = () => {
  useEffect(() => {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_API_KEY&autoload=false`;
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

        // ✅ 동작구 의류 수거함 위치 마커
        const locations = [
          { lat: 37.51234, lng: 126.96789, name: "동작구청 근처 수거함" },
          { lat: 37.50567, lng: 126.96123, name: "이수역 근처 수거함" },
          { lat: 37.52012, lng: 126.97156, name: "상도역 근처 수거함" },
        ];

        locations.forEach((location) => {
          const marker = new window.kakao.maps.Marker({
            position: new window.kakao.maps.LatLng(location.lat, location.lng),
            map: map,
          });

          const infowindow = new window.kakao.maps.InfoWindow({
            content: `<div style="padding:5px;font-size:14px;">${location.name}</div>`,
          });

          window.kakao.maps.event.addListener(marker, "click", function () {
            infowindow.open(map, marker);
          });
        });
      });
    };
  }, []);

  return <div id="map" className="map-container"></div>;
};

export default MapComponent;
