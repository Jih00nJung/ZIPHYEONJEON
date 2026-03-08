import React from "react";
import { useNavigate } from "react-router-dom";
import Button from "./common/Button";

const SearchContainer = ({ placeholderTxt, buttonName }) => {
    const navigate = useNavigate();
    const handleRiskButton = () => {
        navigate('/RiskReport');
    };

    return (
        <div className="search-container-v2">
            <div className="search-input-wrapper-v2">
                <span className="material-symbols-outlined search-icon-v2">search</span>
                <input
                    type="text"
                    className="search-input-v2"
                    placeholder={placeholderTxt}
                />
            </div>
            <Button
                variant="primary"
                size="lg"
                onClick={handleRiskButton}
                className="search-btn-v2"
            >
                {buttonName}
            </Button>
        </div>
    );
}

export default SearchContainer;