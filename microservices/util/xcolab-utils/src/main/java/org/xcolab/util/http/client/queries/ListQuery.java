package org.xcolab.util.http.client.queries;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.CollectionUtils;

import org.xcolab.util.http.ServiceRequestUtils;
import org.xcolab.util.http.UriBuilder;
import org.xcolab.util.http.caching.CacheKey;
import org.xcolab.util.http.caching.CacheRetention;
import org.xcolab.util.http.client.RestResource;

import java.util.List;

public class ListQuery<T> implements CacheableQuery<T, List<T>> {
    private final UriBuilder uriBuilder;
    private final ParameterizedTypeReference<List<T>> typeReference;
    private CacheKey<T, List<T>> cacheKey;
    private CacheRetention cacheRetention;

    public ListQuery(RestResource<T, ?> restResource,
            ParameterizedTypeReference<List<T>> typeReference) {
        this.typeReference = typeReference;
        this.uriBuilder = restResource.getResourceUrl();
    }

    @Override
    public List<T> execute() {
        if (cacheKey == null) {
            return ServiceRequestUtils.getList(uriBuilder, typeReference);
        } else {
            return ServiceRequestUtils.getList(uriBuilder, typeReference, cacheKey, cacheRetention);
        }
    }

    public ListResult<T> executeWithResult() {
        return new ListResult<>(this);
    }

    public ListQuery<T> addRange(int startRecord, int limitRecord) {
        uriBuilder.queryParam("startRecord", startRecord)
                .queryParam("limitRecord", limitRecord);
        return this;
    }

    @Override
    public ListQuery<T> withCache(CacheKey<T, List<T>> cacheKey, CacheRetention cacheRetention) {
        this.cacheKey = cacheKey;
        this.cacheRetention = cacheRetention;
        return this;
    }

    @Override
    public ListQuery<T> queryParam(String name, Object value) {
        uriBuilder.queryParam(name, value);
        return this;
    }

    @Override
    public ListQuery<T> queryParam(String name, Object... value) {
        uriBuilder.queryParam(name, value);
        return this;
    }

    @Override
    public ListQuery<T> optionalQueryParam(String name, Object value) {
        uriBuilder.optionalQueryParam(name, value);
        return this;
    }

    @Override
    public String toString() {
        return "ListQuery{" +
                "uriBuilder=" + uriBuilder.buildString() +
                ", typeReference=" + typeReference.toString() +
                ", cacheKey=" + cacheKey +
                ", cacheRetention=" + cacheRetention +
                '}';
    }

    public static class ListResult<T> {
        private final ListQuery<T> query;

        private ListResult(ListQuery<T> query) {
            this.query = query;
        }

        public List<T> get() {
            return query.execute();
        }

        public T getOne() {
            //fetch two elements so we can check if the result is unique
            List<T> result = checkNonNull(query.addRange(0, 2).execute());
            if (result.size() == 1) {
                return result.get(0);
            }
            throw new IndexOutOfBoundsException("Expected exactly one element, found " + result.size());
        }

        public T getOneIfExists() {
            //fetch two elements so we can check if the result is unique
            List<T> result = query.addRange(0, 2).execute();
            if (CollectionUtils.isEmpty(result)) {
                return null;
            }
            if (result.size() == 1) {
                return result.get(0);
            }
            throw new IndexOutOfBoundsException("Expected at most one element, found 2 or more");
        }

        public T getFirst() {
            List<T> result = checkNonNull(query.addRange(0, 1).execute());
            if (!result.isEmpty()) {
                return result.get(0);
            }
            throw new IndexOutOfBoundsException("Can't get first element of empty list");
        }

        public T getFirstIfExists() {
            List<T> result = query.addRange(0, 1).execute();
            if (CollectionUtils.isEmpty(result)) {
                return null;
            }
            return result.get(0);
        }

        private List<T> checkNonNull(List<T> result) {
            if (result == null) {
                throw new EmptyQueryResultException(query);
            }
            return result;
        }

        private static class EmptyQueryResultException extends IllegalStateException {
            EmptyQueryResultException(ListQuery<?> query) {
                super("Got empty result for " + query);
            }
        }
    }
}
