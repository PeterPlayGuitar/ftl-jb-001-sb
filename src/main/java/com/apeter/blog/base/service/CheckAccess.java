package com.apeter.blog.base.service;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.auth.service.AuthService;
import com.apeter.blog.user.model.UserDoc;
import org.bson.types.ObjectId;

public abstract class CheckAccess<T> {

    protected abstract ObjectId getOwnerFromEntity(T entity);

    protected UserDoc checkAccess(T entity) throws NoAccessException, AuthException {
        ObjectId ownerId = getOwnerFromEntity(entity);

        UserDoc owner = authService().currentUser();

        if (!owner.getId().equals(ownerId))
            throw new NoAccessException();

        return owner;
    }

    protected abstract AuthService authService();
}
