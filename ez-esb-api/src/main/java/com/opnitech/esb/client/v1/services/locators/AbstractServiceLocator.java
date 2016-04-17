package com.opnitech.esb.client.v1.services.locators;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.opnitech.esb.client.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class AbstractServiceLocator {

    private final String serviceBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, String> methodURLCache = new HashMap<>();

    public AbstractServiceLocator(String serviceBaseUrl) {

        this.serviceBaseUrl = serviceBaseUrl;
    }

    protected ServiceException wrapException(Exception e) {

        return (ServiceException) (e instanceof ServiceException
                ? e
                : new ServiceException(
                        MessageFormat.format("Error calling the backend REST service. Base service url: ", this.serviceBaseUrl),
                        e));
    }

    protected <ResultType> ResultType invokeGet(String url, Class<ResultType> clazz, Object... arguments) {

        return ArrayUtils.isEmpty(arguments)
                ? this.restTemplate.getForObject(url, clazz)
                : this.restTemplate.getForObject(url, clazz, arguments);
    }

    protected <ResultType> ResultType invokePost(String url, Class<ResultType> clazz, Object request, Object... arguments) {

        return ArrayUtils.isEmpty(arguments)
                ? this.restTemplate.postForObject(url, request, clazz)
                : this.restTemplate.postForObject(url, request, clazz, arguments);
    }

    protected <ResultType> ResultType invokePostForGenerics(String url, ParameterizedTypeReference<ResultType> clazz,
            Object request, Object... arguments) {

        HttpEntity<Object> requestEntity = request != null
                ? new HttpEntity<>(request)
                : null;
        return this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, clazz, arguments).getBody();
    }

    protected String buildMethodUrl(String methodId, String... urlPieces) {

        String urlCash = this.methodURLCache.get(methodId);
        if (StringUtils.isNotBlank(urlCash)) {
            return urlCash;
        }

        return createMethodURL(methodId, urlPieces);
    }

    private String createMethodURL(String methodId, String... urlPieces) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getServiceBaseUrl());

        for (String urlPiece : urlPieces) {
            buffer.append(urlPiece);
        }

        this.methodURLCache.put(methodId, buffer.toString());

        return buffer.toString();
    }

    public String getServiceBaseUrl() {

        return this.serviceBaseUrl;
    }
}
