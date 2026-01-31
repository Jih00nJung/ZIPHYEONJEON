import { BrowserRouter, Routes, Route} from 'react-router-dom';

import Ziphyeonjeon from "./Ziphyeonjeon"
import Login from "./common/Login";
import Registration from "./common/Registration";

import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";
import RiskReport from "./RiskAnalysis/RiskReport";

// router 설정 페이지입니다. 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Ziphyeonjeon />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />} />

                {/* RiskReport */}
                <Route path="/riskanalysis" element={<RiskAnalysis />} />
                <Route path="/riskreport" element={<RiskReport />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;