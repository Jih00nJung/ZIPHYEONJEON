import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Ziphyeonjeon from "./Ziphyeonjeon"
import Login from "./common/Login";
import Registration from "./common/Registration";

import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";
import RiskReport from "./RiskAnalysis/RiskReport";
import Loan from "./LoanRecommendation/Loan";
import PriceSearch from "./PriceSearch/PriceSearch";

// router 설정 페이지입니다. 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Ziphyeonjeon />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />} />

                {/* Risk Analysis */}
                <Route path="/riskanalysis" element={<RiskAnalysis />} />
                <Route path="/riskreport" element={<RiskReport />} />

                {/* Loan Recommendation */}
                {/* Loan Recommendation */}
                <Route path="/loan" element={<Loan />} />

                {/* Price Search (P-001 ~ P-008) */}
                <Route path="/price-search" element={<PriceSearch />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;