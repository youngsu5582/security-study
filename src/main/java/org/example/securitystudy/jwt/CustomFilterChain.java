package org.example.securitystudy.jwt;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class CustomFilterChain implements FilterChain {

    private final Filter[] filters;
    private int currentFilterIndex = 0;

    public CustomFilterChain(final Filter... filters) {
        this.filters = filters;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
        if (currentFilterIndex < filters.length) {
            final Filter currentFilter = filters[currentFilterIndex];
            currentFilterIndex++;
            currentFilter.doFilter(request, response, this); // 다음 필터 또는 서블릿에 요청 전달
        }
    }
}
