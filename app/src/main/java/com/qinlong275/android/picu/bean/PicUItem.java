package com.qinlong275.android.picu.bean;

public class PicUItem {

    //还要存一些：点击的位置，文字颜色，位置。。。

    private String userid;     //唯一标识用户，可用来获取头像
    private String piuId;       //唯一标识一个PicU
    private String userName;    //qq用户名
    private String picUfrom;    //1:我制作的  2：我收到的
    private String pictureUrl;  //图片的Url
    private String voiceUrl;    //语音的Url
    private String secretType;  //1:语音彩蛋    2：文字彩蛋
    private String unBlockType;  //1:语音 2：画图   3：点击  4：拍照
    private String isBlock;     //标记是否已经被解锁，true锁状态，false已解锁

    public PicUItem(String userid, String piuId, String userName, String picUfrom, String pictureUrl,
                    String voiceUrl, String secretType, String unBlockType, String isBlock) {
        this.userid = userid;
        this.piuId = piuId;
        this.userName = userName;
        this.picUfrom = picUfrom;
        this.pictureUrl = pictureUrl;
        this.voiceUrl = voiceUrl;
        this.secretType = secretType;
        this.unBlockType = unBlockType;
        this.isBlock = isBlock;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPiuId() {
        return piuId;
    }

    public void setPiuId(String piuId) {
        this.piuId = piuId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPicUfrom() {
        return picUfrom;
    }

    public void setPicUfrom(String picUfrom) {
        this.picUfrom = picUfrom;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getSecretType() {
        return secretType;
    }

    public void setSecretType(String secretType) {
        this.secretType = secretType;
    }

    public String getUnBlockType() {
        return unBlockType;
    }

    public void setUnBlockType(String unBlockType) {
        this.unBlockType = unBlockType;
    }

    public String getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(String isBlock) {
        this.isBlock = isBlock;
    }
}
