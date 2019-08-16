package com.taotao.search.service;

import com.taotao.pojo.SearchItem;
import com.taotao.pojo.SearchResult;
import com.taotao.pojo.TaotaoResult;

public interface SearchItemService {
    public TaotaoResult importAllItems();

    public SearchResult search(String queryString, int page, int rows) throws Exception;

    void addDocument(SearchItem searchItem);
}
