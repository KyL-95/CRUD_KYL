package com.vti.testing.bosung.resttemplate;

import com.vti.testing.bosung.resttemplate.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultsList {
    public List<Result> result;
    public Object targetUrl;
    public boolean success;
    public Object error;
    public boolean unAuthorizedRequest;
    public boolean __abp;
}
