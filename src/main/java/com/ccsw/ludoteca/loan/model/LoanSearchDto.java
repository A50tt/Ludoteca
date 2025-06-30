package com.ccsw.ludoteca.loan.model;

import com.ccsw.ludoteca.common.pagination.PageableRequest;

/**
 * @author ccsw
 *
 */
public class LoanSearchDto {

    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}