package com.cheweibao.liuliu.data;

import java.util.List;

/**
 * Created by user on 2018/4/13.
 */

public class CarDealerOfferBean {


    /**
     * success : true
     * content : {"Estimate":{"ncmsrp":"30.00","c2BPrices":{"b":{"mid":"4.07","low":"3.66",
     * "up":"4.44"},"a":{"low":"4.18","up":"4.55","mid":"4.35"},"c":{"up":"3.82","low":"3.18",
     * "mid":"3.57"}},"c2CPrices":{"c":{"up":"4.24","mid":"3.97","low":"3.53"},"b":{"mid":"4.52",
     * "up":"4.94","low":"4.07"},"a":{"mid":"4.83","up":"5.06","low":"4.65"}},
     * "b2BPrices":{"b":{"up":"4.62","low":"3.81","mid":"4.23"},"c":{"low":"3.30","up":"3.97",
     * "mid":"3.72"},"a":{"up":"4.73","mid":"4.52","low":"4.35"}},"trimId":"10000818",
     * "b2CPrices":{"c":{"low":"3.68","up":"4.43","mid":"4.15"},"a":{"up":"5.28","mid":"5.04",
     * "low":"4.85"},"b":{"mid":"4.72","up":"5.15","low":"4.25"}}},
     * "ResidualRatioRanking":[{"b2BPrices":"4.18","c2BPrices":"4.02","b2CPrices":"4.66",
     * "trimId":"10000818","ncmsrp":"30.00","Month":"2018-05-01"},{"trimId":"10000818",
     * "Month":"2018-06-01","ncmsrp":"30.00","b2BPrices":"4.13","c2BPrices":"3.98","b2CPrices":"4
     * .61"},{"Month":"2018-07-01","b2BPrices":"4.08","c2BPrices":"3.92","ncmsrp":"30.00",
     * "trimId":"10000818","b2CPrices":"4.55"},{"b2BPrices":"4.04","b2CPrices":"4.51",
     * "trimId":"10000818","c2BPrices":"3.89","ncmsrp":"30.00","Month":"2018-08-01"},
     * {"b2CPrices":"4.45","Month":"2018-09-01","trimId":"10000818","c2BPrices":"3.84",
     * "b2BPrices":"3.99","ncmsrp":"30.00"},{"c2BPrices":"3.79","Month":"2018-10-01",
     * "b2BPrices":"3.94","b2CPrices":"4.40","trimId":"10000818","ncmsrp":"30.00"},
     * {"b2CPrices":"4.35","ncmsrp":"30.00","b2BPrices":"3.90","c2BPrices":"3.75",
     * "trimId":"10000818","Month":"2018-11-01"},{"ncmsrp":"30.00","b2BPrices":"3.86",
     * "c2BPrices":"3.71","b2CPrices":"4.30","Month":"2018-12-01","trimId":"10000818"},
     * {"trimId":"10000818","b2BPrices":"3.81","ncmsrp":"30.00","c2BPrices":"3.66","b2CPrices":"4
     * .25","Month":"2019-01-01"},{"b2BPrices":"3.77","c2BPrices":"3.62","trimId":"10000818",
     * "b2CPrices":"4.20","Month":"2019-02-01","ncmsrp":"30.00"},{"ncmsrp":"30.00",
     * "trimId":"10000818","c2BPrices":"3.59","b2CPrices":"4.16","b2BPrices":"3.73",
     * "Month":"2019-03-01"},{"Month":"2019-04-01","ncmsrp":"30.00","trimId":"10000818",
     * "b2BPrices":"3.69","c2BPrices":"3.54","b2CPrices":"4.11"},{"b2CPrices":"4.06",
     * "Month":"2019-05-01","b2BPrices":"3.64","c2BPrices":"3.50","ncmsrp":"30.00",
     * "trimId":"10000818"},{"b2CPrices":"4.02","ncmsrp":"30.00","trimId":"10000818",
     * "c2BPrices":"3.46","Month":"2019-06-01","b2BPrices":"3.60"},{"ncmsrp":"30.00",
     * "b2BPrices":"3.55","trimId":"10000818","Month":"2019-07-01","c2BPrices":"3.42",
     * "b2CPrices":"3.96"},{"b2CPrices":"3.93","trimId":"10000818","c2BPrices":"3.39",
     * "ncmsrp":"30.00","b2BPrices":"3.52","Month":"2019-08-01"},{"b2CPrices":"3.88",
     * "trimId":"10000818","c2BPrices":"3.34","Month":"2019-09-01","b2BPrices":"3.48",
     * "ncmsrp":"30.00"},{"Month":"2019-10-01","b2CPrices":"3.83","trimId":"10000818",
     * "ncmsrp":"30.00","c2BPrices":"3.30","b2BPrices":"3.44"},{"b2CPrices":"3.79","b2BPrices":"3
     * .40","c2BPrices":"3.27","trimId":"10000818","ncmsrp":"30.00","Month":"2019-11-01"},
     * {"trimId":"10000818","c2BPrices":"3.24","b2BPrices":"3.36","b2CPrices":"3.75","ncmsrp":"30
     * .00","Month":"2019-12-01"},{"ncmsrp":"30.00","trimId":"10000818","c2BPrices":"3.20",
     * "b2CPrices":"3.71","b2BPrices":"3.33","Month":"2020-01-01"},{"b2CPrices":"3.67",
     * "c2BPrices":"3.16","trimId":"10000818","Month":"2020-02-01","b2BPrices":"3.29",
     * "ncmsrp":"30.00"},{"Month":"2020-03-01","b2BPrices":"3.26","b2CPrices":"3.63",
     * "trimId":"10000818","ncmsrp":"30.00","c2BPrices":"3.13"},{"c2BPrices":"3.09",
     * "b2BPrices":"3.22","b2CPrices":"3.59","trimId":"10000818","ncmsrp":"30.00",
     * "Month":"2020-04-01"},{"b2BPrices":"3.18","ncmsrp":"30.00","Month":"2020-05-01",
     * "c2BPrices":"3.06","b2CPrices":"3.55","trimId":"10000818"},{"Month":"2020-06-01",
     * "trimId":"10000818","b2CPrices":"3.51","ncmsrp":"30.00","b2BPrices":"3.15","c2BPrices":"3
     * .03"},{"b2CPrices":"3.46","c2BPrices":"2.99","Month":"2020-07-01","trimId":"10000818",
     * "ncmsrp":"30.00","b2BPrices":"3.10"},{"ncmsrp":"30.00","Month":"2020-08-01","b2BPrices":"3
     * .08","b2CPrices":"3.43","c2BPrices":"2.96","trimId":"10000818"},{"b2BPrices":"3.04",
     * "trimId":"10000818","Month":"2020-09-01","b2CPrices":"3.39","ncmsrp":"30.00",
     * "c2BPrices":"2.92"},{"b2CPrices":"3.35","trimId":"10000818","c2BPrices":"2.89",
     * "b2BPrices":"3.00","Month":"2020-10-01","ncmsrp":"30.00"},{"b2CPrices":"3.31",
     * "c2BPrices":"2.86","trimId":"10000818","ncmsrp":"30.00","b2BPrices":"2.97",
     * "Month":"2020-11-01"},{"trimId":"10000818","b2CPrices":"3.28","b2BPrices":"2.94",
     * "ncmsrp":"30.00","c2BPrices":"2.83","Month":"2020-12-01"},{"b2BPrices":"2.91","ncmsrp":"30
     * .00","b2CPrices":"3.24","c2BPrices":"2.80","Month":"2021-01-01","trimId":"10000818"},
     * {"c2BPrices":"2.77","Month":"2021-02-01","b2CPrices":"3.21","ncmsrp":"30.00",
     * "b2BPrices":"2.88","trimId":"10000818"},{"c2BPrices":"2.74","trimId":"10000818",
     * "b2CPrices":"3.18","ncmsrp":"30.00","b2BPrices":"2.85","Month":"2021-03-01"},{"ncmsrp":"30
     * .00","b2CPrices":"3.14","c2BPrices":"2.71","Month":"2021-04-01","b2BPrices":"2.81",
     * "trimId":"10000818"}]}
     */

