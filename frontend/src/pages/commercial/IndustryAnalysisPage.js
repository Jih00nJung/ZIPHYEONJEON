import React, { useState } from 'react';
import apiClient from 'api/apiClient';
import CommercialSubNav from './CommercialSubNav';

const IndustryAnalysisPage = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [syncMessage, setSyncMessage] = useState('');

    const handleSync = async () => {
        setIsLoading(true);
        setSyncMessage('');
        try {
            const res = await apiClient.post('/api/industry/sync-local');
            setSyncMessage(res.data || '점포 데이터 동기화가 성공적으로 완료되었습니다.');
        } catch (error) {
            console.error("동기화 실패:", error);
            setSyncMessage('서버 오류로 동기화에 실패했습니다.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-[#F1F5F9] p-8">
            <header className="max-w-7xl mx-auto mb-10">
                <h1 className="text-4xl font-black text-slate-900 tracking-tighter">🏢 상권 분석 시스템</h1>
                <p className="text-slate-500 font-medium mt-2">상가 입지 선정을 위한 유동인구, 업황, 임대료 통합 분석</p>
            </header>

            <main className="max-w-7xl mx-auto">
                <CommercialSubNav />

                <div className="grid lg:grid-cols-12 gap-8">
                    {/* 데이터 동기화 사이드바 */}
                    <aside className="lg:col-span-4 space-y-6">
                        <div className="bg-white p-8 rounded-[40px] shadow-sm border border-slate-100">
                            <div className="flex items-center gap-3 mb-6">
                                <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-xl">🏪</div>
                                <h3 className="text-lg font-black">상권 점포 데이터 관리</h3>
                            </div>
                            
                            <p className="text-sm text-slate-500 font-medium mb-6 leading-relaxed">
                                로컬에 저장된 최신 '서울시 상권분석서비스(점포-행정동)' CSV 데이터를 
                                DB에 일괄 저장 및 동기화합니다.
                            </p>

                            <button 
                                onClick={handleSync}
                                disabled={isLoading}
                                className={`w-full py-4 rounded-2xl font-black transition-all ${
                                    isLoading 
                                    ? 'bg-slate-100 text-slate-400 cursor-not-allowed' 
                                    : 'bg-emerald-600 text-white hover:bg-emerald-700 hover:shadow-lg hover:shadow-emerald-600/20'
                                }`}
                            >
                                {isLoading ? "데이터 갱신 중..." : "상권 점포 데이터 갱신"}
                            </button>

                            {syncMessage && (
                                <div className="mt-4 p-4 bg-slate-50 rounded-2xl text-sm font-bold text-slate-700 border border-slate-100 text-center">
                                    {syncMessage}
                                </div>
                            )}
                        </div>
                    </aside>

                    {/* 소상공인365_업소현황 서비스 iframe 영역 */}
                    <section className="lg:col-span-8 space-y-6">
                        <div className="bg-white p-10 rounded-[48px] shadow-sm border border-slate-100 h-full min-h-[600px] flex flex-col">
                            <div className="flex justify-between items-center mb-6">
                                <h3 className="text-xl font-black text-slate-900 tracking-tighter">소상공인365 업소현황 서비스</h3>
                                <span className="px-3 py-1 bg-emerald-50 text-emerald-600 text-[10px] font-black rounded-full uppercase tracking-wider">iFrame Integration</span>
                            </div>
                            
                            <div className="w-full flex-1 rounded-3xl overflow-hidden border-2 border-slate-100 bg-slate-50 relative">
                                {/* 외부 서비스 연동 iframe */}
                                <iframe 
                                    src="https://sg.sbiz.or.kr/godo/index.sg" 
                                    title="소상공인365 업소현황 서비스"
                                    className="absolute top-0 left-0 w-full h-full border-none"
                                    sandbox="allow-scripts allow-same-origin allow-popups allow-forms"
                                ></iframe>
                            </div>
                        </div>
                    </section>
                </div>
            </main>
        </div>
    );
};

export default IndustryAnalysisPage;
