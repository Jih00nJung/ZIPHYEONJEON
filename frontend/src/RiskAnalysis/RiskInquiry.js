import React, {useState} from 'react';
import axios from 'axios';
import './RiskInquiry.css';

import MainLayout from "../layouts/MainLayout";
import {
    IoSearchOutline,
    IoCloudUploadOutline,
    IoShieldCheckmarkOutline,
    IoCheckmark,
    IoArrowForwardOutline
} from "react-icons/io5";

const RiskInquiry = () => {

    const [address, setAddress] = useState('');
    const [file, setFile] = useState(null);
    const [isSearched, setIsSearched] = useState(false);

    // 주소 요청 버튼
    const addressButton = async () => {
        if (!address.trim()) return;
        setIsSearched(true);

        try {
            console.log(`${address}에 대한 데이터 요청을 시작합니다...`);
            const [disasterRes, buildingRes] = await Promise.all([
                axios.get(`http://localhost:8080/api/risk/disaster/${address}`),
                axios.get(`http://localhost:8080/api/risk/building/${address}`)
            ]);
            console.log("재해 위험 정보:", disasterRes.data);
            console.log("건축물 대장 정보:", buildingRes.data);
        } catch (error) {
            console.error("데이터 요청 실패:", error);
        }
    }

    // 주소가 바뀌면
    const handleAddressChange = (e) => {
        setAddress(e.target.value);
        setIsSearched(false);
    };

    // UUID 생성 (중복 방지)
    const generateUUID = () => {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };


    // 파일 업로드
    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const isInvalid = !address.trim() || !file || !isSearched;

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!address.trim() || !file || !isSearched) {
            alert("주소와 등기부등본을 모두 입력해주세요.");
            return;
        }

        const commonRequestId = generateUUID();

        const backFormData = new FormData();
        backFormData.append('address', address);
        backFormData.append('requestId', commonRequestId);
        backFormData.append('file', file);

        const ocrFormData = new FormData();
        const message = {
            version: 'V2',
            requestId: commonRequestId,
            timestamp: Date.now(),
            lang: 'ko',
            images: [
                {
                    format: file.name.split('.').pop().toLowerCase(),
                    name: 'registration_document'
                }
            ]
        };
        ocrFormData.append('message', JSON.stringify(message));
        ocrFormData.append('file', file);

        try {
            const [uploadRes, ocrRes] = await Promise.all([
                axios.post('http://localhost:8080/api/risk/upload', backFormData),
                axios.post('http://localhost:8080/api/risk/ocr', ocrFormData)
            ]);

            console.log('RiskInquiry DB 저장 완료:', uploadRes.data);
            console.log('RiskInquiry OCR 분석 완료:', ocrRes.data);

        } catch (error) {
            console.error('RiskInquiry 요청 실패:', error.response?.data || error.message);
            alert("RiskInquiry 서버 통신 중 오류가 발생했습니다.");
        }
    };

    // 툴팁 팝업
    const [isTooltipVisible, setIsTooltipVisible] = useState(false);
    const handleMouseEnter = () => setIsTooltipVisible(true);
    const handleMouseLeave = () => setIsTooltipVisible(false);

    return (
        <MainLayout>

            <div className="risk-inquiry-page">

                {/* Main Content */}
                <main className="main-content">
                    <section className="main-section">
                        <div className="main-bg-blur"></div>
                        <span className="badge">
                        <IoShieldCheckmarkOutline size={14}/> 안심 분석
                        </span>
                        <h2 className="title">종합 위험도 분석하기</h2>
                        <p className="description">
                            부동산 정보를 입력하고 등기부등본을 업로드해 주세요. <br/>
                            집현전이 법적 및 시세 위험을 분석해 드립니다.
                        </p>
                    </section>

                    <div className="form-container">
                        <div className="card">
                            <div className="card-decoration"></div>
                            <form className="analysis-form" onSubmit={(e) => {
                                e.preventDefault();
                                addressButton();
                            }}>
                                {/* 1: 부동산 주소 */}
                                <div className="form-step">
                                    <div className="label-row">
                                        <label className="input-label">
                                            <span className="step-num">1</span> 부동산 주소
                                            <span className="tag-required">필수</span>
                                        </label>
                                        <span className="helper-text-link">지도 열기</span>
                                    </div>
                                    <div className="search-input-wrapper">
                                        <IoSearchOutline className="search-icon"/>
                                        <input
                                            type="text"
                                            placeholder="지번주소를 입력하세요. 예) 서울특별시 용산구 한강대로 405"
                                            value={address}
                                            onChange={(e) => setAddress(e.target.value)}
                                        />
                                        <button type="button" className="search-btn" onChange={(e) => {
                                            setAddress(e.target.value);
                                            setIsSearched(false);
                                        }} onClick={addressButton}>검색
                                        </button>
                                    </div>
                                </div>

                                <hr className="form-divider"/>

                                {/* 2: 등기부등본 업로드 */}
                                <div className="form-step">
                                    <div className="label-row">
                                        <label className="input-label">
                                            <span className="step-num">2</span> 등기부등본 업로드
                                            <span className="tag-required">필수</span>
                                        </label>
                                        <div className="tooltip-container">
                                            <span className="tooltip-trigger"
                                                  onMouseEnter={handleMouseEnter}
                                                  onMouseLeave={handleMouseLeave}>등기부등본은 왜요?</span>

                                            {isTooltipVisible && (
                                                <div className="tooltip-box">
                                                    권리 분석을 위해 필요한 서류입니다.
                                                </div>
                                            )}
                                        </div>

                                    </div>
                                    <div className="upload-box">
                                        <input type="file" className="file-input" onChange={handleFileChange}/>
                                        <div className="upload-content">
                                            <div className="upload-icon-circle">
                                                <IoCloudUploadOutline size={30}/>
                                            </div>
                                            <h3>{file ? `선택된 파일: ${file.name}` : "업로드하려면 클릭하거나 파일을 끌어오세요."}</h3>
                                            <p>PDF, JPG, PNG 파일 (10MB 이하)</p>
                                            <div className="select-btn">파일 선택</div>
                                        </div>
                                    </div>
                                </div>

                                <hr className="form-divider"/>

                                {/* 3: 약관 동의 */}
                                <div className="form-step">
                                    <label className="input-label">
                                        <span className="step-num">3</span> 약관 동의
                                    </label>
                                    <div className="consent-group">
                                        <ConsentItem
                                            title="분석 결과 활용 시 주의사항"
                                            desc={<>
                                                분석 결과는 과거 데이터를 바탕으로 구성되었습니다.<br/>
                                                실제 매물 상태와 차이가 있을 수 있으므로, 의사 결정의 보조 수단으로만 사용하시길 권장합니다.
                                            </>}
                                            essential={true}
                                        />
                                        <ConsentItem
                                            title="부동산 재해 위험 정보 조회 동의"
                                            desc={"정부 공공 데이터를 활용하여 해당 위치의 침수, 화재 등 재해 이력을 확인합니다."}
                                            essential={true}
                                        />
                                        <ConsentItem
                                            title="전세가율 분석"
                                            desc="시세 데이터를 바탕으로 매물의 안전성을 진단합니다."
                                            essential={false}
                                        />
                                    </div>
                                </div>

                                <div className="submit-section">
                                    {isInvalid && (
                                        <div className="validation-message">
                                        <span>
                                            주소 검색과 파일 업로드가 완료되어야 분석을 시작할 수 있습니다.
                                        </span>
                                        </div>
                                    )}

                                    <button type="button" className={`submit-btn ${isInvalid ? 'disabled' : ''}`}
                                            disabled={isInvalid} onClick={handleSubmit}>
                                        <span>위험 분석 시작하기</span>
                                        <IoArrowForwardOutline size={20}/>
                                    </button>

                                    <p className="terms-text">

                                        시작하기를 클릭하면 <a href="#">서비스 약관</a>에 동의하게 됩니다.
                                    </p>
                                </div>
                            </form>
                        </div>

                    </div>
                </main>
            </div>

        </MainLayout>
    );
};

const ConsentItem = ({title, desc, essential}) => (
    <label className="consent-item">
        <div className="checkbox-wrapper">
            <input type="checkbox" defaultChecked/>
            <span className="custom-checkbox"><IoCheckmark/></span>
        </div>
        <div className="consent-text">
            <div className="consent-header">
                <p className="consent-title">{title}</p>
                <span className={`tag-${essential ? 'essential' : 'optional'}`}>
          {essential ? '필수' : '선택'}
        </span>
            </div>
            <p className="consent-desc">{desc}</p>
        </div>
    </label>
);

const FeatureItem = ({icon, text, color}) => (
    <div className="feature-item">
        <div className={`feature-icon icon-${color}`}>{icon}</div>
        <span>{text}</span>
    </div>
);

export default RiskInquiry;