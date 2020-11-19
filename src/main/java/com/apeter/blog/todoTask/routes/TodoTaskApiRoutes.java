package com.apeter.blog.todoTask.routes;

import com.apeter.blog.base.routers.BaseApiRoutes;

public class TodoTaskApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/todo-task";
    public static final String BY_ID = ROOT + "/{id}";
}
