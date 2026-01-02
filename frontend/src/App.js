import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import RiskAnalysis from "./RiskAnalysis/RiskAnalysis";

// router 설정 페이지입니다. 여기서 메인페이지 만들지 마세요
// 그냥 라우터 연결만!!

function App() {
    return (
        <BrowserRouter>
            <nav>
                <Link to={"/"}><div>홈 화면</div></Link>
                <Link to="/RiskAnalysis">위험 분석 페이지로 이동</Link>
            </nav>

            <Routes>
                <Route path="/" element={<h2>홈 화면</h2>} />

                {/*RiskAnalysis router*/}
                <Route path="/RiskAnalysis" element={<RiskAnalysis />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;