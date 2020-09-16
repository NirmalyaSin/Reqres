package com.reqres.screens.home.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserListResponse implements Serializable {

	@SerializedName("per_page")
	private Integer perPage;

	@SerializedName("total")
	private Integer total;

	@SerializedName("ad")
	private Ad ad;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("page")
	private Integer page;

	@SerializedName("total_pages")
	private Integer totalPages;

	public void setPerPage(Integer perPage){
		this.perPage = perPage;
	}

	public Integer getPerPage(){
		return perPage;
	}

	public void setTotal(Integer total){
		this.total = total;
	}

	public Integer getTotal(){
		return total;
	}

	public void setAd(Ad ad){
		this.ad = ad;
	}

	public Ad getAd(){
		return ad;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setPage(Integer page){
		this.page = page;
	}

	public Integer getPage(){
		return page;
	}

	public void setTotalPages(Integer totalPages){
		this.totalPages = totalPages;
	}

	public Integer getTotalPages(){
		return totalPages;
	}
}