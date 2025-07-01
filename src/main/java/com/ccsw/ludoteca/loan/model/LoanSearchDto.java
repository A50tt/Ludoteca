package com.ccsw.ludoteca.loan.model;

import com.ccsw.ludoteca.common.pagination.PageableRequest;

import java.time.LocalDate;

/**
 * @author ccsw
 *
 */
public class LoanSearchDto {

    private PageableRequest pageable;
    private LocalDate date;
    private String gameTitle;
    private String clientName;


    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}