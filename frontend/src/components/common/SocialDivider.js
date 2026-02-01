import React from 'react';
import './SocialDivider.css';

const SocialDivider = ({ children = "OR", className = "" }) => {
    return (
        <div className={`social-divider ${className}`}>
            <div className="divider-line"></div>
            <span className="divider-text">{children}</span>
        </div>
    );
};

export default SocialDivider;
