package org.xcolab.util.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import org.xcolab.util.functions.Supplier;
import org.xcolab.util.http.caching.CacheKey;
import org.xcolab.util.http.caching.CacheProvider;
import org.xcolab.util.http.caching.CacheProviderNoOpImpl;
import org.xcolab.util.http.caching.CacheRetention;
import org.xcolab.util.http.exceptions.EntityNotFoundException;
import org.xcolab.util.http.exceptions.UncheckedEntityNotFoundException;
import org.xcolab.util.http.interceptors.HeaderRequestInterceptor;
import org.xcolab.util.http.interceptors.UriAwareResponseInterceptor;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class RequestHelper {

    private final RestTemplate restTemplate;

    private CacheProvider cacheProvider = new CacheProviderNoOpImpl();
    private boolean cacheActive;

    public RequestHelper(ResponseErrorHandler errorHandler) {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setErrorHandler(errorHandler);
        restTemplate.setInterceptors(Arrays.asList(new UriAwareResponseInterceptor(),
                new HeaderRequestInterceptor(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)));
    }

    public <R> List<R> getList(UriBuilder uriBuilder,
            ParameterizedTypeReference<List<R>> typeReference) {
        return getList(uriBuilder, typeReference, null, CacheRetention.NONE);
    }

    public <T, R> List<R> getList(final UriBuilder uriBuilder,
            final ParameterizedTypeReference<List<R>> typeReference,
            final CacheKey<T, List<R>> cacheKey, CacheRetention cacheRetention) {
        return getCached(cacheRetention, cacheKey, new Supplier<List<R>>() {
            @Override
            public List<R> get() {
                ResponseEntity<List<R>> response = restTemplate.exchange(uriBuilder.buildString(),
                        HttpMethod.GET, null, typeReference);
                return response.getBody();
            }
        });
    }

    public <T> T get(UriBuilder uriBuilder, Class<T> entityType)
            throws EntityNotFoundException {
        return get(uriBuilder, entityType, null, CacheRetention.NONE);
    }

    public <T, R> R get(UriBuilder uriBuilder, Class<R> entityType,
            CacheKey<T, R> cacheKey, CacheRetention cacheRetention)
            throws EntityNotFoundException {
        try {
            return getUnchecked(uriBuilder, entityType, cacheKey, cacheRetention);
        } catch (UncheckedEntityNotFoundException e) {
            throw new EntityNotFoundException(e.getLocalizedMessage());
        }
    }

    public <R> R getUnchecked(UriBuilder uriBuilder, Class<R> returnType) {
        return getUnchecked(uriBuilder, returnType, null, CacheRetention.NONE);
    }

    public <T, R> R getUnchecked(final UriBuilder uriBuilder, final Class<R> returnType,
            CacheKey<T, R> cacheKey, CacheRetention cacheRetention) {
        return getCached(cacheRetention, cacheKey, new Supplier<R>() {
            @Override
            public R get() {
                return restTemplate.getForObject(uriBuilder.buildUri(), returnType);
            }
        });
    }

    public int getCount(UriBuilder uriBuilder) {
        return getCount(uriBuilder, null, CacheRetention.REQUEST);
    }

    public int getCount(final UriBuilder uriBuilder,
            final CacheKey<?, Integer> cacheKey, CacheRetention cacheRetention) {
        return getCached(cacheRetention, cacheKey, new Supplier<Integer>() {
            @Override
            public Integer get() {
                final HttpHeaders httpHeaders = restTemplate.headForHeaders(uriBuilder.buildString());
                final List<String> countHeaders = httpHeaders.get("X-Total-Count");
                if (countHeaders.isEmpty()) {
                    return 0;
                }

                return Integer.valueOf(countHeaders.get(0));
            }
        });
    }

    private <T> T getCached(CacheRetention cacheRetention, CacheKey<?, T> cacheKey,
            Supplier<T> supplier) {
        T ret;
        final boolean cacheActive = isCacheActive() && cacheProvider.isActive()
                && cacheKey != null && cacheRetention != CacheRetention.NONE;
        if (cacheActive) {
            ret = cacheProvider.get(cacheKey, cacheRetention);
            if (ret != null) {
                return ret;
            }
        }
        ret = supplier.get();
        if (cacheActive) {
            cacheProvider.add(cacheKey, cacheRetention, ret);
        }
        return ret;
    }

    public boolean put(UriBuilder uriBuilder) {
        return put(uriBuilder, null, null);
    }

    public <T> boolean put(UriBuilder uriBuilder, T entity) {
        return put(uriBuilder, entity, null);
    }

    public <T> boolean put(UriBuilder uriBuilder, T entity, CacheKey<T, T> cacheKey) {

        final boolean cacheActive = isCacheActive() && cacheProvider.isActive() && cacheKey != null;
        if (cacheActive) {
            cacheProvider.replace(cacheKey, CacheRetention.REQUEST, entity);
        }

        HttpEntity<T> httpEntity = new HttpEntity<>(entity);

        restTemplate.exchange(uriBuilder.buildString(), HttpMethod.PUT, httpEntity, Void.class);
        return true;
    }

    public boolean delete(UriBuilder uriBuilder) {
        restTemplate.exchange(uriBuilder.buildString(), HttpMethod.DELETE, null, Void.class);
        return true;
    }

    public <T> T post(UriBuilder uriBuilder, Object entity, Class<T> returnType) {
        return restTemplate.postForObject(uriBuilder.buildString(), entity, returnType);
    }

    public void invalidateCache(CacheKey<?, ?> cacheKey, CacheRetention cacheRetention) {
        cacheProvider.delete(cacheKey, cacheRetention);
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public boolean isCacheActive() {
        return cacheActive;
    }

    public void setCacheActive(boolean cacheActive) {
        this.cacheActive = cacheActive;
    }
}
