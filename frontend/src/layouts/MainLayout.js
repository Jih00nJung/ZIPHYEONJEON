import React from 'react';
import Header from './Header';
import '../Ziphyeonjeon.css';

const MainLayout = ({ children }) => {
    return (
        <div className="main-wrapper">
            <Header />
            <main>
                {children}
            </main>
        </div>
    );
};

export default MainLayout;
