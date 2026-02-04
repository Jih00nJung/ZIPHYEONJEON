import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { searchMolitTrade } from '../api/priceApi';

const RealTradeSearch = () => {
    const [formData, setFormData] = useState({
        sigunguName: '',
        buildingType: '아파트',
        dealYearMonth: '202412' // Default
    });
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSearch = async () => {
        if (!formData.sigunguName) {
            alert('지역명(구)을 입력해주세요.');
            return;
        }
        setLoading(true);
        setError(null);
        try {
            // API expects: sigungu_name, building_type, deal_year_month (YYYYMM)
            const params = {
                sigungu_name: formData.sigunguName,
                building_type: formData.buildingType,
                deal_year_month: formData.dealYearMonth
            };
            const data = await searchMolitTrade(params);
            setResults(data);
            if (data.length === 0) {
                alert('검색 결과가 없습니다.');
            }
        } catch (err) {
            setError('데이터를 불러오는 중 오류가 발생했습니다.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="price-search-card">
            <h2>국토부 실거래가 조회 (P-001)</h2>
            <div className="search-form" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', alignItems: 'center', marginBottom: '20px' }}>
                <Input
                    name="sigunguName"
                    value={formData.sigunguName}
                    onChange={handleChange}
                    placeholder="지역명 (예: 강남구)"
                    label="지역"
                />
                <div className="form-group">
                    <label style={{ display: 'block', fontSize: '14px', marginBottom: '5px', fontWeight: 'bold' }}>건물 유형</label>
                    <select
                        name="buildingType"
                        value={formData.buildingType}
                        onChange={handleChange}
                        style={{ padding: '10px', borderRadius: '4px', border: '1px solid #ddd' }}
                    >
                        <option value="아파트">아파트</option>
                        <option value="빌라">빌라/연립</option>
                        <option value="오피스텔">오피스텔</option>
                    </select>
                </div>
                <Input
                    name="dealYearMonth"
                    value={formData.dealYearMonth}
                    onChange={handleChange}
                    placeholder="YYYYMM"
                    label="거래년월"
                />
                <Button onClick={handleSearch} disabled={loading} style={{ marginTop: '24px' }}>
                    {loading ? '검색 중...' : '조회'}
                </Button>
            </div>

            {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}

            <div className="result-list">
                {results.length > 0 ? (
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                        <thead>
                            <tr style={{ background: '#f5f5f5', borderBottom: '2px solid #ddd' }}>
                                <th style={{ padding: '10px', textAlign: 'left' }}>단지명</th>
                                <th style={{ padding: '10px', textAlign: 'left' }}>주소</th>
                                <th style={{ padding: '10px', textAlign: 'right' }}>전용면적</th>
                                <th style={{ padding: '10px', textAlign: 'right' }}>거래금액(만원)</th>
                                <th style={{ padding: '10px', textAlign: 'center' }}>계약일</th>
                            </tr>
                        </thead>
                        <tbody>
                            {results.map((item, index) => (
                                <tr key={index} style={{ borderBottom: '1px solid #eee' }}>
                                    <td style={{ padding: '10px' }}>{item.complexName}</td>
                                    <td style={{ padding: '10px' }}>{item.sigungu} {item.jibun}</td>
                                    <td style={{ padding: '10px', textAlign: 'right' }}>{item.exclusiveArea}㎡</td>
                                    <td style={{ padding: '10px', textAlign: 'right', fontWeight: 'bold', color: '#0056b3' }}>
                                        {item.dealAmountMan?.toLocaleString()}
                                    </td>
                                    <td style={{ padding: '10px', textAlign: 'center' }}>
                                        {item.contractYm}.{item.contractDay}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    !loading && <p style={{ color: '#888', textAlign: 'center' }}>조회된 데이터가 없습니다.</p>
                )}
            </div>
        </Card>
    );
};

export default RealTradeSearch;
