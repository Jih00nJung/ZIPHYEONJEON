import React from 'react';
import './Hero.css';
import Badge from './Badge';

/**
 * Hero Component
 * @param {string} title - 메인 타이틀
 * @param {string} subtitle - 서브 설명
 * @param {string} badgeText - 상단 뱃지 텍스트
 * @param {string} badgeIcon - 상단 뱃지 아이콘
 * @param {ReactNode} actions - 우측 액션 버튼들
 */
const Hero = ({ title, subtitle, badgeText, badgeIcon, actions, className = '' }) => {
    return (
        <header className={`common-hero ${className}`}>
            <div className="hero-content">
                {badgeText && (
                    <Badge color="blue" variant="subtle" icon={badgeIcon} className="hero-badge">
                        {badgeText}
                    </Badge>
                )}
                <h1 className="hero-title">
                    {typeof title === 'string' ? <span dangerouslySetInnerHTML={{ __html: title }} /> : title}
                </h1>
                {subtitle && <p className="hero-subtitle">{subtitle}</p>}
            </div>
            {actions && <div className="hero-actions">{actions}</div>}
        </header>
    );
};

export default Hero;
