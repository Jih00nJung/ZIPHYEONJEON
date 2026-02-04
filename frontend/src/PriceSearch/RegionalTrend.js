import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import { getRegionalTrend } from '../api/priceApi';

const RegionalTrend = () => {
    const [address, setAddress] = useState('');
    const [trendData, setTrendData] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSearch = async () => {
        if (!address) {
            alert('지역명을 입력해주세요.');
            return;
        }
        setLoading(true);
        try {
            const data = await getRegionalTrend(address);
            setTrendData(data);
        } catch (error) {
            alert('시세 추이를 불러오는데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    // Simple SVG Line Chart Logic
    const renderChart = () => {
        if (!trendData || !trendData.trends || trendData.trends.length === 0) return <p>데이터가 없습니다.</p>;

        const width = 600;
        const height = 300;
        const padding = 40;
        const chartWidth = width - padding * 2;
        const chartHeight = height - padding * 2;

        const trends = trendData.trends;
        // Calculate Min/Max for scaling
        const prices = trends.map(t => t.aptSale || 0).filter(p => p > 0);
        if (prices.length === 0) return <p>표시할 시세 데이터가 부족합니다.</p>;

        const minPrice = Math.min(...prices) * 0.9;
        const maxPrice = Math.max(...prices) * 1.1;

        const getX = (index) => padding + (index / (trends.length - 1)) * chartWidth;
        const getY = (price) => height - padding - ((price - minPrice) / (maxPrice - minPrice)) * chartHeight;

        // Create Path
        let pathD = "";
        trends.forEach((t, i) => {
            if (!t.aptSale) return;
            const x = getX(i);
            const y = getY(t.aptSale);
            if (i === 0) pathD += `M ${x} ${y}`;
            else pathD += ` L ${x} ${y}`;
        });

        return (
            <svg width="100%" height={height} viewBox={`0 0 ${width} ${height}`} style={{ border: '1px solid #eee', background: '#fff' }}>
                {/* Axis Lines */}
                <line x1={padding} y1={height - padding} x2={width - padding} y2={height - padding} stroke="#ccc" />
                <line x1={padding} y1={padding} x2={padding} y2={height - padding} stroke="#ccc" />

                {/* Grid Lines (Horizontal) */}
                {[0, 0.25, 0.5, 0.75, 1].map((ratio, i) => {
                    const y = height - padding - (ratio * chartHeight);
                    const labelPrice = minPrice + (ratio * (maxPrice - minPrice));
                    return (
                        <g key={i}>
                            <line x1={padding} y1={y} x2={width - padding} y2={y} stroke="#eee" />
                            <text x={padding - 5} y={y + 5} textAnchor="end" fontSize="10">{Math.round(labelPrice)}</text>
                        </g>
                    )
                })}

                {/* Line */}
                <path d={pathD} fill="none" stroke="#007bff" strokeWidth="2" />

                {/* Data Points */}
                {trends.map((t, i) => {
                    if (!t.aptSale) return null;
                    const x = getX(i);
                    const y = getY(t.aptSale);
                    return (
                        <g key={i}>
                            <circle cx={x} cy={y} r="4" fill="#007bff" />
                            <text x={x} y={y - 10} textAnchor="middle" fontSize="10">{t.aptSale}</text>
                            <text x={x} y={height - padding + 15} textAnchor="middle" fontSize="10">{t.period.substring(2)}</text>
                        </g>
                    );
                })}
            </svg>
        );
    };

    return (
        <Card className="trend-card">
            <h2>지역 시세 추이 (P-006)</h2>
            <p>최근 1년간의 거래 가격 변동 추이(만원/m²)를 그래프로 확인합니다.</p>
            <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                <Input
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder="지역명 (예: 강남구 역삼동)"
                />
                <Button onClick={handleSearch} disabled={loading}>조회</Button>
            </div>

            {trendData && (
                <div className="chart-area">
                    <h3>{trendData.regionName} 아파트 매매 추이</h3>
                    <div style={{ overflowX: 'auto' }}>
                        {renderChart()}
                    </div>
                </div>
            )}
        </Card>
    );
};

export default RegionalTrend;
