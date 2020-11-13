package edu.utdallas.cs6303.finalproject.model.pages;

import com.paypal.http.Headers;
import com.paypal.http.HttpResponse;

public class PayPalHttpResponse<T> extends HttpResponse<T> {

    public PayPalHttpResponse(Headers headers, int statusCode, T result) {
        super(headers, statusCode, result);
    }

    public Headers getHeaders() {
        return super.headers();
    }

    public int getStatusCode() {
        return super.statusCode();
    }

    public T getResult() {
        return super.result();
    }
    
}
