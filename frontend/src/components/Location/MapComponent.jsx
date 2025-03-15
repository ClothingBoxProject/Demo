import React, { useEffect, useState } from "react";
import axios from "axios";
import Select from "react-select"; // react-select 임포트

const MapComponent = () => {
  const [locations, setLocations] = useState([]);
  const [selectedDongs, setSelectedDongs] = useState([]);
  const [filteredLocations, setFilteredLocations] = useState([]);
  const [selectBoxHeight, setSelectBoxHeight] = useState(0); // 셀렉트 박스 높이를 상태로 관리

  // 동 데이터를 설정하는 부분
  const dongOptions = [
    { value: "신대방1동", label: "신대방1동" },
    { value: "신대방2동", label: "신대방2동" },
    { value: "대방동", label: "대방동" },
    { value: "노량진1동", label: "노량진1동" },
    { value: "노량진2동", label: "노량진2동" },
    { value: "상도1동", label: "상도1동" },
    { value: "상도2동", label: "상도2동" },
    { value: "상도3동", label: "상도3동" },
    { value: "상도4동", label: "상도4동" },
    { value: "흑석동", label: "흑석동" },
    { value: "사당1동", label: "사당1동" },
    { value: "사당2동", label: "사당2동" },
    { value: "사당3동", label: "사당3동" },
    { value: "사당4동", label: "사당4동" },
    { value: "사당5동", label: "사당5동" },
  ];

  // 수거함 데이터를 가져오는 부분
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

  // 선택된 동에 해당하는 수거함만 필터링
  useEffect(() => {
    if (selectedDongs.length > 0) {
      const filtered = locations.filter((location) =>
        selectedDongs.includes(location.locationName)
      );
      setFilteredLocations(filtered);
    } else {
      setFilteredLocations(locations);
    }
  }, [selectedDongs, locations]);

  // 동 선택 변화 처리
  const handleDongChange = (selectedOptions) => {
    const selectedValues = selectedOptions.map((option) => option.value);
    setSelectedDongs(selectedValues);
  };

  // 셀렉트 박스 높이 동적으로 설정
  useEffect(() => {
    const selectBox = document.querySelector(".select-container");
    if (selectBox) {
      setSelectBoxHeight(selectBox.offsetHeight); // 셀렉트 박스 높이를 상태로 업데이트
    }
  }, [selectedDongs]);

  // 카카오 맵 관련 코드
  useEffect(() => {
    if (filteredLocations.length > 0) {
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${import.meta.env.VITE_KAKAO_API_KEY}&autoload=false&libraries=services`;
      script.async = true;
      document.head.appendChild(script);

      script.onload = () => {
        window.kakao.maps.load(() => {
          const mapContainer = document.getElementById("map");
          const mapOption = {
            center: new window.kakao.maps.LatLng(37.50972, 126.96355), // 초기 맵 중심
            level: 5,
          };
          const map = new window.kakao.maps.Map(mapContainer, mapOption);

          const markerPositions = [];

          filteredLocations.forEach((location) => {
            let position;
            if (location.latitude !== 0 && location.longitude !== 0) {
              position = new window.kakao.maps.LatLng(location.latitude, location.longitude);
              markerPositions.push(position);
              createMarker(position, location);
            } else {
              const geocoder = new window.kakao.maps.services.Geocoder();
              geocoder.addressSearch(location.address, (result, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                  position = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                  markerPositions.push(position);
                  createMarker(position, location);
                }
              });
            }
          });

          // 마커들 가운데 위치를 구하는 함수
          const getCenterOfMarkers = (positions) => {
            let latSum = 0;
            let lngSum = 0;
            positions.forEach((pos) => {
              latSum += pos.getLat();
              lngSum += pos.getLng();
            });
            const centerLat = latSum / positions.length;
            const centerLng = lngSum / positions.length;
            return new window.kakao.maps.LatLng(centerLat, centerLng);
          };

          // 마커들 가운데 위치 계산 후, 맵의 중심을 그 위치로 변경
          const center = getCenterOfMarkers(markerPositions);
          map.setCenter(center);

          function createMarker(position, location) {
            const marker = new window.kakao.maps.Marker({
              position: position,
              map: map,
            });

            const infowindow = new window.kakao.maps.InfoWindow({
              content: `
                <div style="padding:5px;font-size:14px;">
                  <div>${location.address}</div>
                  <button id="closeBtn" style="background: black; color: white; border: none; cursor: pointer;">X</button>
                </div>`,
            });

            window.kakao.maps.event.addListener(marker, "click", function () {
              infowindow.open(map, marker);
              const closeBtn = document.getElementById("closeBtn");
              if (closeBtn) {
                closeBtn.addEventListener("click", () => {
                  infowindow.close();
                });
              }
            });
          }
        });
      };
    }
  }, [filteredLocations]);

  return (
    <div className="page-container">
      {/* 동을 선택하세요 드롭다운 */}
      <div className="select-container">
        <Select
          isMulti
          options={dongOptions}
          onChange={handleDongChange}
          value={dongOptions.filter((option) => selectedDongs.includes(option.value))}
          placeholder="동을 선택하세요"
        />
      </div>

      {/* 카카오 맵 컨테이너 */}
      <div
        id="map"
        className="map-container"
        style={{ marginTop: `${selectBoxHeight + 20}px` }} // 셀렉트 박스 높이만큼 맵을 아래로 밀기
      ></div>
    </div>
  );
};

export default MapComponent;