    private boolean success;
    private ContentBean content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * Estimate : {"ncmsrp":"30.00","c2BPrices":{"b":{"mid":"4.07","low":"3.66","up":"4.44"},
         * "a":{"low":"4.18","up":"4.55","mid":"4.35"},"c":{"up":"3.82","low":"3.18","mid":"3
         * .57"}},"c2CPrices":{"c":{"up":"4.24","mid":"3.97","low":"3.53"},"b":{"mid":"4.52",
         * "up":"4.94","low":"4.07"},"a":{"mid":"4.83","up":"5.06","low":"4.65"}},
         * "b2BPrices":{"b":{"up":"4.62","low":"3.81","mid":"4.23"},"c":{"low":"3.30","up":"3
         * .97","mid":"3.72"},"a":{"up":"4.73","mid":"4.52","low":"4.35"}},"trimId":"10000818",
         * "b2CPrices":{"c":{"low":"3.68","up":"4.43","mid":"4.15"},"a":{"up":"5.28","mid":"5
         * .04","low":"4.85"},"b":{"mid":"4.72","up":"5.15","low":"4.25"}}}
         * ResidualRatioRanking : [{"b2BPrices":"4.18","c2BPrices":"4.02","b2CPrices":"4.66",
         * "trimId":"10000818","ncmsrp":"30.00","Month":"2018-05-01"},{"trimId":"10000818",
         * "Month":"2018-06-01","ncmsrp":"30.00","b2BPrices":"4.13","c2BPrices":"3.98",
         * "b2CPrices":"4.61"},{"Month":"2018-07-01","b2BPrices":"4.08","c2BPrices":"3.92",
         * "ncmsrp":"30.00","trimId":"10000818","b2CPrices":"4.55"},{"b2BPrices":"4.04",
         * "b2CPrices":"4.51","trimId":"10000818","c2BPrices":"3.89","ncmsrp":"30.00",
         * "Month":"2018-08-01"},{"b2CPrices":"4.45","Month":"2018-09-01","trimId":"10000818",
         * "c2BPrices":"3.84","b2BPrices":"3.99","ncmsrp":"30.00"},{"c2BPrices":"3.79",
         * "Month":"2018-10-01","b2BPrices":"3.94","b2CPrices":"4.40","trimId":"10000818",
         * "ncmsrp":"30.00"},{"b2CPrices":"4.35","ncmsrp":"30.00","b2BPrices":"3.90",
         * "c2BPrices":"3.75","trimId":"10000818","Month":"2018-11-01"},{"ncmsrp":"30.00",
         * "b2BPrices":"3.86","c2BPrices":"3.71","b2CPrices":"4.30","Month":"2018-12-01",
         * "trimId":"10000818"},{"trimId":"10000818","b2BPrices":"3.81","ncmsrp":"30.00",
         * "c2BPrices":"3.66","b2CPrices":"4.25","Month":"2019-01-01"},{"b2BPrices":"3.77",
         * "c2BPrices":"3.62","trimId":"10000818","b2CPrices":"4.20","Month":"2019-02-01",
         * "ncmsrp":"30.00"},{"ncmsrp":"30.00","trimId":"10000818","c2BPrices":"3.59",
         * "b2CPrices":"4.16","b2BPrices":"3.73","Month":"2019-03-01"},{"Month":"2019-04-01",
         * "ncmsrp":"30.00","trimId":"10000818","b2BPrices":"3.69","c2BPrices":"3.54",
         * "b2CPrices":"4.11"},{"b2CPrices":"4.06","Month":"2019-05-01","b2BPrices":"3.64",
         * "c2BPrices":"3.50","ncmsrp":"30.00","trimId":"10000818"},{"b2CPrices":"4.02",
         * "ncmsrp":"30.00","trimId":"10000818","c2BPrices":"3.46","Month":"2019-06-01",
         * "b2BPrices":"3.60"},{"ncmsrp":"30.00","b2BPrices":"3.55","trimId":"10000818",
         * "Month":"2019-07-01","c2BPrices":"3.42","b2CPrices":"3.96"},{"b2CPrices":"3.93",
         * "trimId":"10000818","c2BPrices":"3.39","ncmsrp":"30.00","b2BPrices":"3.52",
         * "Month":"2019-08-01"},{"b2CPrices":"3.88","trimId":"10000818","c2BPrices":"3.34",
         * "Month":"2019-09-01","b2BPrices":"3.48","ncmsrp":"30.00"},{"Month":"2019-10-01",
         * "b2CPrices":"3.83","trimId":"10000818","ncmsrp":"30.00","c2BPrices":"3.30",
         * "b2BPrices":"3.44"},{"b2CPrices":"3.79","b2BPrices":"3.40","c2BPrices":"3.27",
         * "trimId":"10000818","ncmsrp":"30.00","Month":"2019-11-01"},{"trimId":"10000818",
         * "c2BPrices":"3.24","b2BPrices":"3.36","b2CPrices":"3.75","ncmsrp":"30.00",
         * "Month":"2019-12-01"},{"ncmsrp":"30.00","trimId":"10000818","c2BPrices":"3.20",
         * "b2CPrices":"3.71","b2BPrices":"3.33","Month":"2020-01-01"},{"b2CPrices":"3.67",
         * "c2BPrices":"3.16","trimId":"10000818","Month":"2020-02-01","b2BPrices":"3.29",
         * "ncmsrp":"30.00"},{"Month":"2020-03-01","b2BPrices":"3.26","b2CPrices":"3.63",
         * "trimId":"10000818","ncmsrp":"30.00","c2BPrices":"3.13"},{"c2BPrices":"3.09",
         * "b2BPrices":"3.22","b2CPrices":"3.59","trimId":"10000818","ncmsrp":"30.00",
         * "Month":"2020-04-01"},{"b2BPrices":"3.18","ncmsrp":"30.00","Month":"2020-05-01",
         * "c2BPrices":"3.06","b2CPrices":"3.55","trimId":"10000818"},{"Month":"2020-06-01",
         * "trimId":"10000818","b2CPrices":"3.51","ncmsrp":"30.00","b2BPrices":"3.15",
         * "c2BPrices":"3.03"},{"b2CPrices":"3.46","c2BPrices":"2.99","Month":"2020-07-01",
         * "trimId":"10000818","ncmsrp":"30.00","b2BPrices":"3.10"},{"ncmsrp":"30.00",
         * "Month":"2020-08-01","b2BPrices":"3.08","b2CPrices":"3.43","c2BPrices":"2.96",
         * "trimId":"10000818"},{"b2BPrices":"3.04","trimId":"10000818","Month":"2020-09-01",
         * "b2CPrices":"3.39","ncmsrp":"30.00","c2BPrices":"2.92"},{"b2CPrices":"3.35",
         * "trimId":"10000818","c2BPrices":"2.89","b2BPrices":"3.00","Month":"2020-10-01",
         * "ncmsrp":"30.00"},{"b2CPrices":"3.31","c2BPrices":"2.86","trimId":"10000818",
         * "ncmsrp":"30.00","b2BPrices":"2.97","Month":"2020-11-01"},{"trimId":"10000818",
         * "b2CPrices":"3.28","b2BPrices":"2.94","ncmsrp":"30.00","c2BPrices":"2.83",
         * "Month":"2020-12-01"},{"b2BPrices":"2.91","ncmsrp":"30.00","b2CPrices":"3.24",
         * "c2BPrices":"2.80","Month":"2021-01-01","trimId":"10000818"},{"c2BPrices":"2.77",
         * "Month":"2021-02-01","b2CPrices":"3.21","ncmsrp":"30.00","b2BPrices":"2.88",
         * "trimId":"10000818"},{"c2BPrices":"2.74","trimId":"10000818","b2CPrices":"3.18",
         * "ncmsrp":"30.00","b2BPrices":"2.85","Month":"2021-03-01"},{"ncmsrp":"30.00",
         * "b2CPrices":"3.14","c2BPrices":"2.71","Month":"2021-04-01","b2BPrices":"2.81",
         * "trimId":"10000818"}]
         */

        private EstimateBean Estimate;
        private List<ResidualRatioRankingBean> ResidualRatioRanking;

        public EstimateBean getEstimate() {
            return Estimate;
        }

        public void setEstimate(EstimateBean Estimate) {
            this.Estimate = Estimate;
        }

        public List<ResidualRatioRankingBean> getResidualRatioRanking() {
            return ResidualRatioRanking;
        }

        public void setResidualRatioRanking(List<ResidualRatioRankingBean> ResidualRatioRanking) {
            this.ResidualRatioRanking = ResidualRatioRanking;
        }

        public static class EstimateBean {
            /**
             * ncmsrp : 30.00
             * c2BPrices : {"b":{"mid":"4.07","low":"3.66","up":"4.44"},"a":{"low":"4.18","up":"4
             * .55","mid":"4.35"},"c":{"up":"3.82","low":"3.18","mid":"3.57"}}
             * c2CPrices : {"c":{"up":"4.24","mid":"3.97","low":"3.53"},"b":{"mid":"4.52","up":"4
             * .94","low":"4.07"},"a":{"mid":"4.83","up":"5.06","low":"4.65"}}
             * b2BPrices : {"b":{"up":"4.62","low":"3.81","mid":"4.23"},"c":{"low":"3.30","up":"3
             * .97","mid":"3.72"},"a":{"up":"4.73","mid":"4.52","low":"4.35"}}
             * trimId : 10000818
             * b2CPrices : {"c":{"low":"3.68","up":"4.43","mid":"4.15"},"a":{"up":"5.28","mid":"5
             * .04","low":"4.85"},"b":{"mid":"4.72","up":"5.15","low":"4.25"}}
             */

            private String ncmsrp;
            private C2BPricesBean c2BPrices;
            private C2CPricesBean c2CPrices;
            private B2BPricesBean b2BPrices;
            private String trimId;
            private B2CPricesBean b2CPrices;

            public String getNcmsrp() {
                return ncmsrp;
            }

            public void setNcmsrp(String ncmsrp) {
                this.ncmsrp = ncmsrp;
            }

            public C2BPricesBean getC2BPrices() {
                return c2BPrices;
            }

            public void setC2BPrices(C2BPricesBean c2BPrices) {
                this.c2BPrices = c2BPrices;
            }

            public C2CPricesBean getC2CPrices() {
                return c2CPrices;
            }

            public void setC2CPrices(C2CPricesBean c2CPrices) {
                this.c2CPrices = c2CPrices;
            }

            public B2BPricesBean getB2BPrices() {
                return b2BPrices;
            }

            public void setB2BPrices(B2BPricesBean b2BPrices) {
                this.b2BPrices = b2BPrices;
            }

            public String getTrimId() {
                return trimId;
            }

            public void setTrimId(String trimId) {
                this.trimId = trimId;
            }

            public B2CPricesBean getB2CPrices() {
                return b2CPrices;
            }

            public void setB2CPrices(B2CPricesBean b2CPrices) {
                this.b2CPrices = b2CPrices;
            }

            public static class C2BPricesBean {
                /**
                 * b : {"mid":"4.07","low":"3.66","up":"4.44"}
                 * a : {"low":"4.18","up":"4.55","mid":"4.35"}
                 * c : {"up":"3.82","low":"3.18","mid":"3.57"}
                 */

                private BBean b;
                private ABean a;
                private CBean c;

                public BBean getB() {
                    return b;
                }

                public void setB(BBean b) {
                    this.b = b;
                }

                public ABean getA() {
                    return a;
                }

                public void setA(ABean a) {
                    this.a = a;
                }

                public CBean getC() {
                    return c;
                }

                public void setC(CBean c) {
                    this.c = c;
                }

                public static class BBean {
                    /**
                     * mid : 4.07
                     * low : 3.66
                     * up : 4.44
                     */

                    private String mid;
                    private String low;
                    private String up;

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }
                }

                public static class ABean {
                    /**
                     * low : 4.18
                     * up : 4.55
                     * mid : 4.35
                     */

                    private String low;
                    private String up;
                    private String mid;

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }
                }

                public static class CBean {
                    /**
                     * up : 3.82
                     * low : 3.18
                     * mid : 3.57
                     */

                    private String up;
                    private String low;
                    private String mid;

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }
                }
            }

            public static class C2CPricesBean {
                /**
                 * c : {"up":"4.24","mid":"3.97","low":"3.53"}
                 * b : {"mid":"4.52","up":"4.94","low":"4.07"}
                 * a : {"mid":"4.83","up":"5.06","low":"4.65"}
                 */

                private CBeanX c;
                private BBeanX b;
                private ABeanX a;

                public CBeanX getC() {
                    return c;
                }

                public void setC(CBeanX c) {
                    this.c = c;
                }

                public BBeanX getB() {
                    return b;
                }

                public void setB(BBeanX b) {
                    this.b = b;
                }

                public ABeanX getA() {
                    return a;
                }

                public void setA(ABeanX a) {
                    this.a = a;
                }

                public static class CBeanX {
                    /**
                     * up : 4.24
                     * mid : 3.97
                     * low : 3.53
                     */

                    private String up;
                    private String mid;
                    private String low;

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }

                public static class BBeanX {
                    /**
                     * mid : 4.52
                     * up : 4.94
                     * low : 4.07
                     */

                    private String mid;
                    private String up;
                    private String low;

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }

                public static class ABeanX {
                    /**
                     * mid : 4.83
                     * up : 5.06
                     * low : 4.65
                     */

                    private String mid;
                    private String up;
                    private String low;

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }
            }

            public static class B2BPricesBean {
                /**
                 * b : {"up":"4.62","low":"3.81","mid":"4.23"}
                 * c : {"low":"3.30","up":"3.97","mid":"3.72"}
                 * a : {"up":"4.73","mid":"4.52","low":"4.35"}
                 */

                private BBeanXX b;
                private CBeanXX c;
                private ABeanXX a;

                public BBeanXX getB() {
                    return b;
                }

                public void setB(BBeanXX b) {
                    this.b = b;
                }

                public CBeanXX getC() {
                    return c;
                }

                public void setC(CBeanXX c) {
                    this.c = c;
                }

                public ABeanXX getA() {
                    return a;
                }

                public void setA(ABeanXX a) {
                    this.a = a;
                }

                public static class BBeanXX {
                    /**
                     * up : 4.62
                     * low : 3.81
                     * mid : 4.23
                     */

                    private String up;
                    private String low;
                    private String mid;

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }
                }

                public static class CBeanXX {
                    /**
                     * low : 3.30
                     * up : 3.97
                     * mid : 3.72
                     */

                    private String low;
                    private String up;
                    private String mid;

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }
                }

                public static class ABeanXX {
                    /**
                     * up : 4.73
                     * mid : 4.52
                     * low : 4.35
                     */

                    private String up;
                    private String mid;
                    private String low;

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }
            }

            public static class B2CPricesBean {
                /**
                 * c : {"low":"3.68","up":"4.43","mid":"4.15"}
                 * a : {"up":"5.28","mid":"5.04","low":"4.85"}
                 * b : {"mid":"4.72","up":"5.15","low":"4.25"}
                 */

                private CBeanXXX c;
                private ABeanXXX a;
                private BBeanXXX b;

                public CBeanXXX getC() {
                    return c;
                }

                public void setC(CBeanXXX c) {
                    this.c = c;
                }

                public ABeanXXX getA() {
                    return a;
                }

                public void setA(ABeanXXX a) {
                    this.a = a;
                }

                public BBeanXXX getB() {
                    return b;
                }

                public void setB(BBeanXXX b) {
                    this.b = b;
                }

                public static class CBeanXXX {
                    /**
                     * low : 3.68
                     * up : 4.43
                     * mid : 4.15
                     */

                    private String low;
                    private String up;
                    private String mid;

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }
                }

                public static class ABeanXXX {
                    /**
                     * up : 5.28
                     * mid : 5.04
                     * low : 4.85
                     */

                    private String up;
                    private String mid;
                    private String low;

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }

                public static class BBeanXXX {
                    /**
                     * mid : 4.72
                     * up : 5.15
                     * low : 4.25
                     */

                    private String mid;
                    private String up;
                    private String low;

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public String getUp() {
                        return up;
                    }

                    public void setUp(String up) {
                        this.up = up;
                    }

                    public String getLow() {
                        return low;
                    }

                    public void setLow(String low) {
                        this.low = low;
                    }
                }
            }
        }

        public static class ResidualRatioRankingBean {
            /**
             * b2BPrices : 4.18
             * c2BPrices : 4.02
             * b2CPrices : 4.66
             * trimId : 10000818
             * ncmsrp : 30.00
             * Month : 2018-05-01
             */

            private String b2BPrices;
            private String c2BPrices;
            private String b2CPrices;
            private String trimId;
            private String ncmsrp;
            private String Month;

            public String getB2BPrices() {
                return b2BPrices;
            }

            public void setB2BPrices(String b2BPrices) {
                this.b2BPrices = b2BPrices;
            }

            public String getC2BPrices() {
                return c2BPrices;
            }

            public void setC2BPrices(String c2BPrices) {
                this.c2BPrices = c2BPrices;
            }

            public String getB2CPrices() {
                return b2CPrices;
            }

            public void setB2CPrices(String b2CPrices) {
                this.b2CPrices = b2CPrices;
            }

            public String getTrimId() {
                return trimId;
            }

            public void setTrimId(String trimId) {
                this.trimId = trimId;
            }

            public String getNcmsrp() {
                return ncmsrp;
            }

            public void setNcmsrp(String ncmsrp) {
                this.ncmsrp = ncmsrp;
            }

            public String getMonth() {
                return Month;
            }

            public void setMonth(String Month) {
                this.Month = Month;
            }
        }
    }
}
