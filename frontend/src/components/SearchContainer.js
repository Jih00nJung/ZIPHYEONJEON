import React from "react";
import {useNavigate} from "react-router-dom";

const SearchContainer = ({ placeholderTxt, buttonName }) => {

    const navigate = useNavigate();
    const handleRiskButton = () => {
        navigate('/RiskReport');

    };

    return (
        <div className="search-container">
            <input type="text" placeholder={placeholderTxt}/>
            <button className="btn-primary" onClick={handleRiskButton}>{buttonName}</button>
        </div>
    );
}

export default SearchContainer;