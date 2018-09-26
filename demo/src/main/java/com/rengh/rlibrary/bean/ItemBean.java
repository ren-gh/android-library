
package com.rengh.rlibrary.bean;

public class ItemBean {
    public Title title;
    public RedPackage redPackage;
    public Video video;
    public Commodity commodity;

    @Override
    public String toString() {
        return "ItemBean{" +
                "title=" + title +
                ", redPackage=" + redPackage +
                ", video=" + video +
                ", commodity=" + commodity +
                '}';
    }

    public static class Title {
        public String title;

        @Override
        public String toString() {
            return "Title{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    public static class RedPackage {
        public String activityCode, pid, picUrl;

        @Override
        public String toString() {
            return "RedPackage{" +
                    "activityCode='" + activityCode + '\'' +
                    ", pid='" + pid + '\'' +
                    ", picUrl='" + picUrl + '\'' +
                    '}';
        }
    }

    public static class Video {
        public VideoItem video1, video2, video3;

        @Override
        public String toString() {
            return "Video{" +
                    "video1=" + video1 +
                    ", video2=" + video2 +
                    ", video3=" + video3 +
                    '}';
        }

        public static class VideoItem {
            public String name;
            public String url;

            @Override
            public String toString() {
                return "VideoItem{" +
                        "name='" + name + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }

    public static class Commodity {
        public CommodityItem commodity1, commodity2, commodity3;

        @Override
        public String toString() {
            return "Commodity{" +
                    "commodity1=" + commodity1 +
                    ", commodity2=" + commodity2 +
                    ", commodity3=" + commodity3 +
                    '}';
        }

        public static class CommodityItem {
            // 商品Id
            public String itemId;
            // 商品标题
            public String title;
            // 商品品牌
            public String brand;
            // 商品底图
            public String whiteBgImage;
            // 商品价格
            public String showPrice;

            @Override
            public String toString() {
                return "CommodityItem{" +
                        "itemId='" + itemId + '\'' +
                        ", title='" + title + '\'' +
                        ", brand='" + brand + '\'' +
                        ", whiteBgImage='" + whiteBgImage + '\'' +
                        ", showPrice='" + showPrice + '\'' +
                        '}';
            }
        }
    }
}
