import React, { useState } from 'react';
import apiClient from 'api/apiClient';
import CommercialSubNav from './CommercialSubNav';

const CommercialRentPage = () => {
    const [filters, setFilters] = useState({
        sigungu: '',
    });

    const [commercialList, setCommercialList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const handleSearch = async (e) => {
        if(e) e.preventDefault();
        if (!filters.sigungu) return alert("지역명(법정동코드 또는 시군구)을 입력하세요.");
        
        setIsLoading(true);
        try {
            // GET /api/stores?sigungu={sigungu}
            const res = await apiClient.get('/api/stores', {
                params: { sigungu: filters.sigungu }
            });
            setCommercialList(res.data || []);
        } catch (error) {
            console.error("데이터 로드 실패:", error);
            alert("서버 통신에 실패했습니다.");
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
                    <aside className="lg:col-span-4 space-y-6">
                        <form onSubmit={handleSearch} className="bg-white p-8 rounded-[40px] shadow-sm border border-slate-100">
                            <h3 className="text-lg font-black mb-6">검색 필터</h3>
                            <div className="space-y-4">
                                <input 
                                    className="w-full p-4 bg-slate-50 rounded-2xl outline-none focus:ring-2 ring-blue-500/20 font-bold"
                                    placeholder="시군구 (예: 11110 또는 강남구)"
                                    value={filters.sigungu}
                                    onChange={(e) => setFilters({...filters, sigungu: e.target.value})}
                                />
                                <button type="submit" className="w-full py-4 bg-blue-600 text-white rounded-2xl font-black transition-all hover:bg-blue-700 hover:shadow-lg hover:shadow-blue-600/20">
                                    {isLoading ? "분석 중..." : "상가 실거래가 검색"}
                                </button>
                            </div>
                        </form>

                        <div className="bg-white p-8 rounded-[40px] shadow-sm border border-slate-100">
                            <h3 className="text-lg font-black mb-4">안내사항</h3>
                            <ul className="text-sm text-slate-500 space-y-2 font-medium">
                                <li>• 법정동코드(5자리) 또는 구/동 이름을 입력하세요.</li>
                                <li>• 국토교통부 실거래가 최신 데이터를 기반으로 제공됩니다.</li>
                            </ul>
                        </div>
                    </aside>

                    <section className="lg:col-span-8 space-y-8">
                        <div className="bg-white p-10 rounded-[48px] shadow-sm border border-slate-100 min-h-[600px]">
                            <h3 className="text-xl font-black text-slate-900 mb-8 tracking-tighter">
                                최근 상가 실거래 내역 
                                <span className="ml-3 text-sm font-bold text-blue-600 bg-blue-50 px-3 py-1 rounded-full">
                                    총 {commercialList.length}건
                                </span>
                            </h3>
                            
                            {commercialList.length > 0 ? (
                                <div className="space-y-4 h-[600px] overflow-y-auto pr-2 custom-scrollbar">
                                    {commercialList.map((item, idx) => (
                                        <div key={idx} className="p-6 bg-slate-50 rounded-[28px] hover:bg-blue-50 transition-all border border-slate-100 hover:border-blue-100 group">
                                            <div className="flex justify-between items-start mb-3">
                                                <div>
                                                    <span className="px-2.5 py-1 bg-white text-slate-600 text-[10px] font-black rounded-lg border border-slate-200 mr-2 shadow-sm">
                                                        {item.type || '상가'}
                                                    </span>
                                                    <span className="px-2.5 py-1 bg-blue-100 text-blue-700 text-[10px] font-black rounded-lg border border-blue-200 shadow-sm">
                                                        {item.buildingUse || '용도미상'}
                                                    </span>
                                                </div>
                                                <div className="text-right">
                                                    <div className="text-xs font-bold text-slate-400">거래연월</div>
                                                    <div className="text-sm font-black text-slate-700">{item.dealYear}.{item.dealMonth}</div>
                                                </div>
                                            </div>
                                            
                                            <h4 className="font-black text-xl text-slate-800 mb-1">
                                                {item.sigungu} {item.emd} {item.jibun}
                                            </h4>
                                            
                                            <div className="grid grid-cols-3 gap-4 mt-6 pt-4 border-t border-slate-200/60">
                                                <div>
                                                    <div className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1">Floor</div>
                                                    <div className="font-black text-slate-700">{item.floor || '-'}층</div>
                                                </div>
                                                <div>
                                                    <div className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1">Area</div>
                                                    <div className="font-black text-slate-700">{item.area} <span className="text-xs font-bold text-slate-500">㎡</span></div>
                                                </div>
                                                <div>
                                                    <div className="text-[10px] font-bold text-rose-400 uppercase tracking-widest mb-1">Amount</div>
                                                    <div className="font-black text-rose-600 text-lg leading-none">{Number(item.amount?.replace(/,/g, '')).toLocaleString()} <span className="text-xs font-bold text-rose-400">만원</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="h-full flex flex-col items-center justify-center py-32 bg-slate-50 rounded-[32px] border-2 border-dashed border-slate-200 text-slate-400 font-bold">
                                    <div className="text-4xl mb-4 opacity-50">🏢</div>
                                    검색 결과가 없습니다.<br/>
                                    <span className="text-sm font-medium mt-2">왼쪽 패널에서 시군구를 검색해보세요.</span>
                                </div>
                            )}
                        </div>
                    </section>
                </div>
            </main>
        </div>
    );
};

export default CommercialRentPage;
