package com.clt.common.error;

public class ExternalServiceError extends RuntimeException{
    
	private int statusCode;
    private String description;
	
	public ExternalServiceError (String message, String description, int statusCode) {
		super(message);
		this.statusCode = statusCode;
        this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

    public String getDescription(){
        return description;
    }
}
