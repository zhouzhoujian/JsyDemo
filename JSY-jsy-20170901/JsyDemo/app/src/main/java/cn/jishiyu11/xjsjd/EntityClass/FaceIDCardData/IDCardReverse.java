package cn.jishiyu11.xjsjd.EntityClass.FaceIDCardData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jsy_zj on 2017/11/2.
 */

public class IDCardReverse {


    /**
     * legality : {"Edited":0,"Photocopy":0,"ID Photo":1,"Screen":0,"Temporary ID Photo":0}
     * request_id : 1505963132,28e364aa-97c0-469d-8738-cd20762e0251
     * time_used : 418
     * valid_date : 2016.02.29-2026.02.28
     * issued_by : 北京市海淀区公安局
     * side : back
     */

    private LegalityBean legality;
    private String request_id;
    private String time_used;
    private String valid_date;
    private String issued_by;
    private String side;

    public LegalityBean getLegality() {
        return legality;
    }

    public void setLegality(LegalityBean legality) {
        this.legality = legality;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getTime_used() {
        return time_used;
    }

    public void setTime_used(String time_used) {
        this.time_used = time_used;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getIssued_by() {
        return issued_by;
    }

    public void setIssued_by(String issued_by) {
        this.issued_by = issued_by;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public static class LegalityBean {
        public LegalityBean() {
        }

        public LegalityBean(String edited, String photocopy, String _$IDPhoto192, String screen, String _$TemporaryIDPhoto304) {
            Edited = edited;
            Photocopy = photocopy;
            this._$IDPhoto192 = _$IDPhoto192;
            Screen = screen;
            this._$TemporaryIDPhoto304 = _$TemporaryIDPhoto304;
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
        private String _$IDPhoto192; // FIXME check this code
        private String Screen;
        @SerializedName("Temporary ID Photo")
        private String _$TemporaryIDPhoto304; // FIXME check this code

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

        public String get_$IDPhoto192() {
            return _$IDPhoto192;
        }

        public void set_$IDPhoto192(String _$IDPhoto192) {
            this._$IDPhoto192 = _$IDPhoto192;
        }

        public String getScreen() {
            return Screen;
        }

        public void setScreen(String Screen) {
            this.Screen = Screen;
        }

        public String get_$TemporaryIDPhoto304() {
            return _$TemporaryIDPhoto304;
        }

        public void set_$TemporaryIDPhoto304(String _$TemporaryIDPhoto304) {
            this._$TemporaryIDPhoto304 = _$TemporaryIDPhoto304;
        }
    }

    public IDCardReverse() {
    }

    public IDCardReverse(LegalityBean legality, String request_id, String time_used, String valid_date, String issued_by, String side) {
        this.legality = legality;
        this.request_id = request_id;
        this.time_used = time_used;
        this.valid_date = valid_date;
        this.issued_by = issued_by;
        this.side = side;
    }
}
