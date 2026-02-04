import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { checkJeonseRisk } from '../api/priceApi';

const JeonseRiskCheck = () => {
    const [formData, setFormData] = useState({
        address: '',
        propertyType: '아파트',
        exclusiveArea: '',
        jeonseAmount: ''
    });
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleCheck = async () => {
        if (!formData.address || !formData.exclusiveArea || !formData.jeonseAmount) {
            alert('모든 정보를 입력해주세요.');
            return;
        }
        setLoading(true);
        try {
            const request = {
                address: formData.address, // "강남구 역삼동" (유사 검색 지원)
                propertyType: formData.propertyType,
                exclusiveArea: parseFloat(formData.exclusiveArea),
                jeonse_amount: parseInt(formData.jeonseAmount)
            };
            const data = await checkJeonseRisk(request);
            setResult(data);
        } catch (error) {
            alert('분석 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="risk-check-card">
            <h2 style={{ color: '#d63384' }}>전세 사기 리스크 진단 (P-005)</h2>
            <p>보증금을 입력하면 깡통 전세 위험도를 분석해드립니다.</p>

            <div className="risk-form" style={{ maxWidth: '500px', margin: '0 auto' }}>
                <Input
                    name="address"
                    value={formData.address}
                    onChange={handleChange}
                    placeholder="지역 (예: 강남구 역삼동)"
                    label="지역"
                />
                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '5px' }}>유형</label>
                    <select
                        name="propertyType"
                        value={formData.propertyType}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ddd' }}
                    >
                        <option value="아파트">아파트</option>
                        <option value="빌라">빌라/연립</option>
                        <option value="오피스텔">오피스텔</option>
                    </select>
                </div>
                <Input
                    name="exclusiveArea"
                    value={formData.exclusiveArea}
                    onChange={handleChange}
                    placeholder="전용면적 (m²)"
                    label="면적"
                    type="number"
                />
                <Input
                    name="jeonseAmount"
                    value={formData.jeonseAmount}
                    onChange={handleChange}
                    placeholder="보증금 (만원)"
                    label="전세보증금"
                    type="number"
                />
                <Button onClick={handleCheck} disabled={loading} style={{ width: '100%', marginTop: '10px', backgroundColor: '#dc3545' }}>
                    {loading ? '진단 중...' : '위험도 조회'}
                </Button>
            </div>

            {result && (
                <div className="risk-result" style={{ marginTop: '30px', padding: '20px', background: '#fff0f3', borderRadius: '8px', border: '1px solid #f5c2c7' }}>
                    <h3 style={{ textAlign: 'center', color: result.riskLevel === 'SAFE' ? 'green' : (result.riskLevel === 'CAUTION' ? 'orange' : 'red') }}>
                        {result.riskLevel === 'SAFE' ? '안전' : (result.riskLevel === 'CAUTION' ? '주의 필요' : '위험 (깡통전세)')}
                    </h3>
                    <div style={{ textAlign: 'center', fontSize: '1.2rem', fontWeight: 'bold', marginBottom: '15px' }}>
                        내 전세가율: {result.myJeonseRatio.toFixed(1)}%
                    </div>

                    <div className="progress-bar-container" style={{ width: '100%', height: '20px', background: '#e9ecef', borderRadius: '10px', overflow: 'hidden', position: 'relative' }}>
                        <div
                            style={{
                                width: `${Math.min(result.myJeonseRatio, 100)}%`,
                                height: '100%',
                                background: result.myJeonseRatio > 80 ? 'red' : (result.myJeonseRatio > 60 ? 'orange' : 'green'),
                                transition: 'width 0.5s ease'
                            }}
                        />
                        <div style={{ position: 'absolute', left: '80%', top: 0, bottom: 0, width: '2px', background: 'black' }} title="위험 기준 (80%)"></div>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8rem', marginTop: '5px' }}>
                        <span>0%</span>
                        <span>위험 기준 (80%)</span>
                        <span>100%</span>
                    </div>

                    <p style={{ marginTop: '20px', textAlign: 'center' }}>
                        {result.riskMessage}
                    </p>
                    <hr />
                    <ul style={{ fontSize: '0.9rem', color: '#555' }}>
                        <li>인근 매매 평균가: {result.avgSalePrice.toLocaleString()}만원</li>
                        <li>인근 전세 평균가: {result.avgJeonsePrice.toLocaleString()}만원 (전세가율 {result.marketJeonseRatio.toFixed(1)}%)</li>
                    </ul>
                </div>
            )}
        </Card>
    );
};

export default JeonseRiskCheck;
