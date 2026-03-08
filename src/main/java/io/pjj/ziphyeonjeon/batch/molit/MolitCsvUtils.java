package io.pjj.ziphyeonjeon.batch.molit;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

public class MolitCsvUtils {

    private MolitCsvUtils() {}

    // 엑셀에서 저장된 CSV는 보통 CP949(윈도우)인 경우 많음. UTF-8이면 알아서 잘 읽힘.
    public static final Charset[] CANDIDATE_CHARSETS = new Charset[] {
            Charset.forName("MS949"),
            Charset.forName("EUC-KR"),
            Charset.forName("UTF-8")
    };

    /** CSV 라인 split (따옴표 처리 최소 대응) */
    public static List<String> splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;

        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuote = !inQuote;
                continue;
            }

            if (c == ',' && !inQuote) {
                out.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        out.add(sb.toString().trim());
        return out;
    }

    public static String get(Map<String, Integer> headerIndex, List<String> row, String... keys) {
        for (String k : keys) {
            Integer idx = headerIndex.get(k);
            if (idx != null && idx < row.size()) {
                String v = row.get(idx);
                if (v != null) {
                    v = v.trim();
                    if (!v.isEmpty() && !v.equals("-")) return v;
                }
            }
        }
        return null;
    }

    public static Integer toInt(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equals("-")) return null;
        s = s.replaceAll("[^0-9-]", "");
        if (s.isEmpty()) return null;
        return Integer.parseInt(s);
    }

    public static Long toLongAmount(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equals("-")) return null;
        // 35,400 같은 케이스
        s = s.replace(",", "").replaceAll("[^0-9-]", "");
        if (s.isEmpty()) return null;
        return Long.parseLong(s);
    }

    public static BigDecimal toDecimal(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equals("-")) return null;
        s = s.replaceAll("[^0-9.\\-]", "");
        if (s.isEmpty()) return null;
        return new BigDecimal(s);
    }

    public static Map<String, Integer> buildHeaderIndex(List<String> headerCols) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headerCols.size(); i++) {
            map.put(headerCols.get(i).trim(), i);
        }
        return map;
    }
}
