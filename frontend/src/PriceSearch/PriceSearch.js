import React, { useState } from 'react';
import '../Ziphyeonjeon.css'; // Ensure CSS is available
import MainLayout from '../layouts/MainLayout'; // Import MainLayout
import '../components/common/Hero.css';
import RealTradeSearch from './RealTradeSearch';
import PriceComparison from './PriceComparison';
import JeonseRiskCheck from './JeonseRiskCheck';
import PriceSuggestion from './PriceSuggestion';
import RegionalTrend from './RegionalTrend';
import DataDownload from './DataDownload';
import LandPriceSearch from './LandPriceSearch';
import { FaMagnifyingGlass, FaRobot, FaChartLine, FaScaleBalanced, FaTriangleExclamation, FaFileArrowDown, FaArrowLeft, FaHouse } from "react-icons/fa6";
import { useNavigate } from 'react-router-dom';

const PriceSearch = () => {
    const [view, setView] = useState('dashboard'); // 'dashboard' or specific feature key
    const navigate = useNavigate();

    const handleCardClick = (featureKey) => {
        setView(featureKey);
    };

    const handleBack = () => {
        setView('dashboard');
    };

    const handleGoHome = () => {
        navigate('/');
    };

    const features = [
        {
            key: 'trade',
            title: '실거래가 조회',
            desc: '국토부 아파트/빌라 실거래가 데이터 검색',
            icon: <FaMagnifyingGlass size={40} color="#0d6efd" />,
            component: (
                <div>
                    <RealTradeSearch />
                    <div style={{ marginTop: '20px' }}><DataDownload /></div>
                </div>
            )
        },
        {
            key: 'ai',
            title: 'AI 적정가 제안',
            desc: '머신러닝 기반 우리집 적정 가격 분석',
            icon: <FaRobot size={40} color="#198754" />,
            component: <PriceSuggestion />
        },
        {
            key: 'trend',
            title: '시세 추이 그래프',
            desc: '최근 1년간 지역별 시세 변동 흐름 확인',
            icon: <FaChartLine size={40} color="#6610f2" />,
            component: <RegionalTrend />
        },
        {
            key: 'compare',
            title: '매물 가격 비교',
            desc: '관심 매물 두 곳의 스펙과 가격 상세 비교',
            icon: <FaScaleBalanced size={40} color="#fd7e14" />,
            component: <PriceComparison />
        },
        {
            key: 'risk',
            title: '전세 리스크 진단',
            desc: '깡통전세 위험도 분석 및 안전 등급 확인',
            icon: <FaTriangleExclamation size={40} color="#dc3545" />,
            component: <JeonseRiskCheck />
        },
        {
            key: 'land',
            title: '공시지가 조회',
            desc: '토지 공시지가 및 실거래 데이터 다운로드',
            icon: <FaFileArrowDown size={40} color="#0dcaf0" />,
            component: <LandPriceSearch />
        }
    ];

    const renderDashboard = () => (
        <div style={{ maxWidth: '1000px', margin: '0 auto' }}>
            <button
                onClick={handleGoHome}
                style={{
                    background: 'none',
                    border: 'none',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '8px',
                    cursor: 'pointer',
                    fontSize: '1rem',
                    color: '#555',
                    marginBottom: '20px',
                    padding: '10px 0'
                }}
            >
                <FaHouse /> 메인으로 돌아가기
            </button>

            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
                gap: '20px'
            }}>
                {features.map((feature) => (
                    <div
                        key={feature.key}
                        onClick={() => handleCardClick(feature.key)}
                        style={{
                            background: 'white',
                            padding: '30px',
                            borderRadius: '15px',
                            boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
                            cursor: 'pointer',
                            transition: 'transform 0.2s, box-shadow 0.2s',
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            justifyContent: 'center',
                            textAlign: 'center',
                            border: '1px solid #eee'
                        }}
                        onMouseEnter={(e) => {
                            e.currentTarget.style.transform = 'translateY(-5px)';
                            e.currentTarget.style.boxShadow = '0 8px 25px rgba(0,0,0,0.15)';
                        }}
                        onMouseLeave={(e) => {
                            e.currentTarget.style.transform = 'translateY(0)';
                            e.currentTarget.style.boxShadow = '0 4px 15px rgba(0,0,0,0.1)';
                        }}
                    >
                        <div style={{ marginBottom: '15px', padding: '15px', borderRadius: '50%', background: '#f8f9fa' }}>
                            {feature.icon}
                        </div>
                        <h3 style={{ margin: '10px 0', fontSize: '1.2rem', fontWeight: 'bold', color: '#333' }}>{feature.title}</h3>
                        <p style={{ margin: '0', color: '#666', fontSize: '0.95rem' }}>{feature.desc}</p>
                    </div>
                ))}
            </div>
        </div>
    );

    const renderDetail = () => {
        const activeFeature = features.find(f => f.key === view);
        return (
            <div style={{ maxWidth: '1000px', margin: '0 auto' }}>
                <button
                    onClick={handleBack}
                    style={{
                        background: 'none',
                        border: 'none',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '8px',
                        cursor: 'pointer',
                        fontSize: '1rem',
                        color: '#555',
                        marginBottom: '20px',
                        padding: '10px 0'
                    }}
                >
                    <FaArrowLeft /> 메뉴로 돌아가기
                </button>
                <div style={{ animation: 'fadeIn 0.3s ease-in-out' }}>
                    {activeFeature?.component}
                </div>
            </div>
        );
    };

    return (
        <MainLayout>
            <div className="container" style={{ marginTop: '40px', minHeight: '80vh', paddingBottom: '50px' }}>
                <div className="hero-content" style={{ textAlign: 'center', marginBottom: '40px' }}>
                    <h1 className="hero-title" style={{ fontSize: '2.2rem', color: '#2c3e50', marginBottom: '10px' }}>
                        {view === 'dashboard' ? '통합 시세 조회 서비스' : features.find(f => f.key === view)?.title}
                    </h1>
                    <p className="hero-subtitle" style={{ color: '#7f8c8d', fontSize: '1.1rem' }}>
                        {view === 'dashboard'
                            ? '실거래가부터 AI 분석까지, 스마트한 부동산 의사결정을 지원합니다.'
                            : features.find(f => f.key === view)?.desc}
                    </p>
                </div>

                {view === 'dashboard' ? renderDashboard() : renderDetail()}
            </div>
        </MainLayout>
    );
};

export default PriceSearch;
