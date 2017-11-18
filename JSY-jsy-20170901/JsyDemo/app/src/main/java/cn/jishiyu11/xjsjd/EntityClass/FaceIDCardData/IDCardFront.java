package cn.jishiyu11.xjsjd.EntityClass.FaceIDCardData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jsy_zj on 2017/11/2.
 */

public class IDCardFront {
    public IDCardFront() {
    }

    public IDCardFront(String race, String name, String time_used, String gender, String id_card_number, String request_id, BirthdayBean birthday, LegalityBean legality, String address, HeadRectBean head_rect, String side) {
        this.race = race;
        this.name = name;
        this.time_used = time_used;
        this.gender = gender;
        this.id_card_number = id_card_number;
        this.request_id = request_id;
        this.birthday = birthday;
        this.legality = legality;
        this.address = address;
        this.head_rect = head_rect;
        this.side = side;
    }

    /**
     * race : 汉
     * name : 张三
     * time_used : 1144
     * gender : 男
     * id_card_number : 110xxxxxx123456789
     * request_id : 1505962912,9066edb8-719e-4ee3-8eaf-312b863fcce3
     * birthday : {"year":"1989","day":"3","month":"4"}
     * legality : {"Edited":0,"Photocopy":0,"ID Photo":1,"Screen":0,"Temporary ID Photo":0}
     * address : 北京市海淀区xxxxx
     * head_rect : {"rt":{"y":0.19160387,"x":0.89415807},"lt":{"y":0.19160387,"x":0.59965634},"lb":{"y":0.712056,"x":0.59965634},"rb":{"y":0.71905273,"x":0.89415807}}
     * side : front
     */

    private String race;
    private String name;
    private String time_used;
    private String gender;
    private String id_card_number;
    private String request_id;
    private BirthdayBean birthday;
    private LegalityBean legality;
    private String address;
    private HeadRectBean head_rect;
    private String side;

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_used() {
        return time_used;
    }

    public void setTime_used(String time_used) {
        this.time_used = time_used;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public BirthdayBean getBirthday() {
        return birthday;
    }

    public void setBirthday(BirthdayBean birthday) {
        this.birthday = birthday;
    }

    public LegalityBean getLegality() {
        return legality;
    }

    public void setLegality(LegalityBean legality) {
        this.legality = legality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HeadRectBean getHead_rect() {
        return head_rect;
    }

    public void setHead_rect(HeadRectBean head_rect) {
        this.head_rect = head_rect;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public static class BirthdayBean {
        public BirthdayBean() {
        }

        public BirthdayBean(String year, String day, String month) {
            this.year = year;
            this.day = day;
            this.month = month;
        }

        /**
         * year : 1989
         * day : 3
         * month : 4
         */

        private String year;
        private String day;
        private String month;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public static class LegalityBean {
        public LegalityBean() {
        }

        public LegalityBean(String edited, String photocopy, String _$IDPhoto91, String screen, String _$TemporaryIDPhoto79) {
            Edited = edited;
            Photocopy = photocopy;
            this._$IDPhoto91 = _$IDPhoto91;
            Screen = screen;
            this._$TemporaryIDPhoto79 = _$TemporaryIDPhoto79;
        }

        /**
         * Edited : 0
         * Photocopy : 0
         * ID Photo : 1
         * Screen : 0
         * Temporary ID Photo : 0
         */

        private String Edited;
        private String Photocopy;
        @SerializedName("ID Photo")
        private String _$IDPhoto91; // FIXME check this code
        private String Screen;
        @SerializedName("Temporary ID Photo")
        private String _$TemporaryIDPhoto79; // FIXME check this code

        public String getEdited() {
            return Edited;
        }

        public void setEdited(String Edited) {
            this.Edited = Edited;
        }

        public String getPhotocopy() {
            return Photocopy;
        }

        public void setPhotocopy(String Photocopy) {
            this.Photocopy = Photocopy;
        }

        public String get_$IDPhoto91() {
            return _$IDPhoto91;
        }

        public void set_$IDPhoto91(String _$IDPhoto91) {
            this._$IDPhoto91 = _$IDPhoto91;
        }

        public String getScreen() {
            return Screen;
        }

        public void setScreen(String Screen) {
            this.Screen = Screen;
        }

        public String get_$TemporaryIDPhoto79() {
            return _$TemporaryIDPhoto79;
        }

        public void set_$TemporaryIDPhoto79(String _$TemporaryIDPhoto79) {
            this._$TemporaryIDPhoto79 = _$TemporaryIDPhoto79;
        }
    }

    public static class HeadRectBean {
        public HeadRectBean(RtBean rt, LtBean lt, LbBean lb, RbBean rb) {
            this.rt = rt;
            this.lt = lt;
            this.lb = lb;
            this.rb = rb;
        }

        public HeadRectBean() {
        }

        /**
         * rt : {"y":0.19160387,"x":0.89415807}
         * lt : {"y":0.19160387,"x":0.59965634}
         * lb : {"y":0.712056,"x":0.59965634}
         * rb : {"y":0.71905273,"x":0.89415807}
         */

        private RtBean rt;
        private LtBean lt;
        private LbBean lb;
        private RbBean rb;

        public RtBean getRt() {
            return rt;
        }

        public void setRt(RtBean rt) {
            this.rt = rt;
        }

        public LtBean getLt() {
            return lt;
        }

        public void setLt(LtBean lt) {
            this.lt = lt;
        }

        public LbBean getLb() {
            return lb;
        }

        public void setLb(LbBean lb) {
            this.lb = lb;
        }

        public RbBean getRb() {
            return rb;
        }

        public void setRb(RbBean rb) {
            this.rb = rb;
        }

        public static class RtBean {
            public RtBean() {
            }

            public RtBean(String y, String x) {
                this.y = y;
                this.x = x;
            }

            /**
             * y : 0.19160387
             * x : 0.89415807
             */

            private String y;
            private String x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }

        public static class LtBean {
            public LtBean() {
            }

            public LtBean(String y, String x) {
                this.y = y;
                this.x = x;
            }

            /**
             * y : 0.19160387
             * x : 0.59965634
             */

            private String y;
            private String x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }

        public static class LbBean {
            public LbBean() {
            }

            public LbBean(String y, String x) {
                this.y = y;
                this.x = x;
            }

            /**
             * y : 0.712056
             * x : 0.59965634
             */

            private String y;
            private String x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }

        public static class RbBean {
            public RbBean(String y, String x) {
                this.y = y;
                this.x = x;
            }

            public RbBean() {
            }

            /**
             * y : 0.71905273
             * x : 0.89415807
             */

            private String y;
            private String x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }
    }


}
