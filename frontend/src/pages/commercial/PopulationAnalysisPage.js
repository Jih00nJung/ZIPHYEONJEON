import React, { useState } from 'react';
import apiClient from 'api/apiClient';
import CommercialSubNav from './CommercialSubNav';
import { 
    XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar, AreaChart, Area
} from 'recharts';

const PopulationAnalysisPage = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [syncMessage, setSyncMessage] = useState('');

    // Mockup data for UI demonstration
    const timeData = [
        { time: '00-06시', pop: 1200 },
        { time: '06-11시', pop: 4500 },
        { time: '11-14시', pop: 8900 },
        { time: '14-17시', pop: 6700 },
        { time: '17-21시', pop: 9800 },
        { time: '21-24시', pop: 3400 },
    ];

    const ageData = [
        { age: '10대', pop: 15 },
        { age: '20대', pop: 35 },
        { age: '30대', pop: 25 },
        { age: '40대', pop: 15 },
        { age: '50대 이상', pop: 10 },
    ];

    const handleSync = async () => {
        setIsLoading(true);
        setSyncMessage('');
        try {
            const res = await apiClient.post('/api/populations/sync-local');
            setSyncMessage(res.data || '동기화 요청이 성공적으로 전송되었습니다.');
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
                                <div className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center text-xl">👥</div>
                                <h3 className="text-lg font-black">유동인구 데이터 관리</h3>
                            </div>
                            
                            <p className="text-sm text-slate-500 font-medium mb-6 leading-relaxed">
                                백엔드 서버에 적재된 최신 유동인구(시간대별, 연령별) 데이터를 DB와 동기화합니다.
                                대규모 데이터이므로 백그라운드에서 진행됩니다.
                            </p>

                            <button 
                                onClick={handleSync}
                                disabled={isLoading}
                                className={`w-full py-4 rounded-2xl font-black transition-all ${
                                    isLoading 
                                    ? 'bg-slate-100 text-slate-400 cursor-not-allowed' 
                                    : 'bg-blue-600 text-white hover:bg-blue-700 hover:shadow-lg hover:shadow-blue-600/20'
                                }`}
                            >
                                {isLoading ? "동기화 요청 중..." : "최신 데이터 동기화"}
                            </button>

                            {syncMessage && (
                                <div className="mt-4 p-4 bg-slate-50 rounded-2xl text-sm font-bold text-slate-700 border border-slate-100 text-center">
                                    {syncMessage}
                                </div>
                            )}
                        </div>
                    </aside>

                    {/* 대시보드 메인 영역 (목업) */}
                    <section className="lg:col-span-8 space-y-6">
                        <div className="bg-white p-10 rounded-[48px] shadow-sm border border-slate-100">
                            <div className="flex justify-between items-center mb-8">
                                <h3 className="text-xl font-black text-slate-900 tracking-tighter">시간대별 유동인구 추이 <span className="text-slate-300 text-sm font-medium ml-2">(예시 데이터)</span></h3>
                                <span className="px-3 py-1 bg-rose-50 text-rose-600 text-[10px] font-black rounded-full uppercase tracking-wider">Preview UI</span>
                            </div>
                            <div className="h-72 w-full">
                                <ResponsiveContainer width="100%" height="100%">
                                    <AreaChart data={timeData}>
                                        <defs>
                                            <linearGradient id="colorPop" x1="0" y1="0" x2="0" y2="1">
                                                <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.3}/>
                                                <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                                            </linearGradient>
                                        </defs>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                                        <XAxis dataKey="time" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12, fontWeight: 700}} dy={10} />
                                        <YAxis hide />
                                        <Tooltip 
                                            contentStyle={{ borderRadius: '16px', border: 'none', boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1)', fontWeight: 'bold' }}
                                            cursor={{ stroke: '#cbd5e1', strokeWidth: 1, strokeDasharray: '5 5' }}
                                        />
                                        <Area type="monotone" dataKey="pop" stroke="#3b82f6" strokeWidth={4} fillOpacity={1} fill="url(#colorPop)" name="유동인구" />
                                    </AreaChart>
                                </ResponsiveContainer>
                            </div>
                        </div>

                        <div className="bg-white p-10 rounded-[48px] shadow-sm border border-slate-100">
                            <div className="flex justify-between items-center mb-8">
                                <h3 className="text-xl font-black text-slate-900 tracking-tighter">연령대별 비중 분석 <span className="text-slate-300 text-sm font-medium ml-2">(예시 데이터)</span></h3>
                            </div>
                            <div className="h-64 w-full">
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart data={ageData} layout="vertical">
                                        <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="#f1f5f9" />
                                        <XAxis type="number" hide />
                                        <YAxis dataKey="age" type="category" axisLine={false} tickLine={false} tick={{fill: '#475569', fontSize: 13, fontWeight: 800}} width={80} />
                                        <Tooltip 
                                            cursor={{fill: '#f8fafc'}}
                                            contentStyle={{ borderRadius: '16px', border: 'none', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)', fontWeight: 'bold' }}
                                        />
                                        <Bar dataKey="pop" fill="#10b981" radius={[0, 8, 8, 0]} barSize={24} name="비중(%)" />
                                    </BarChart>
                                </ResponsiveContainer>
                            </div>
                        </div>
                    </section>
                </div>
            </main>
        </div>
    );
};

export default PopulationAnalysisPage;
