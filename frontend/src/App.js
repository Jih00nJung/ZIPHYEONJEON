import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";
import Ziphyeonjeon from "./Ziphyeonjeon"

// router 설정 페이지입니다. 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Ziphyeonjeon />} />

                {/*RiskAnalysis router*/}
                <Route path="/RiskAnalysis" element={<RiskAnalysis />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;