// AuthContext.jsx
import { createContext, useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getAccessToken,
  setAccessToken,
  clearAccessToken,
  isAccessTokenExpired,
} from "../utils/db";
import axios from "axios";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authStatus, setAuthStatus] = useState("unknown");
  const navigate = useNavigate();
  const location = useLocation();
  let isRefreshing = false;

  useEffect(() => {
    const checkAuthStatus = async () => {
      console.log("[AuthContext] 🔍 Checking auth status...");
      const token = await getAccessToken();
      if (!token) {
        console.log("[AuthContext] ❌ No token. Logged out.");
        setAuthStatus("loggedOut");
        return ;
      }

      const expired = await isAccessTokenExpired();

      console.log(`[AuthContext] 🔐 Token: ${token}`);
      console.log(`[AuthContext] ⏰ Expired: ${expired}`);

      if (!expired) {
        console.log("[AuthContext] ✅ Token valid. Logged in.");
        setAuthStatus("loggedIn");
      } else {
          console.log("[AuthContext] 🔄 Token expired. Trying refresh...");
          const newToken = await refreshAccessToken();
          if (!newToken) {
            console.log("[AuthContext] 🔁 Refresh failed. Logging out...");
          }
      }
    };

    if (authStatus === "unknown") {
      console.log("[AuthContext] 🔄 AuthStatus is 'unknown', checking...");
      checkAuthStatus();
    }
  }, [authStatus]);

  const refreshAccessToken = async () => {
    if (isRefreshing) return null;
    isRefreshing = true;
    try {
      console.log("[AuthContext] 🛰️ Sending refresh request...");
      const response = await axios.post("/api/auth/refresh", null, {
        withCredentials: true,
      });

      const { accessToken } = response.data;
      console.log("[AuthContext] 🔓 Refresh succeeded. New token set.");
      await setAccessToken(accessToken);
      setAuthStatus("loggedIn");

      return accessToken;
    } catch (error) {
      console.error("[AuthContext] ❗ Error refreshing token:", error);
      if (authStatus !== "loggingOut") {
        logout();
      }
      return null;
    }
  };

  const logout = async () => {
    console.log("[AuthContext] 🚪 Logging out...");
    if(authStatus=="loggingOut" || authStatus=="loggedOut"){
      return;
    }
    setAuthStatus("loggingOut");
    try {
      await axios.delete("/api/auth/logout", {
        withCredentials: true,
      });
      console.log("[AuthContext] 🧹 Logout request sent.");
    } catch (error) {
      console.error("[AuthContext] ❗ Logout error:", error);
    } finally {
      await clearAccessToken();
      handleLoggedOut();
    }
  };

  const handleLoggedOut = () => {
    console.log("[AuthContext] 👋 Logged out. Redirecting to login...");
    setAuthStatus("loggedOut");
    if (location.pathname !== "/login") {
      navigate("/login");
    }
  };

  return (
    <AuthContext.Provider value={{ authStatus, logout, setAuthStatus }}>
      {children}
    </AuthContext.Provider>
  );
};
