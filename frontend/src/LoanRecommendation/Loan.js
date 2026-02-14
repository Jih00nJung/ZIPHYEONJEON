import React, {useEffect, useState} from 'react';
import './Loan.css';
import MainLayout from "../layouts/MainLayout";
import Card from '../components/common/Card';
import Hero from '../components/common/Hero';
import Badge from '../components/common/Badge';
import Button from '../components/common/Button';
import axios from "axios";

const Loan = () => {
    const govLoans = [
        {id: 1, tag: "신혼부부 전용", title: "버팀목 전세자금", rate: "1.2%", color: "blue"},
        {id: 2, tag: "청년 전용", title: "청년 맞춤형 전세", rate: "1.8%", color: "green"},
        {id: 3, tag: "저소득층 전용", title: "디딤돌 대출", rate: "2.1%", color: "yellow"}
    ];

    const bankLoans = [
        {id: 1, bank: "KB국민은행", logo: "KB", name: "KB Star 전세자금대출", rate: "3.42%", limit: "2억 2,200만원"},
        {id: 2, bank: "신한은행", logo: "SH", name: "쏠편한 전세대출", rate: "3.58%", limit: "5억원"},
        {id: 3, bank: "하나은행", logo: "HN", name: "하나 전세론", rate: "3.61%", limit: "4억원"},
        {id: 4, bank: "우리은행", logo: "WR", name: "iTouch 전세론", rate: "3.65%", limit: "2억 2,000만원"}
    ];

    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchLoans = async () => {
            try {
                setLoading(true);
                const response = await axios.get('http://localhost:8080/api/loan/list');
                console.log(response.data);
                setLoans(response.data);
            } catch (err) {
                console.error("Loan.js 실패:", err);
                setError("대출 정보를 불러오는 중 오류가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchLoans();
    }, []);

    if (loading) return <div>데이터를 불러오는 중입니다...</div>;
    if (error) return <div>{error}</div>;

    return (
        <MainLayout>
            <div className="loan-page-loan">
                <div className="dashboard-container-loan">
                    <Hero
                        badgeText="맞춤 대출 분석"
                        badgeIcon="auto_awesome"
                        title='나에게 가장 유리한 최적의 대출을 찾아보세요'
                        subtitle="Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                    />

                    <div className="layout-grid-loan">
                        <div className="main-content-loan">
                            {/* Government Loans */}
                            <section className="section-loan">
                                <div className="section-header-loan">
                                    <h2 className="section-title-loan">
                                        <span className="material-symbols-outlined icon-blue">account_balance</span>
                                        정부 지원 대출 상품
                                    </h2>
                                    <Button variant="ghost" size="sm">{loans.length}개 상품 전체보기</Button>
                                </div>
                                <div className="product-grid-loan">
                                    {govLoans.map(loan => (
                                        <Card key={loan.id} className="loan-card-loan" padding="24px">
                                            <Badge color={loan.color} variant="subtle"
                                                   className="mb-12">{loan.tag}</Badge>
                                            <h3 className="loan-title-loan">{loan.title}</h3>
                                            <div className="loan-rate-box-loan">
                                                연 <span className="loan-rate-value-loan">{loan.rate}</span> ~
                                            </div>
                                            <ul className="benefit-list-loan">
                                                <li><span className="material-symbols-outlined">check</span> 최대 3억원 한도
                                                </li>
                                                <li><span className="material-symbols-outlined">check</span> 소득 합산
                                                    7.5천만원 이하
                                                </li>
                                            </ul>
                                            <Button variant="secondary" fullWidth className="mt-16">자격 확인</Button>
                                        </Card>
                                    ))}
                                </div>
                            </section>

                            {/* Bank Loans Comparison */}
                            <section className="section-loan">
                                <Card padding="0" overflow={false}>
                                    <div className="table-header-loan">
                                        <h2 className="section-title-loan">
                                            <span className="material-symbols-outlined icon-blue">list_alt</span>
                                            1금융권 실시간 금리 비교
                                        </h2>
                                        <span className="update-time-loan">2024.05.22 14:00 기준</span>
                                    </div>
                                    <div className="table-wrapper-loan">
                                        <table className="comparison-table-loan">
                                            <thead>
                                            <tr>
                                                <th>은행사</th>
                                                <th>상품명</th>
                                                <th>최저 금리</th>
                                                <th>최대 한도</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {bankLoans.map(loan => (
                                                <tr key={loan.id}>
                                                    <td className="bank-info-loan">
                                                        <div
                                                            className={`bank-logo-loan logo-${loan.logo.toLowerCase()}`}>{loan.logo}</div>
                                                        <span className="bank-name-loan">{loan.bank}</span>
                                                    </td>
                                                    <td>{loan.name}</td>
                                                    <td className="rate-td-loan">{loan.rate}</td>
                                                    <td>{loan.limit}</td>
                                                    <td><span className="material-symbols-outlined">chevron_right</span>
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                        <button className="table-footer-btn-loan">
                                            12개 은행 상품 더보기
                                            <span className="material-symbols-outlined">expand_more</span>
                                        </button>
                                    </div>
                                </Card>
                            </section>
                        </div>

                        <aside className="sidebar-loan">
                            <Card className="sidebar-item-loan" padding="24px">
                                <div className="sidebar-header-loan">
                                    <span className="material-symbols-outlined icon-blue">analytics</span>
                                    나의 대출 역량
                                </div>
                                <div className="credit-score-loan">
                                    <span className="label-loan">현재 예상 신용점수</span>
                                    <div className="score-loan">
                                        <input type="text" defaultValue="950"/>
                                        <span>점</span>
                                    </div>
                                    <p className="desc-loan">NICE / KCB 기준 예상 점수입니다.</p>
                                </div>
                                <Button variant="secondary" fullWidth icon="link" className="mb-8">MyData 연결하기</Button>
                                <div className="sidebar-links-loan">
                                    <div className="link-item-loan"><span
                                        className="material-symbols-outlined">calculate</span> 계산기
                                    </div>
                                    <div className="link-item-loan"><span
                                        className="material-symbols-outlined">history</span> 최근 조회
                                    </div>
                                </div>
                            </Card>

                            <Card className="sidebar-tip-loan" padding="24px">
                                <Badge color="blue" variant="solid" className="mb-12">PRO TIP</Badge>
                                <h4>대출 승인 확률을 높이는 법</h4>
                                <p>전세 보증 보험 가입이 가능한 매물을 선택하면 대출 금리 우대 혜택을 받을 수 있습니다.</p>
                                <Button variant="ghost" icon="arrow_forward" className="p-0 text-blue">분석 리포트
                                    확인</Button>
                            </Card>
                        </aside>
                    </div>

                    <footer className="loan-footer-notice-loan">
                        <h4>꼭 확인하세요!</h4>
                        <ul>
                            <li>표시된 금리는 최저금리 기준이며, 개인의 환경에 따라 달라질 수 있습니다.</li>
                            <li>정부지원 상품은 관련 법규 변화에 따라 상시 변경될 수 있습니다.</li>
                            <li>본 서비스에서 제공하는 정보는 참고용이며, 최종 계약은 금융기관에서 진행하시기 바랍니다.</li>
                        </ul>
                    </footer>
                </div>
            </div>
        </MainLayout>
    );
};

export default Loan;
