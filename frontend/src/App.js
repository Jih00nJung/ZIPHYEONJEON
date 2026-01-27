import { BrowserRouter, Routes, Route} from 'react-router-dom';

import Ziphyeonjeon from "./Ziphyeonjeon"
import Login from "./common/Login";
import Registration from "./common/Registration";

import RiskInquiry from "./RiskAnalysis/RiskInquiry";
import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";

// router 설정 페이지입니다. 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Ziphyeonjeon />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />} />

                {/* RiskAnalysis */}
                <Route path="/riskInquiry" element={<RiskInquiry />} />
                <Route path="/riskAnalysis" element={<RiskAnalysis />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;