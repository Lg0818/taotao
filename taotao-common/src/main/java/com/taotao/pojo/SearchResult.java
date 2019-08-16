package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
    //60个商品信息 的集合对象
    private List<SearchItem> itemList;
    //总记录条数
    private long recordCount;
    //总页数
    private long pageCount;

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "itemList=" + itemList +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                '}';
    }
}
