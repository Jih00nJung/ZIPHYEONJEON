import React from 'react';
import './Card.css';

/**
 * Card Component
 * 프리미엄 스타일의 컨테이너 컴포넌트
 * @param {string} className - 추가 클래스명
 * @param {string} padding - 내부 패딩 (기본값: 32px)
 * @param {boolean} glass - 글래스모피즘 효과 여부
 * @param {boolean} overflow - 오버플로우 숨김 여부
 */
const Card = ({ children, className = '', padding = '32px', glass = false, overflow = true }) => {
    return (
        <div
            className={`common-card ${glass ? 'glass' : ''} ${className}`}
            style={{
                padding: padding,
                overflow: overflow ? 'hidden' : 'visible'
            }}
        >
            {glass && <div className="decoration-blur"></div>}
            <div className="card-inner-content">
                {children}
            </div>
        </div>
    );
};

export default Card;
