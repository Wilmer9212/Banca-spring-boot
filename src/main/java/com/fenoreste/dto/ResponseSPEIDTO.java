package com.fenoreste.dto;

public class ResponseSPEIDTO {
	    int id;
	    String error;

	    public ResponseSPEIDTO() {
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getError() {
	        return error;
	    }

	    public void setError(String error) {
	        this.error = error;
	    }

	    @Override
	    public String toString() {
	        return "ResponseSPEIDTO{" + "id=" + id + ", error=" + error + '}';
	    }
	    
}
