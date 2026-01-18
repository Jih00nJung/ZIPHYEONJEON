import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';

import Ziphyeonjeon from "./Ziphyeonjeon"
import RiskInquiry from "./RiskAnalysis/RiskInquiry";
import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";

// router 설정 페이지입니다. 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Ziphyeonjeon />} />

                {/* RiskAnalysis */}
                <Route path="/riskInquiry" element={<RiskInquiry />} />
                <Route path="/riskAnalysis" element={<RiskAnalysis />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;