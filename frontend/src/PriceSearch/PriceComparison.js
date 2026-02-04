import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { comparePrices } from '../api/priceApi';

const PriceComparison = () => {
    const [items, setItems] = useState([
        { address: '', area_m2: '', transaction_type: '아파트', targetPrice: '' },
        { address: '', area_m2: '', transaction_type: '아파트', targetPrice: '' }
    ]);
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleChange = (index, e) => {
        const { name, value } = e.target;
        const newItems = [...items];
        newItems[index] = { ...newItems[index], [name]: value };
        setItems(newItems);
    };

    const handleCompare = async () => {
        // Validation
        for (let item of items) {
            if (!item.address || !item.area_m2 || !item.targetPrice) {
                alert('모든 정보를 입력해주세요.');
                return;
            }
        }

        setLoading(true);
        try {
            const request = {
                targets: items.map(i => ({
                    address: i.address,
                    area_m2: parseFloat(i.area_m2),
                    transaction_type: i.transaction_type,
                    targetPrice: parseInt(i.targetPrice)
                }))
            };
            const data = await comparePrices(request);
            setResults(data);
        } catch (error) {
            alert('비교 분석 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="price-compare-card">
            <h2>매물 가격 비교 (P-004)</h2>
            <p>두 매물의 상세 정보를 입력하여 비교해보세요.</p>

            <div className="compare-inputs" style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
                {items.map((item, idx) => (
                    <div key={idx} className="compare-item" style={{ flex: 1, minWidth: '300px', border: '1px solid #ddd', padding: '15px', borderRadius: '8px' }}>
                        <h3>매물 {idx + 1}</h3>
                        <Input
                            name="address"
                            value={item.address}
                            onChange={(e) => handleChange(idx, e)}
                            placeholder="주소 (예: 역삼동 123)"
                            label="주소"
                        />
                        <Input
                            name="area_m2"
                            value={item.area_m2}
                            onChange={(e) => handleChange(idx, e)}
                            placeholder="전용면적 (m²)"
                            label="면적"
                            type="number"
                        />
                        <Input
                            name="targetPrice"
                            value={item.targetPrice}
                            onChange={(e) => handleChange(idx, e)}
                            placeholder="매매가 (만원)"
                            label="가격"
                            type="number"
                        />
                        <div style={{ marginTop: '10px' }}>
                            <label style={{ marginRight: '10px' }}>유형</label>
                            <select
                                name="transaction_type"
                                value={item.transaction_type}
                                onChange={(e) => handleChange(idx, e)}
                                style={{ padding: '5px' }}
                            >
                                <option value="아파트">아파트</option>
                                <option value="빌라">빌라/연립</option>
                                <option value="오피스텔">오피스텔</option>
                            </select>
                        </div>
                    </div>
                ))}
            </div>

            <Button onClick={handleCompare} disabled={loading} style={{ marginTop: '20px', width: '100%' }}>
                {loading ? '분석 중...' : '비교하기'}
            </Button>

            {results.length > 0 && (
                <div className="compare-results" style={{ marginTop: '30px' }}>
                    <h3>비교 결과</h3>
                    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                        <thead>
                            <tr style={{ background: '#f8f9fa' }}>
                                <th style={{ padding: '10px', border: '1px solid #ddd' }}>구분</th>
                                {results.map((r, i) => (
                                    <th key={i} style={{ padding: '10px', border: '1px solid #ddd' }}>매물 {i + 1}</th>
                                ))}
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style={{ padding: '10px', border: '1px solid #ddd', fontWeight: 'bold' }}>주소</td>
                                {results.map((r, i) => <td key={i} style={{ padding: '10px', border: '1px solid #ddd' }}>{r.address}</td>)}
                            </tr>
                            <tr>
                                <td style={{ padding: '10px', border: '1px solid #ddd', fontWeight: 'bold' }}>호가</td>
                                {results.map((r, i) => <td key={i} style={{ padding: '10px', border: '1px solid #ddd' }}>{r.targetPrice.toLocaleString()}만원</td>)}
                            </tr>
                            <tr>
                                <td style={{ padding: '10px', border: '1px solid #ddd', fontWeight: 'bold' }}>평균 시세</td>
                                {results.map((r, i) => <td key={i} style={{ padding: '10px', border: '1px solid #ddd' }}>{r.averageMarketPrice > 0 ? r.averageMarketPrice.toLocaleString() + '만원' : '-'}</td>)}
                            </tr>
                            <tr>
                                <td style={{ padding: '10px', border: '1px solid #ddd', fontWeight: 'bold' }}>가격 차이</td>
                                {results.map((r, i) => (
                                    <td key={i} style={{ padding: '10px', border: '1px solid #ddd', color: r.priceDiff > 0 ? 'red' : 'blue' }}>
                                        {r.priceDiff > 0 ? `+${r.priceDiff.toLocaleString()}만원` : `${r.priceDiff.toLocaleString()}만원`}
                                        <br />
                                        <small>({r.diffPercent.toFixed(1)}%)</small>
                                    </td>
                                ))}
                            </tr>
                            <tr>
                                <td style={{ padding: '10px', border: '1px solid #ddd', fontWeight: 'bold' }}>분석</td>
                                {results.map((r, i) => <td key={i} style={{ padding: '10px', border: '1px solid #ddd' }}>{r.analysisMessage}</td>)}
                            </tr>
                        </tbody>
                    </table>
                </div>
            )}
        </Card>
    );
};

export default PriceComparison;
