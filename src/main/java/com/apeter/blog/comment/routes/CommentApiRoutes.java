package com.apeter.blog.comment.routes;

import com.apeter.blog.base.routers.BaseApiRoutes;

public class CommentApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/comment";
    public static final String BY_ID = ROOT + "/{id}";
}
