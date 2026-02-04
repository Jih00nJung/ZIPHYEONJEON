import React, { useState } from 'react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';

import { downloadTradeDataUrl } from '../api/priceApi';

const DataDownload = () => {
    const [sigunguCode, setSigunguCode] = useState('11680'); // Default Kangnam
    const [format, setFormat] = useState('csv');

    const handleDownload = () => {
        const url = downloadTradeDataUrl('11', sigunguCode, format);
        window.open(url, '_blank');
    };

    return (
        <Card>
            <h2>실거래가 데이터 다운로드 (P-007)</h2>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '15px', maxWidth: '400px' }}>
                <div>
                    <label>지역 코드 (구)</label>
                    <select
                        value={sigunguCode}
                        onChange={(e) => setSigunguCode(e.target.value)}
                        style={{ width: '100%', padding: '10px', marginTop: '5px' }}
                    >
                        <option value="11680">강남구 (11680)</option>
                        <option value="11650">서초구 (11650)</option>
                        <option value="11440">마포구 (11440)</option>
                    </select>
                </div>
                <div>
                    <label>파일 형식</label>
                    <select
                        value={format}
                        onChange={(e) => setFormat(e.target.value)}
                        style={{ width: '100%', padding: '10px', marginTop: '5px' }}
                    >
                        <option value="csv">CSV (.csv)</option>
                        <option value="excel">Excel (.xlsx)</option>
                    </select>
                </div>
                <Button onClick={handleDownload} variant="primary">
                    다운로드 시작
                </Button>
            </div>
        </Card>
    );
};

export default DataDownload;
