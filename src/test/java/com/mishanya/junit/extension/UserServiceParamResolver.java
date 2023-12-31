package com.mishanya.junit.extension;

import com.mishanya.junit.dao.UserDao;
import com.mishanya.junit.service.UserService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UserServiceParamResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserService.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        var store = extensionContext.getStore(ExtensionContext.Namespace.create(UserService.class));
        var store = extensionContext.getStore(Namespace.create(extensionContext.getTestMethod()));
        return store.getOrComputeIfAbsent(UserService.class, it -> new UserService(null));
    }
}
