package com.plumber.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class NoPasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public NoPasswordAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
