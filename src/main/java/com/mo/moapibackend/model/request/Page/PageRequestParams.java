package com.mo.moapibackend.model.request.Page;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
public class PageRequestParams implements Serializable {

    private int pageSize;

    private int currentPage;
}
