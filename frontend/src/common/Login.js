import React, { useState } from 'react';
import './Login.css';

const LoginPage = () => {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="login-container">
            <div className="login-wrapper">
                {/* 상단 로고 섹션 */}
                <div className="header-section">
                    <div className="logo-icon">
                        <span className="material-symbols-outlined">home_work</span>
                    </div>
                    <h1 className="brand-name">집현전</h1>
                    <p className="brand-subtitle">Safe Realty Analysis</p>
                </div>

                {/* 로그인 카드 */}
                <div className="login-card">
                    <div className="decoration-circle"></div>
                    <div className="card-content">
                        <div className="card-header">
                            <h2>Welcome Back</h2>
                            <p>로그인하여 안전한 부동산 분석을 시작하세요.</p>
                        </div>

                        <form className="login-form">
                            <div className="input-group">
                                <label htmlFor="email">Email / ID</label>
                                <div className="input-wrapper">
                                    <input
                                        type="text"
                                        id="email"
                                        placeholder="example@email.com"
                                    />
                                </div>
                            </div>

                            <div className="input-group">
                                <label htmlFor="password">Password</label>
                                <div className="input-wrapper">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        id="password"
                                        placeholder="••••••••"
                                    />
                                    <button
                                        type="button"
                                        className="toggle-password"
                                        onClick={() => setShowPassword(!showPassword)}
                                    >
                    <span className="material-symbols-outlined">
                      {showPassword ? 'visibility_off' : 'visibility'}
                    </span>
                                    </button>
                                </div>
                            </div>

                            <div className="form-options">
                                <label className="checkbox-container">
                                    <input type="checkbox" />
                                    <span className="checkmark">
                    <span className="material-symbols-outlined">check</span>
                  </span>
                                    <span className="label-text">로그인 상태 유지</span>
                                </label>
                                <a href="#" className="forgot-password">비밀번호 찾기</a>
                            </div>

                            <button type="submit" className="login-submit-btn">
                                Login
                            </button>
                        </form>

                        <div className="signup-link">
                            <p>계정이 없으신가요? <a href="/frontend/src/common/Registration">회원가입</a></p>
                        </div>

                        <div className="divider">
                            <span className="divider-text">Social Login</span>
                        </div>

                        <div className="social-login-grid">
                            <button className="social-btn kakao">
                                <span className="material-symbols-outlined">chat_bubble</span>
                                카카오 로그인
                            </button>
                            <button className="social-btn naver">
                                <span className="material-symbols-outlined">grid_view</span>
                                네이버 로그인
                            </button>
                        </div>
                    </div>
                </div>

                {/* 푸터 섹션 */}
                <footer className="login-footer">
                    <p>© 2024 Jiphyeonjeon Team. All rights reserved.</p>
                    <div className="footer-links">
                        <a href="#">Privacy Policy</a>
                        <a href="#">Terms of Service</a>
                    </div>
                </footer>
            </div>
        </div>
    );
};

export default LoginPage;