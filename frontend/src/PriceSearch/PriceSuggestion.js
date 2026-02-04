import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { suggestPrice } from '../api/priceApi';

const PriceSuggestion = () => {
    const [formData, setFormData] = useState({
        address: '',
        area_m2: '',
        built_year: '',
        floor: '',
        current_price: ''
    });
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSuggest = async () => {
        if (!formData.address || !formData.area_m2) {
            alert('주소와 전용면적은 필수 입력입니다.');
            return;
        }
        setLoading(true);
        try {
            const request = {
                address: formData.address,
                area_m2: parseFloat(formData.area_m2),
                market_data: {
                    built_year: formData.built_year ? parseInt(formData.built_year) : null,
                    floor: formData.floor ? parseInt(formData.floor) : null,
                    current_price: formData.current_price ? parseInt(formData.current_price) : null
                }
            };
            const data = await suggestPrice(request);
            setResult(data);
        } catch (error) {
            alert('AI 분석 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="ai-suggestion-card">
            <h2 style={{ color: '#198754' }}>AI 적정 가격 제안 (P-008)</h2>
            <p>매물 정보를 입력하면 AI가 주변 시세와 특성을 분석하여 적정 가격을 산출합니다.</p>

            <div className="suggestion-form" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', maxWidth: '600px', margin: '0 auto' }}>
                <div style={{ gridColumn: '1 / -1' }}>
                    <Input
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        placeholder="지역 (예: 강남구 역삼동)"
                        label="위치"
                    />
                </div>
                <Input
                    name="area_m2"
                    value={formData.area_m2}
                    onChange={handleChange}
                    placeholder="면적 (m²)"
                    label="전용면적"
                    type="number"
                />
                <Input
                    name="current_price"
                    value={formData.current_price}
                    onChange={handleChange}
                    placeholder="현재 호가 (선택)"
                    label="현재 가격(만원)"
                    type="number"
                />
                <Input
                    name="built_year"
                    value={formData.built_year}
                    onChange={handleChange}
                    placeholder="YYYY (선택)"
                    label="건축년도"
                    type="number"
                />
                <Input
                    name="floor"
                    value={formData.floor}
                    onChange={handleChange}
                    placeholder="층수 (선택)"
                    label="층수"
                    type="number"
                />

                <div style={{ gridColumn: '1 / -1', marginTop: '10px' }}>
                    <Button onClick={handleSuggest} disabled={loading} style={{ width: '100%', backgroundColor: '#198754' }}>
                        {loading ? 'AI 분석 중...' : '적정가 분석'}
                    </Button>
                </div>
            </div>

            {result && (
                <div className="ai-result" style={{ mt: '30px', padding: '20px', borderRadius: '10px', background: '#f0fff4', border: '1px solid #c3e6cb', marginTop: '20px' }}>
                    <div className="result-header" style={{ textAlign: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: '0 0 10px 0', color: '#155724' }}>분석 결과</h3>
                        <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#198754' }}>
                            {result.suggested_price.toLocaleString()} 만원
                        </div>
                        {result.grade && (
                            <div style={{
                                display: 'inline-block',
                                padding: '5px 15px',
                                borderRadius: '20px',
                                background: result.grade.includes('저평가') ? '#d4edda' : (result.grade.includes('고평가') ? '#fff3cd' : '#d1ecf1'),
                                color: result.grade.includes('저평가') ? '#155724' : (result.grade.includes('고평가') ? '#856404' : '#0c5460'),
                                marginTop: '10px', fontWeight: 'bold'
                            }}>
                                판단: {result.grade}
                            </div>
                        )}
                    </div>

                    <div className="result-details" style={{ background: 'white', padding: '15px', borderRadius: '8px' }}>
                        <h4 style={{ borderBottom: '1px solid #eee', paddingBottom: '10px', marginBottom: '10px' }}>산출 근거 ({result.calculation_basis.algorithm_version})</h4>
                        <ul style={{ listStyle: 'none', padding: 0 }}>
                            <li style={{ padding: '5px 0', display: 'flex', justifyContent: 'space-between' }}>
                                <span>기준 시세</span>
                                <span>{result.calculation_basis.avg_market_price}</span>
                            </li>
                            {result.calculation_basis.adjustments.map((adj, idx) => (
                                <li key={idx} style={{ padding: '5px 0', display: 'flex', justifyContent: 'space-between', color: adj.includes('(+') ? 'blue' : 'red' }}>
                                    <span>- 보정 요인 {idx + 1}</span>
                                    <span>{adj}</span>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}
        </Card>
    );
};

export default PriceSuggestion;
