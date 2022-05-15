package com.johns.recyclerfragment;

public class RecyclerDataModel {
    String ImgUrl;
    String ItemTitle;
    String ItemPrice;
    public RecyclerDataModel(String mItemTitle, String mItemPrice,String mImageUrl) {
        this.ImgUrl = mImageUrl;
        this.ItemTitle = mItemTitle;
        this.ItemPrice = mItemPrice;
    }
    public String getImgUrl() {
        return ImgUrl;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

}
