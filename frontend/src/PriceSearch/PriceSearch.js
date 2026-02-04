import React, { useState } from 'react';
import '../components/common/Hero.css';
import RealTradeSearch from './RealTradeSearch';
import PriceComparison from './PriceComparison';
import JeonseRiskCheck from './JeonseRiskCheck';
import PriceSuggestion from './PriceSuggestion';
import RegionalTrend from './RegionalTrend';
import DataDownload from './DataDownload';
import LandPriceSearch from './LandPriceSearch';

const PriceSearch = () => {
    const [activeTab, setActiveTab] = useState('trade');

    const renderContent = () => {
        switch (activeTab) {
            case 'trade':
                return (
                    <div>
                        <RealTradeSearch />
                        <div style={{ marginTop: '20px' }}><DataDownload /></div>
                    </div>
                );
            case 'land':
                return <LandPriceSearch />;
            case 'compare':
                return <PriceComparison />;
            case 'risk':
                return <JeonseRiskCheck />;
            case 'trend':
                return <RegionalTrend />;
            case 'ai':
                return <PriceSuggestion />;
            default:
                return <RealTradeSearch />;
        }
    };

    return (
        <div className="container" style={{ marginTop: '20px', minHeight: '80vh' }}>
            <div className="hero-content" style={{ textAlign: 'center', marginBottom: '30px' }}>
                <h1 className="hero-title" style={{ fontSize: '2rem', color: '#333' }}>통합 시세 조회</h1>
                <p className="hero-subtitle" style={{ color: '#666' }}>
                    실거래가부터 AI 분석까지, 모든 부동산 정보를 한눈에 확인하세요.
                </p>
            </div>

            {/* Tab Navigation */}
            <div className="tabs" style={{ display: 'flex', justifyContent: 'center', gap: '10px', marginBottom: '30px', flexWrap: 'wrap' }}>
                <button
                    className={`btn ${activeTab === 'trade' ? 'btn-primary' : 'btn-outline-secondary'}`}
                    onClick={() => setActiveTab('trade')}
                >
                    실거래가 조회
                </button>
                <button
                    className={`btn ${activeTab === 'land' ? 'btn-primary' : 'btn-outline-secondary'}`}
                    onClick={() => setActiveTab('land')}
                >
                    공시지가
                </button>
                <button
                    className={`btn ${activeTab === 'compare' ? 'btn-primary' : 'btn-outline-secondary'}`}
                    onClick={() => setActiveTab('compare')}
                >
                    가격 비교
                </button>
                <button
                    className={`btn ${activeTab === 'risk' ? 'btn-primary' : 'btn-danger'}`}
                    onClick={() => setActiveTab('risk')}
                    style={activeTab !== 'risk' ? { borderColor: '#dc3545', color: '#dc3545' } : {}}
                >
                    전세 리스크
                </button>
                <button
                    className={`btn ${activeTab === 'trend' ? 'btn-primary' : 'btn-outline-secondary'}`}
                    onClick={() => setActiveTab('trend')}
                >
                    시세 추이
                </button>
                <button
                    className={`btn ${activeTab === 'ai' ? 'btn-primary' : 'btn-success'}`}
                    onClick={() => setActiveTab('ai')}
                    style={activeTab !== 'ai' ? { borderColor: '#198754', color: '#198754' } : {}}
                >
                    AI 가격 제안
                </button>
            </div>

            {/* Content Area */}
            <div className="tab-content">
                {renderContent()}
            </div>
        </div>
    );
};

export default PriceSearch;
