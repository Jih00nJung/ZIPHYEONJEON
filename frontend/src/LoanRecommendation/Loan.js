import React from 'react';
import './Loan.css';
import MainLayout from "../layouts/MainLayout";
import Card from '../components/common/Card';
import Hero from '../components/common/Hero';
import Badge from '../components/common/Badge';
import Button from '../components/common/Button';

const Loan = () => {
    const govLoans = [
        { id: 1, tag: "신혼부부 전용", title: "버팀목 전세자금", rate: "1.2%", color: "blue" },
        { id: 2, tag: "청년 전용", title: "청년 맞춤형 전세", rate: "1.8%", color: "green" },
        { id: 3, tag: "저소득층 전용", title: "디딤돌 대출", rate: "2.1%", color: "yellow" }
    ];

    const bankLoans = [
        { id: 1, bank: "KB국민은행", logo: "KB", name: "KB Star 전세자금대출", rate: "3.42%", limit: "2억 2,200만원" },
        { id: 2, bank: "신한은행", logo: "SH", name: "쏠편한 전세대출", rate: "3.58%", limit: "5억원" },
        { id: 3, bank: "하나은행", logo: "HN", name: "하나 전세론", rate: "3.61%", limit: "4억원" },
        { id: 4, bank: "우리은행", logo: "WR", name: "iTouch 전세론", rate: "3.65%", limit: "2억 2,000만원" }
    ];

    return (
        <MainLayout>
            <div className="loan-page-v2">
                <div className="dashboard-container-v2">
                    <Hero
                        badgeText="AI 맞춤 대출 분석"
                        badgeIcon="auto_awesome"
                        title='나에게 가장 유리한 <span className="highlight">최적의 대출</span>을 찾아보세요'
                        subtitle="부동산 분석 결과와 나의 신용 정보를 바탕으로 최적의 금리를 추천합니다."
                    />

                    <div className="layout-grid-v2">
                        <div className="main-content-v2">
                            {/* Government Loans */}
                            <section className="section-v2">
                                <div className="section-header-v2">
                                    <h2 className="section-title-v2">
                                        <span className="material-symbols-outlined icon-blue">account_balance</span>
                                        정부 지원 대출 상품
                                    </h2>
                                    <Button variant="ghost" size="sm">전체보기</Button>
                                </div>
                                <div className="product-grid-v2">
                                    {govLoans.map(loan => (
                                        <Card key={loan.id} className="loan-card-v2" padding="24px">
                                            <Badge color={loan.color} variant="subtle" className="mb-12">{loan.tag}</Badge>
                                            <h3 className="loan-title-v2">{loan.title}</h3>
                                            <div className="loan-rate-box-v2">
                                                연 <span className="loan-rate-value-v2">{loan.rate}</span> ~
                                            </div>
                                            <ul className="benefit-list-v2">
                                                <li><span className="material-symbols-outlined">check</span> 최대 3억원 한도</li>
                                                <li><span className="material-symbols-outlined">check</span> 소득 합산 7.5천만원 이하</li>
                                            </ul>
                                            <Button variant="secondary" fullWidth className="mt-16">자격 확인</Button>
                                        </Card>
                                    ))}
                                </div>
                            </section>

                            {/* Bank Loans Comparison */}
                            <section className="section-v2">
                                <Card padding="0" overflow={false}>
                                    <div className="table-header-v2">
                                        <h2 className="section-title-v2">
                                            <span className="material-symbols-outlined icon-blue">list_alt</span>
                                            1금융권 실시간 금리 비교
                                        </h2>
                                        <span className="update-time-v2">2024.05.22 14:00 기준</span>
                                    </div>
                                    <div className="table-wrapper-v2">
                                        <table className="comparison-table-v2">
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
                                                        <td className="bank-info-v2">
                                                            <div className={`bank-logo-v2 logo-${loan.logo.toLowerCase()}`}>{loan.logo}</div>
                                                            <span className="bank-name-v2">{loan.bank}</span>
                                                        </td>
                                                        <td>{loan.name}</td>
                                                        <td className="rate-td-v2">{loan.rate}</td>
                                                        <td>{loan.limit}</td>
                                                        <td><span className="material-symbols-outlined">chevron_right</span></td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                        <button className="table-footer-btn-v2">
                                            12개 은행 상품 더보기
                                            <span className="material-symbols-outlined">expand_more</span>
                                        </button>
                                    </div>
                                </Card>
                            </section>
                        </div>

                        <aside className="sidebar-v2">
                            <Card className="sidebar-item-v2" padding="24px">
                                <div className="sidebar-header-v2">
                                    <span className="material-symbols-outlined icon-blue">analytics</span>
                                    나의 대출 역량
                                </div>
                                <div className="credit-score-v2">
                                    <span className="label-v2">현재 예상 신용점수</span>
                                    <div className="score-v2">
                                        <input type="text" defaultValue="950" />
                                        <span>점</span>
                                    </div>
                                    <p className="desc-v2">NICE / KCB 기준 예상 점수입니다.</p>
                                </div>
                                <Button variant="secondary" fullWidth icon="link" className="mb-8">MyData 연결하기</Button>
                                <div className="sidebar-links-v2">
                                    <div className="link-item-v2"><span className="material-symbols-outlined">calculate</span> 계산기</div>
                                    <div className="link-item-v2"><span className="material-symbols-outlined">history</span> 최근 조회</div>
                                </div>
                            </Card>

                            <Card className="sidebar-tip-v2" padding="24px">
                                <Badge color="blue" variant="solid" className="mb-12">PRO TIP</Badge>
                                <h4>대출 승인 확률을 높이는 법</h4>
                                <p>전세 보증 보험 가입이 가능한 매물을 선택하면 대출 금리 우대 혜택을 받을 수 있습니다.</p>
                                <Button variant="ghost" icon="arrow_forward" className="p-0 text-blue">분석 리포트 확인</Button>
                            </Card>
                        </aside>
                    </div>

                    <footer className="loan-footer-notice-v2">
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
