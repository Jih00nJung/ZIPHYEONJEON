import React from 'react';
import './RiskReport.css';
import MainLayout from "../layouts/MainLayout";
import Card from '../components/common/Card';
import Hero from '../components/common/Hero';
import Badge from '../components/common/Badge';
import Button from '../components/common/Button';

const RiskReport = () => {
    return (
        <MainLayout>
            <div className="risk-report-page-v2">
                <div className="dashboard-container-v2">

                    {/* Process Steps */}
                    <section className="process-section-v2">
                        <div className="progress-track-v2">
                            <div className="progress-fill-v2" style={{ width: '55%' }}></div>
                        </div>
                        <div className="steps-container-v2">
                            <Step icon="location_on" label="주소<br/>분석" active />
                            <Step icon="fact_check" label="등기부등본<br/>확인" active />
                            <Step icon="analytics" label="위험성<br/>평가" current />
                            <Step icon="description" label="계약서<br/>초안" disabled />
                            <Step icon="verified" label="최종<br/>평가" disabled />
                        </div>
                    </section>

                    <Hero
                        title="서울특별시 강남구 테헤란로 123"
                        subtitle="삼성 래미안 아파트 105동 202호"
                        actions={
                            <>
                                <Button variant="outline">공유하기</Button>
                                <Button variant="primary">리포트 다운로드</Button>
                            </>
                        }
                    />

                    <div className="layout-grid-v2">
                        <div className="main-content-v2">
                            <Card className="safety-card-v2" padding="40px">
                                <div className="safety-flex-v2">
                                    <div className="score-circle-v2">
                                        <svg viewBox="0 0 100 100">
                                            <circle cx="50" cy="50" r="45" stroke="#F1F5F9" strokeWidth="8" fill="none" />
                                            <circle cx="50" cy="50" r="45" stroke="#34D399" strokeWidth="8" fill="none"
                                                strokeDasharray="283" strokeDashoffset="42" strokeLinecap="round" />
                                        </svg>
                                        <div className="score-info-v2">
                                            <span className="val-v2">85</span>
                                            <span className="lbl-v2">SAFETY SCORE</span>
                                        </div>
                                    </div>
                                    <div className="score-desc-v2">
                                        <Badge color="green" variant="subtle" className="mb-12">우수 등급</Badge>
                                        <h2>계약 안전 등급: 우수</h2>
                                        <p>서울시 상위 15% 이내의 안전 등급입니다. 근저당권이 깨끗하며 위반건축물 이력이 없습니다.</p>
                                        <div className="mini-stats-v2">
                                            <SmallProg label="권리 분석" val={95} color="blue" />
                                            <SmallProg label="시장 분석" val={78} color="yellow" />
                                            <SmallProg label="물건 상태" val={88} color="green" />
                                        </div>
                                    </div>
                                </div>
                            </Card>

                            <div className="details-grid-v2">
                                <Card className="detail-item-v2" padding="24px">
                                    <h4>등기부 권리 분석</h4>
                                    <div className="info-row-v2"><span>소유주 확인</span><Badge color="green">김민수 (일치)</Badge></div>
                                    <div className="mt-20">
                                        <div className="info-row-v2"><span>부채 비율</span><span>0 KRW</span></div>
                                        <div className="progress-bar-v2"><div className="fill-v2 green" style={{ width: '2%' }}></div></div>
                                    </div>
                                </Card>

                                <Card className="detail-item-v2" padding="24px">
                                    <h4>시장 가치 평가</h4>
                                    <div className="price-v2">12.5억 <small>KRW</small></div>
                                    <div className="info-row-v2"><span>전세가율</span><span className="text-yellow">78%</span></div>
                                    <div className="progress-bar-v2 multi">
                                        <div className="segment safe" style={{ width: '60%' }}></div>
                                        <div className="segment caution" style={{ width: '18%' }}></div>
                                    </div>
                                </Card>

                                <Card className="detail-item-v2" padding="24px">
                                    <h4>건물 상태 정보</h4>
                                    <div className="mini-grid-v2">
                                        <div className="info-box-v2"><span>건물 연식</span><strong>12년</strong></div>
                                        <div className="info-box-v2"><span>총 세대수</span><strong>842세대</strong></div>
                                    </div>
                                </Card>
                            </div>
                        </div>

                        <aside className="sidebar-v2">
                            <Card padding="24px" className="sidebar-card-v2">
                                <h3>위험 요약 리포트</h3>
                                <div className="summary-list-v2">
                                    <Summary icon="verified" title="소유주 검증 완료" desc="신분증 일치" color="green" />
                                    <Summary icon="security" title="보증 보험 가능" desc="HUG 조건 충족" color="green" />
                                    <Summary icon="trending_up" title="시세 변동 경고" desc="최근 거래량 증가" color="yellow" />
                                </div>
                                <Button variant="dark" fullWidth className="mt-20">상세 리포트 보기</Button>
                            </Card>

                            <Card padding="24px" className="sidebar-pro-v2 dark-card">
                                <Badge color="blue" variant="solid" className="mb-16">PRO TIP</Badge>
                                <h4>대항력 확보 방법</h4>
                                <p>전입신고와 확정일자는 이사 당일 반드시 완료해야 보증금을 지킬 수 있습니다.</p>
                                <Button variant="ghost" icon="arrow_forward" className="text-blue p-0">가이드북 보기</Button>
                            </Card>
                        </aside>
                    </div>
                </div>
            </div>
        </MainLayout>
    );
};

/* Internal Helpers */
const Step = ({ icon, label, active, current, disabled }) => (
    <div className={`step-v2 ${active ? 'active' : ''} ${current ? 'current' : ''} ${disabled ? 'disabled' : ''}`}>
        <div className="circle-v2"><span className="material-symbols-outlined">{icon}</span></div>
        <p dangerouslySetInnerHTML={{ __html: label }}></p>
    </div>
);

const SmallProg = ({ label, val, color }) => (
    <div className="small-stat-v2">
        <div className="lbl-row-v2"><span>{label}</span><strong className={`text-${color}`}>{val}%</strong></div>
        <div className="bar-v2"><div className={`fill-v2 ${color}`} style={{ width: `${val}%` }}></div></div>
    </div>
);

const Summary = ({ icon, title, desc, color }) => (
    <div className={`summary-item-v2 ${color}`}>
        <div className="icon-v2"><span className="material-symbols-outlined">{icon}</span></div>
        <div className="txt-v2"><strong>{title}</strong><p>{desc}</p></div>
    </div>
);

export default RiskReport;