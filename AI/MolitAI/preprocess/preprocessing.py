import pandas as pd
import numpy as np

def make_monthly_features(monthly_df):
    df = monthly_df.copy()

    group_cols = ["sido", "property_type", "deal_type"]

    base_cols = [
        "mean_price",
        "mean_price_per_m2",
        "txn_count",
        "kb_price_index",
        "jeonse_ratio",
        "interest_rate",
    ]

    for col in base_cols:
        for lag in [1, 3, 6]:
            df[f"{col}_lag{lag}"] = df.groupby(group_cols)[col].shift(lag)

    df["mean_price_rollmean_3"] = (
        df.groupby(group_cols)["mean_price"]
          .transform(lambda s: s.shift(1).rolling(3).mean())
    )

    df["txn_count_rollmean_3"] = (
        df.groupby(group_cols)["txn_count"]
          .transform(lambda s: s.shift(1).rolling(3).mean())
    )

    df["mean_price_pct_change_1"] = df.groupby(group_cols)["mean_price"].pct_change(1)
    df["kb_price_index_pct_change_1"] = df.groupby(group_cols)["kb_price_index"].pct_change(1)
    df["jeonse_ratio_diff_1"] = df.groupby(group_cols)["jeonse_ratio"].diff(1)
    df["interest_rate_diff_1"] = df.groupby(group_cols)["interest_rate"].diff(1)

    return df

def align_feature_columns(df, feature_cols):
    data = df.copy()
    for col in feature_cols:
        if col not in data.columns:
            data[col] = np.nan
    return data[feature_cols].copy()