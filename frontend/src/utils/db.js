import { openDB } from "idb"; // IndexedDB 라이브러리 사용

// IndexedDB 데이터베이스 생성
const dbPromise = openDB("authDB", 1, {
  upgrade(db) {
    if (!db.objectStoreNames.contains("auth")) {
      db.createObjectStore("auth"); // "auth" 저장소 생성
    }
  },
});

// AccessToken 저장
export const setAccessToken = async (token) => {
  const db = await dbPromise;
  await db.put("auth", token, "accessToken");
};

// AccessToken 가져오기
export const getAccessToken = async () => {
  const db = await dbPromise;
  return await db.get("auth", "accessToken");
};

// AccessToken 삭제 (로그아웃 시)
export const clearAccessToken = async () => {
  const db = await dbPromise;
  await db.delete("auth", "accessToken");
};

// JWT 디코딩 함수 (Base64 -> JSON 변환)
const decodeJWT = (token) => {
  try {
    const base64Url = token.split(".")[1]; // JWT의 payload 부분
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(atob(base64));
  } catch (error) {
    console.error("JWT 디코딩 실패:", error);
    return null;
  }
};

// AccessToken 만료 시간 가져오기
export const getAccessTokenExpiry = async () => {
  const token = await getAccessToken();
  if (!token) return null; // 토큰이 없으면 null 반환

  const decoded = decodeJWT(token);
  return decoded ? decoded.exp * 1000 : null; // UNIX timestamp를 밀리초(ms)로 변환
};

// AccessToken 만료 여부 확인
export const isAccessTokenExpired = async () => {
  const expiresAt = await getAccessTokenExpiry();
  if (!expiresAt) return true; // 토큰이 없으면 만료된 것으로 처리

  return Date.now() >= expiresAt; // 현재 시간이 만료 시간보다 크면 true
};