import React from 'react';
import {IoCodeSlash} from "react-icons/io5";
import '../Ziphyeonjeon.css';
import axios from "axios"; // Importing global styles until CSS is modularized

const Header = () => {
    const navItems = [
        {
            label: '서비스 소개',
            link: '#',
            children: [
                {label: '서비스 개요', link: '#'},
                {label: '주요 기능', link: '#'}
            ]
        },
        {
            label: '기술 스택',
            link: '#',
            children: [
                {label: 'Frontend', link: '#'},
                {label: 'Backend', link: '#'},
                {label: 'AI / DB', link: '#'}
            ]
        },
        {
            label: '위험 분석',
            link: '/riskInquiry',
            children: [
                {label: '분석 조회', link: '/riskInquiry'},
                {label: '리포트 예시', link: '/riskAnalysis'}
            ]
        }
    ];

    const refreshApiButton = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/risk/refresh-disaster`);
            console.log("/refresh-disaster:", response.data);
        } catch (error) {
            console.error("데이터 요청 실패:", error);
        }
    }

    return (
        <header className="header">
            <div className="container header-content">
                <div className="logo-area"><a className="logo_link" href={`/`}>
                    <span className="material-symbols-outlined"><img className="logo" src={`/img/Logo_cropZIPHYEONJEONv1.png`} alt={`Logo_ZIPHYEONJEONv1`}/></span>
                    집현전
                </a></div>

                {/* 내비게이션바 드랍다운*/}
                <nav className="nav-links">
                    {navItems.map((item, index) => (
                        <div key={index} className="nav-item dropdown">
                            <a href={item.link} className="nav-link">
                                {item.label}
                            </a>
                            {item.children && (
                                <div className="dropdown-menu">
                                    {item.children.map((child, childIndex) => (
                                        <a key={childIndex} href={child.link} className="dropdown-item">
                                            {child.label}
                                        </a>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </nav>

                <div className="header-buttons">

                    <a onClick={refreshApiButton}>
                        /refresh-disaster
                    </a>

                    <a
                        href="https://www.erdcloud.com/d/wHBL6BcoxjcspCNEt"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="btn-erd"
                    >
                        <span className="material-symbols-outlined"><IoCodeSlash/></span>
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
    );
};

export default Header;
