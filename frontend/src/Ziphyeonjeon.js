import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Ziphyeonjeon.css';

// react icon
import { IoCodeSlash } from "react-icons/io5";

const Ziphyeonjeon = () => {
    const navigate = useNavigate();

    const handleRiskButton = () => {
        navigate('/RiskAnalysis');
    };

    return (
        <div className="main-wrapper">
            {/* Header */}
            <header className="header">
                <div className="container header-content">
                    <div className="logo-area">
                        <span className="material-symbols-outlined">집현전</span>
                        Ziphyeonjeon
                    </div>

                    <nav className="nav-links">
                        <a href="#">서비스 소개</a>
                        <a href="#">기술 스택</a>
                        <a href="/RiskAnalysis">위험 분석</a>
                    </nav>


                    <div className="header-buttons">
                        <a
                            href="https://www.erdcloud.com/d/wHBL6BcoxjcspCNEt"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn-erd"
                        >
                            <span className="material-symbols-outlined"><IoCodeSlash /></span>
                            ERD
                        </a>
                        <a
                            href="https://github.com/Jih00nJung/ZIPHYEONJEON.git"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn-github"
                        >
                            Github
                        </a>
                    </div>
                </div>
            </header>

            {/* Hero Section */}
            <section className="hero">
                <div className="container hero-layout">
                    <div className="hero-text">
                        <div className="badge">안전한 부동산 거래의 시작</div>
                        <h1 className="hero-title">
                            당신의 소중한 보증금,<br />
                            <span className="text-gradient">AI가 지켜드립니다</span>
                        </h1>
                        <p className="hero-desc">
                            Spring Boot와 AI 기반의 정밀 권리 분석으로<br />
                            전세 사기 리스크를 사전에 차단합니다.
                        </p>

                        <div className="search-container">
                            <input type="text" placeholder="도로명 주소 또는 지번을 입력하세요" />
                            <button className="btn-primary" onClick={handleRiskButton}>리스크 조회</button>
                        </div>
                    </div>
                </div>
            </section>

            {/* Architecture (Portfolio Core) */}
            <section className="arch-section">
                <div className="container">
                    <h2 style={{ textAlign: 'center', marginBottom: '10px' }}>System Architecture</h2>
                    <p style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                        Java 21 & Spring Boot 기반의 견고한 백엔드 시스템
                    </p>

                    <div className="arch-grid">
                        <div className="arch-card">
                            <span className="material-symbols-outlined" style={{ fontSize: '48px', color: '#10b981' }}>settings</span>
                            <h3>Spring Boot Server</h3>
                            <p>Main API 및 비즈니스 로직 처리</p>
                        </div>

                        <div className="arch-card">
                            <span className="material-symbols-outlined" style={{ fontSize: '48px', color: '#3b82f6' }}>psychology</span>
                            <h3>Python AI Server</h3>
                            <p>OCR 분석 및 LLM 기반 위험도 예측</p>
                        </div>

                        <div className="arch-card">
                            <span className="material-symbols-outlined" style={{ fontSize: '48px', color: '#f59e0b' }}>database</span>
                            <h3>MySQL</h3>
                            <p>부동산 데이터 및 유저 정보 관리</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default Ziphyeonjeon;