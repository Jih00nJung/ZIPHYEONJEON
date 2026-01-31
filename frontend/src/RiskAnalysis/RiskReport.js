import React from 'react';
import './RiskReport.css';

const RiskReport = () => {
    return (
        <div className="analysis-page">
            {/* 1. Analysis Process (기획서 2단계 시각화) */}
            <section className="process-card">
                <div className="progress-track"><div className="progress-fill"></div></div>
                <div className="steps-container">
                    <Step icon="장소" label="주소<br/>분석" active />
                    <Step icon="판별" label="등기부등본<br/>확인" active />
                    <Step icon="분석" label="위험성<br/>평가" current />
                    <Step icon="추가 자료" label="계약서<br/>초안" disabled />
                    <Step icon="검증" label="최종<br/>평가" disabled />
                </div>
            </section>

            {/* 2. Address Header */}
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '32px'}}>
                <div>
                    <div style={{display: 'flex', gap: '8px', marginBottom: '8px'}}>
                        <span className="status-pill" style={{background: '#ECFDF5', color: '#059669'}}>VERIFIED</span>
                        <span className="status-pill" style={{background: '#eff6ff', color: '#1d4ed8'}}>APARTMENT</span>
                    </div>
                    <h1 style={{fontSize: '2.5rem', fontWeight: '900', color: '#0f172a'}}>서울특별시 강남구 테헤란로 123</h1>
                    <p style={{color: '#64748b', fontWeight: '500'}}>삼성 래미안 아파트 105동 202호</p>
                </div>
                <div style={{display: 'flex', gap: '12px', alignItems: 'center'}}>
                    <button className="btn-white">Share</button>
                    <button className="btn-primary-gradient">Download Report</button>
                </div>
            </div>

            <div className="dashboard-grid">
                {/* 3. Safety Score Card (종합 안전 점수 85점) */}
                <div className="white-card col-8" style={{display: 'flex', gap: '40px'}}>
                    <div className="score-box">
                        <svg className="score-circle-svg" viewBox="0 0 100 100">
                            <circle cx="50" cy="50" r="45" stroke="#F1F5F9" strokeWidth="8" fill="none" />
                            <circle cx="50" cy="50" r="45" stroke="#34D399" strokeWidth="8" fill="none"
                                    strokeDasharray="283" strokeDashoffset="42" strokeLinecap="round" />
                        </svg>
                        <div className="score-number-group">
                            <span className="score-val">85</span>
                            <div style={{fontSize: '12px', fontWeight: '800', color: '#94a3b8'}}>SAFETY SCORE</div>
                        </div>
                    </div>
                    <div style={{flex: 1}}>
                        <h2 style={{fontSize: '1.5rem', fontWeight: '800', marginBottom: '12px'}}>Safe for Contract</h2>
                        <p style={{color: '#475569', lineHeight: '1.7', marginBottom: '24px'}}>
                            이 매물은 서울시 상위 15% 이내의 안전 등급을 유지하고 있습니다. 근저당권 합산액이 시세 대비 안전하며 위반건축물 이력이 없습니다.
                        </p>
                        <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '16px'}}>
                            <SmallProgress label="Legal" val={95} color="#3B82F6" />
                            <SmallProgress label="Market" val={78} color="#FBBF24" />
                            <SmallProgress label="Physical" val={88} color="#34D399" />
                        </div>
                    </div>
                </div>

                {/* 4. Risk Summary (요약 리스크) */}
                <div className="white-card col-4">
                    <h3 style={{fontSize: '1.1rem', fontWeight: '800', marginBottom: '24px'}}>Risk Summary</h3>
                    <SummaryItem icon="check" title="Owner Verified" desc="Identity matches title deed" type="success" />
                    <SummaryItem icon="verified_user" title="Insurance Eligible" desc="Qualifies for HUG guarantee" type="success" />
                    <SummaryItem icon="priority_high" title="High Demand" desc="Price volatility expected" type="warning" />
                    <button className="btn-dark-full">View Detailed Report →</button>
                </div>

                {/* 5. Title Deed & Market (등기 및 시세 분석) */}
                <div className="white-card col-4">
                    <h4 style={{fontWeight: '800', marginBottom: '20px'}}>Title Deed Analysis</h4>
                    <div className="owner-info">
                        <span className="material-symbols-outlined" style={{color: '#34D399'}}>verified</span>
                        <span>Owner: Kim Min-su</span>
                    </div>
                    <div style={{marginTop: '20px'}}>
                        <div style={{display: 'flex', justifyContent: 'space-between', fontSize: '12px', fontWeight: '700'}}>
                            <span>Mortgage</span><span>0 KRW</span>
                        </div>
                        <div className="bar-bg"><div className="bar-fill" style={{width: '2%', background: '#34D399'}}></div></div>
                    </div>
                </div>

                <div className="white-card col-4">
                    <h4 style={{fontWeight: '800', marginBottom: '8px'}}>Market Valuation</h4>
                    <div style={{fontSize: '1.8rem', fontWeight: '900', marginBottom: '16px'}}>1.25B <small style={{fontSize: '14px', color: '#94a3b8'}}>KRW</small></div>
                    <div className="jeonse-ratio">
                        <div style={{display: 'flex', justifyContent: 'space-between', fontSize: '11px', fontWeight: '800'}}>
                            <span>Jeonse Ratio</span><span style={{color: '#D97706'}}>78%</span>
                        </div>
                        <div className="ratio-bar-multi">
                            <div className="bar-segment safe" style={{width: '60%'}}></div>
                            <div className="bar-segment caution striped-bg" style={{width: '18%'}}></div>
                        </div>
                    </div>
                </div>

                <div className="white-card col-4">
                    <h4 style={{fontWeight: '800', marginBottom: '20px'}}>Building Status</h4>
                    <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px'}}>
                        <div className="stat-box"><span>Age</span><strong>12 yrs</strong></div>
                        <div className="stat-box"><span>Units</span><strong>842</strong></div>
                    </div>
                </div>
            </div>
        </div>
    );
};

/* Sub-Components */
const Step = ({ icon, label, active, current, disabled }) => (
    <div className={`step-unit ${active ? 'active' : ''} ${current ? 'current' : ''} ${disabled ? 'disabled' : ''}`}>
        <div className="step-circle"><span className="material-symbols-outlined">{icon}</span></div>
        <p dangerouslySetInnerHTML={{__html: label}}></p>
    </div>
);

const SmallProgress = ({ label, val, color }) => (
    <div className="small-prog">
        <div className="label-row"><span>{label}</span><strong>{val}</strong></div>
        <div className="bar-base"><div className="bar-val" style={{width: `${val}%`, background: color}}></div></div>
    </div>
);

const SummaryItem = ({ icon, title, desc, type }) => (
    <div className={`summary-item ${type}`}>
        <div className="item-icon-circle"><span className="material-symbols-outlined">{icon}</span></div>
        <div><strong>{title}</strong><p>{desc}</p></div>
    </div>
);

export default RiskReport;