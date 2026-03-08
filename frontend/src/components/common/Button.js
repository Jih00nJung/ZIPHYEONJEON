import React from 'react';
import './Button.css';

/**
 * Button Component
 * @param {string} variant - 버튼 스타일 (primary, secondary, dark, outline, ghost)
 * @param {string} size - 크기 (sm, md, lg)
 * @param {boolean} fullWidth - 전체 너비 여부
 * @param {ReactNode} icon - 버튼 내 아이콘
 */
const Button = ({
    children,
    onClick,
    type = 'button',
    variant = 'primary',
    size = 'md',
    fullWidth = false,
    icon,
    className = '',
    disabled = false
}) => {
    return (
        <button
            type={type}
            className={`common-btn btn-${variant} btn-${size} ${fullWidth ? 'full-width' : ''} ${className}`}
            onClick={onClick}
            disabled={disabled}
        >
            {icon && <span className="material-symbols-outlined btn-icon">{icon}</span>}
            <span className="btn-text">{children}</span>
        </button>
    );
};

export default Button;
