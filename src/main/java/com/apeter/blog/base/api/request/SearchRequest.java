package com.apeter.blog.base.api.request;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    @ApiParam(name = "query", value = "Search by fields", required = false)
    protected String query = null;
    @ApiParam(name = "size", value = "count of elements needed to be found (default 100)", required = false)
    protected Integer size = 100;
    @ApiParam(name = "skip", value = "Skip first n elements", required = false)
    protected Long skip = 0l;
}
