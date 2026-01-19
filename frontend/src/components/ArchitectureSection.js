import React from 'react';
import './ArchitectureSection.css';

// 개별 카드 컴포넌트
export const ArchitectureCard = ({title, subtitle, description, color }) => {
    return (
        <div className="arch-card">
            <div className="icon-wrapper" style={{ backgroundColor: `${color}10` }}>
            </div>
            <h3 className="card-title">{title}</h3>
            <p className="card-subtitle">{subtitle}</p>
            <p className="card-description">{description}</p>
        </div>
    );
};

// 전체 섹션 컴포넌트
const ArchitectureSection = ({ header, subHeader, description, children }) => {
    return (
        <section className="arch-section">
            <div className="arch-container">
                <header className="arch-header">
                    <span className="arch-top-label">{header}</span>
                    <h2 className="arch-main-title">{subHeader}</h2>
                    <p className="arch-top-description">{description}</p>
                </header>

                <div className="arch-grid-wrapper">
                    <div className="connector-line"></div>
                    <div className="arch-grid">
                        {children}
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ArchitectureSection;