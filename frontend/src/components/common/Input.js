import React from 'react';
import './Input.css';

/**
 * Input Component
 * @param {string} label - 필드 라벨
 * @param {string} icon - 좌측 아이콘 이름
 * @param {ReactNode} rightElement - 우측 추가 요소 (예: 비밀번호 보기 버튼)
 */
const Input = ({
    label,
    id,
    type = 'text',
    placeholder,
    icon,
    rightElement,
    value,
    onChange,
    className = "",
    ...props
}) => {
    return (
        <div className={`common-input-group ${className}`}>
            {label && <label htmlFor={id} className="input-label">{label}</label>}
            <div className="input-wrapper">
                {icon && <span className="material-symbols-outlined input-icon">{icon}</span>}
                <input
                    id={id}
                    type={type}
                    placeholder={placeholder}
                    value={value}
                    onChange={onChange}
                    className={`common-input ${icon ? 'has-icon' : ''} ${rightElement ? 'has-right' : ''}`}
                    {...props}
                />
                {rightElement && <div className="right-element">{rightElement}</div>}
            </div>
        </div>
    );
};

export default Input;
