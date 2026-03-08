import React from 'react';
import './Badge.css';

/**
 * Badge Component
 * @param {string} color - 뱃지 색상 테마 (blue, green, yellow, red, gray)
 * @param {string} variant - 스타일 (subtle, outline, solid)
 */
const Badge = ({ children, color = 'blue', variant = 'subtle', icon, className = '' }) => {
    return (
        <span className={`common-badge badge-${color} badge-${variant} ${className}`}>
            {icon && <span className="material-symbols-outlined badge-icon">{icon}</span>}
            {children}
        </span>
    );
};

export default Badge;
