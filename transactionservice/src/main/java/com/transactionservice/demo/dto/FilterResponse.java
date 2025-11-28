package com.transactionservice.demo.dto;

import java.util.List;

public class FilterResponse {
    private long total;
    private List<TransactionResponse> data;

    public FilterResponse(long total, List<TransactionResponse> data) {
        this.total = total;
        this.data = data;
    }

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<TransactionResponse> getData() {
		return data;
	}

	public void setData(List<TransactionResponse> data) {
		this.data = data;
	}
}
