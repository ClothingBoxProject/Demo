import React, { useEffect, useState } from 'react';
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";

import Login from "./components/AuthForm/Login";
import Register from "./components/AuthForm/Register";
import FindId from "./components/AuthForm/FindId";
import ResetPassword from "./components/AuthForm/ResetPassword";
import FindIdResult from "./components/AuthForm/FindIdResult";
import SearchResult from "./components/Clothing/SearchResult";
import Main from "./components/Main/Main";
import ClothingDonation from "./components/Clothing/ClothingDonation";
import ProductDetail from "./components/Clothing/ProductDetail";
import SuggestionForm from "./components/SuggestionForm/SuggestionForm";
import MyPage from "./components/User/MyPage";
import ClothingSearch from "./components/Clothing/ClothingSearch";
import LocationBin from "./components/Location/LocationBin";
import ClothingImpact from "./components/Clothing/ClothingImpact";
import { AuthProvider } from "./contexts/AuthContext";

const App = () => {
  return (
    <Router>
    <AuthProvider>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/find-id" element={<FindId />} />
        <Route path="/find-id-result" element={<FindIdResult />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/clothing-donation" element={<ClothingDonation />} />
        <Route path="/product/:id" element={<ProductDetail />} />
        <Route path="/search/:query" element={<SearchResult />} />
        <Route path="/suggestion" element={<SuggestionForm />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/clothing-search" element={<ClothingSearch />} />
        <Route path="/box-location" element={<LocationBin />} />
        <Route path="/clothing-impact" element={<ClothingImpact />} />
      </Routes>
    </AuthProvider>
    </Router>
  );
};

export default App
