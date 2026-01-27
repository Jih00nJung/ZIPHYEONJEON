import React, { useState } from 'react';
import './Registration.css';

const SignupPage = () => {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="signup-page">
            {/* 네비게이션 헤더 */}
            <header className="signup-header">
                <div className="header-container">
                    <a href="/" className="logo-group">
                        <div className="logo-box">
                            <span className="material-symbols-outlined">home_work</span>
                        </div>
                        <div className="logo-text">
                            <h1>집현전</h1>
                            <p>Safe Realty</p>
                        </div>
                    </a>
                    <div className="header-actions">
                        <span className="login-prompt">Already have an account?</span>
                        <a href="/login" className="btn-login-outline">Log In</a>
                    </div>
                </div>
            </header>

            {/* 메인 가입 폼 */}
            <main className="signup-main">
                <div className="form-container">
                    <div className="signup-card">
                        <div className="bg-blur-decoration"></div>

                        <div className="card-inner">
                            <div className="card-title">
                                <h2>Create Account</h2>
                                <p>Join Jiphyeonjeon for safe real estate transactions.</p>
                            </div>

                            {/* 스텝퍼 (단계 표시) */}
                            <div className="stepper">
                                <div className="stepper-line"></div>
                                <div className="step active">
                                    <div className="step-number">1</div>
                                    <span className="step-label">Basic Info</span>
                                </div>
                                <div className="step">
                                    <div className="step-number">2</div>
                                    <span className="step-label">Verification</span>
                                </div>
                                <div className="step">
                                    <div className="step-number">3</div>
                                    <span className="step-label">Agreements</span>
                                </div>
                            </div>

                            <form className="signup-form">
                                <div className="input-stack">
                                    <div className="field-group">
                                        <label>Full Name</label>
                                        <div className="input-with-icon">
                                            <span className="material-symbols-outlined icon">person</span>
                                            <input type="text" placeholder="Enter your full name" />
                                        </div>
                                    </div>

                                    <div className="field-group">
                                        <label>Email Address</label>
                                        <div className="input-with-icon">
                                            <span className="material-symbols-outlined icon">mail</span>
                                            <input type="email" placeholder="name@example.com" />
                                        </div>
                                    </div>

                                    <div className="field-group">
                                        <label>Password</label>
                                        <div className="input-with-icon">
                                            <span className="material-symbols-outlined icon">lock</span>
                                            <input
                                                type={showPassword ? "text" : "password"}
                                                placeholder="At least 8 characters"
                                            />
                                            <button
                                                type="button"
                                                className="btn-toggle-eye"
                                                onClick={() => setShowPassword(!showPassword)}
                                            >
                        <span className="material-symbols-outlined">
                          {showPassword ? 'visibility_off' : 'visibility'}
                        </span>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div className="submit-area">
                                    <button type="button" className="btn-continue">Continue</button>
                                </div>

                                <div className="social-divider">
                                    <span>Or sign up with</span>
                                </div>

                                <div className="social-grid">
                                    <button type="button" className="social-btn google">
                                        <img src="https://www.google.com/favicon.ico" alt="Google" />
                                        Google
                                    </button>
                                    <button type="button" className="social-btn kakao">
                                        <span className="material-symbols-outlined">chat_bubble</span>
                                        Kakao
                                    </button>
                                </div>

                                {/* 약관 미리보기 */}
                                <div className="terms-preview">
                                    <div className="preview-header">
                                        <h3>Preview: Terms & Agreements</h3>
                                        <label className="select-all">
                                            <input type="checkbox" />
                                            <span>Select All</span>
                                        </label>
                                    </div>
                                    <div className="terms-list">
                                        <div className="term-item"><div className="dot"></div> Service Terms Agreement (Essential)</div>
                                        <div className="term-item"><div className="dot"></div> Privacy Policy Agreement (Essential)</div>
                                        <div className="term-item"><div className="dot"></div> Marketing Notifications (Optional)</div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    {/* 하단 보안 뱃지 */}
                    <div className="security-badges">
                        <div className="badge">
                            <div className="badge-icon blue"><span className="material-symbols-outlined">encrypted</span></div>
                            <span>Encrypted</span>
                        </div>
                        <div className="badge">
                            <div className="badge-icon green"><span className="material-symbols-outlined">verified_user</span></div>
                            <span>Secure</span>
                        </div>
                        <div className="badge">
                            <div className="badge-icon gray"><span className="material-symbols-outlined">policy</span></div>
                            <span>Privacy First</span>
                        </div>
                    </div>
                </div>
            </main>

            <footer className="signup-footer">
                <p>© 2024 Jiphyeonjeon Team. All rights reserved.</p>
                <div className="footer-links">
                    <a href="#">Privacy Policy</a>
                    <a href="#">Terms of Use</a>
                    <a href="#">Contact Support</a>
                </div>
            </footer>
        </div>
    );
};

export default SignupPage;