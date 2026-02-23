import React, {useEffect} from 'react';
import './RiskReport.css';
import MainLayout from "../layouts/MainLayout";
import Card from '../components/common/Card';
import Hero from '../components/common/Hero';
import Badge from '../components/common/Badge';
import Button from '../components/common/Button';
import {useLocation, useNavigate} from "react-router-dom";

const RiskReport = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    const resultData = location.state?.result;

    if (!resultData) {
        return (
            <MainLayout>
                <div style={{padding: "50px", textAlign: "center"}}>
                    <Hero
                        subtitle={"분석 데이터가 없습니다. 다시 시도해 주세요."}>
                    </Hero>
                    <Button variant="outline" onClick={() => navigate('/riskanalysis')}>돌아가기</Button>
                </div>
            </MainLayout>
        );
    }

    const averageScore = Math.round(
        ((resultData.disasterData.data[0].score || 0) +
            (resultData.buildingData.data[0].score || 0) +
            (resultData.ocrData.data[0].score || 0)) / 3
    )

    const getGrade = (score) => {
        if (score >= 90) return {text: "우수 등급", color: "green"};
        if (score >= 70) return {text: "보통 등급", color: "yellow"};
        return {text: "주의 등급", color: "red"};
    };

    const grade = getGrade(averageScore);

    const circumference = 283;

    const strokeDashoffset = circumference - (averageScore / 100) * circumference;

    return (
        <MainLayout>
            <div className="risk-report-page-risk">
                <div className="dashboard-container-risk">

                    {/* Process Steps */}
                    <section className="process-section-risk">
                        <div className="progress-track-risk">
                            <div className="progress-fill-risk" style={{width: '55%'}}></div>
                        </div>
                        <div className="steps-container-risk">
                            <Step icon="location_on" label="주소<br/>분석" active/>
                            <Step icon="fact_check" label="등기부등본<br/>확인" active/>
                            <Step icon="analytics" label="위험성<br/>평가" current/>
                            <Step icon="description" label="계약서<br/>초안" disabled/>
                            <Step icon="verified" label="최종<br/>평가" disabled/>
                        </div>
                    </section>

                    <Hero
                        title={resultData.address}
                        actions={
                            <>
                                <Button variant="outline">공유하기</Button>
                                <Button variant="primary">리포트 다운로드</Button>
                            </>
                        }
                    />

                    <div className="layout-grid-risk">
                        <div className="main-content-risk">
                            <Card className="safety-card-risk" padding="40px">
                                <div className="safety-flex-risk">
                                    <div className="score-circle-risk">
                                        <svg viewBox="0 0 100 100">
                                            <circle
                                                cx="50" cy="50" r="45"
                                                stroke="#F1F5F9" strokeWidth="8" fill="none"
                                            />
                                            <circle
                                                cx="50" cy="50" r="45"
                                                stroke={grade.color} strokeWidth="8" fill="none"
                                                strokeDasharray={circumference}
                                                strokeDashoffset={strokeDashoffset}
                                                strokeLinecap="round"
                                                style={{transition: 'stroke-dashoffset 0.5s ease-in-out'}}
                                            />
                                        </svg>
                                        <div className="score-info-risk">
                                            <span className="val-risk">{averageScore}</span>
                                            <span className="lbl-risk">SAFETY SCORE</span>
                                        </div>
                                    </div>
                                    <div className="score-desc-risk">
                                        <Badge color={grade.color} variant="subtle"
                                               className="mb-12">{grade.text}</Badge>
                                        {resultData.disasterData?.data[0]?.disasterData?.length === 0 ? (
                                            <h3>재해 분석: 조회된 재해 정보가 없습니다.</h3>
                                        ) : (
                                            <>
                                                <h3>재해 분석</h3>
                                                {resultData.disasterData.data[0].disasterData.map((item, index) => (
                                                    <div key={index} className="risk-item"
                                                         style={{marginBottom: '10px'}}>
                                                        <strong>{item.DST_SE_NM}</strong> ({item.REG_YMD})
                                                    </div>
                                                ))}
                                            </>
                                        )}
                                        <br/>
                                        <h3>건축물 분석: {resultData.buildingData.data[0].riskFactors}</h3>
                                        <br/>
                                        <h3>등기부 분석: {resultData.ocrData.data[0].gapguIssue}</h3>
                                        {resultData.ocrData?.data?.[0]?.riskFactors?.map((factor, index) => (
                                            <div key={index} className="risk-item">
                                                {factor}
                                            </div>
                                        ))}
                                        <p></p>
                                        <div className="mini-stats-risk">
                                            <SmallProg label="재해" val={resultData.disasterData.data[0].score}
                                                       color="blue"/>
                                            <SmallProg label="건축물" val={resultData.buildingData.data[0].score}
                                                       color="yellow"/>
                                            <SmallProg label="등기" val={resultData.ocrData.data[0].score} color="green"/>
                                        </div>
                                    </div>
                                </div>
                            </Card>

                            <div className="details-grid-risk">
                                <Card className="detail-item-risk" padding="24px">
                                    <h4>등기부 권리 분석</h4>
                                    <div className="info-row-risk"><span>소유주 확인</span><Badge color="green">000
                                        (일치)</Badge></div>
                                    <div className="mt-20">
                                        <div className="info-row-risk"><span>부채 비율</span><span>0 KRW</span></div>
                                        <div className="progress-bar-risk">
                                            <div className="fill-risk green" style={{width: '2%'}}></div>
                                        </div>
                                    </div>
                                </Card>

                                <Card className="detail-item-risk" padding="24px">
                                    <h4>시장 가치 평가</h4>
                                    <div className="price-risk">{(resultData.buildingData.data[0].housePrice) / 100000000}억 <small>KRW</small></div>
                                    <div className="info-row-risk"><span>전세가율</span><span
                                        className="text-yellow">00%</span></div>
                                    <div className="progress-bar-risk multi">
                                        <div className="segment safe" style={{width: '60%'}}></div>
                                        <div className="segment caution" style={{width: '18%'}}></div>
                                    </div>
                                </Card>

                                <Card className="detail-item-risk" padding="24px">
                                    <h4>건물 상태 정보</h4>
                                    <div className="mini-grid-risk">
                                        <div className="info-box-risk"><span>건물 연식</span><strong>00년</strong></div>
                                        <div className="info-box-risk"><span>총 세대수</span><strong>000세대</strong></div>
                                    </div>
                                </Card>
                            </div>
                        </div>

                        <aside className="sidebar-risk">
                            <Card padding="24px" className="sidebar-card-risk">
                                <h3>위험 요약 리포트</h3>
                                <div className="summary-list-risk">
                                    <Summary icon="verified" title="소유주 검증 완료" desc="신분증 일치" color="green"/>
                                    <Summary icon="security" title="보증 보험 가능" desc="HUG 조건 충족" color="green"/>
                                    <Summary icon="trending_up" title="시세 변동 경고" desc="최근 거래량 증가" color="yellow"/>
                                </div>
                                <Button variant="dark" fullWidth className="mt-20">상세 리포트 보기</Button>
                            </Card>

                            <Card padding="24px" className="sidebar-pro-risk dark-card">
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
const Step = ({icon, label, active, current, disabled}) => (
    <div className={`step-risk ${active ? 'active' : ''} ${current ? 'current' : ''} ${disabled ? 'disabled' : ''}`}>
        <div className="circle-risk"><span className="material-symbols-outlined">{icon}</span></div>
        <p dangerouslySetInnerHTML={{__html: label}}></p>
    </div>
);

const SmallProg = ({label, val, color}) => (
    <div className="small-stat-risk">
        <div className="lbl-row-risk"><span>{label}</span><strong className={`text-${color}`}>{val}%</strong></div>
        <div className="bar-risk">
            <div className={`fill-risk ${color}`} style={{width: `${val}%`}}></div>
        </div>
    </div>
);

const Summary = ({icon, title, desc, color}) => (
    <div className={`summary-item-risk ${color}`}>
        <div className="icon-risk"><span className="material-symbols-outlined">{icon}</span></div>
        <div className="txt-risk"><strong>{title}</strong><p>{desc}</p></div>
    </div>
);

export default RiskReport;