package com.opnitech.esb.processor.services.impl.routes.connection;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.services.impl.routes.factory.RouteConnectionFactory;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RouteConnectionContainer {

    private Map<Long, RouteConnectionWrapper> routeConnections = new ConcurrentHashMap<>();

    private final RouteConnectionFactory routeConnectionFactory = new RouteConnectionFactory();

    public RouteConnectionContainer() {
        // Default constructor
    }

    public void cleanExpireConnections() throws ServiceException {

        synchronized (this) {
            Set<Entry<Long, RouteConnectionWrapper>> entrySet = new HashSet<>(this.routeConnections.entrySet());
            for (Entry<Long, RouteConnectionWrapper> entry : entrySet) {
                RouteConnectionWrapper routeConnectionWrapper = entry.getValue();

                if (routeConnectionWrapper.expire()) {
                    Long subscriptionId = entry.getKey();

                    closeRouteConnection(subscriptionId, routeConnectionWrapper);
                }
            }
        }
    }

    public <R extends RouteConnection<?>> R resolveRouteConnection(Subscription subscription) throws ServiceException {

        R routeConnection = resolveRouteConnection(subscription.getId(), subscription.getVersion());
        if (routeConnection == null) {
            routeConnection = registerConnection(subscription);
        }

        return routeConnection;
    }

    private <R extends RouteConnection<?>> R resolveRouteConnection(Long subscriptionId, Long version) throws ServiceException {

        RouteConnectionWrapper routeConnectionWrapper = this.routeConnections.get(subscriptionId);
        if (routeConnectionWrapper == null) {
            return null;
        }

        if (!Objects.equals(routeConnectionWrapper.getVersion(), version)) {
            closeRouteConnection(subscriptionId, routeConnectionWrapper);

            return null;
        }

        routeConnectionWrapper.visit();

        @SuppressWarnings("unchecked")
        R routeConnection = (R) routeConnectionWrapper.getRouteConnection();

        return routeConnection;
    }

    private void closeRouteConnection(Long subscriptionId, RouteConnectionWrapper routeConnectionWrapper)
            throws ServiceException {

        routeConnectionWrapper.getRouteConnection().close();
        this.routeConnections.remove(subscriptionId);
    }

    private <R extends RouteConnection<?>> R registerConnection(Subscription subscription) throws ServiceException {

        synchronized (this) {
            Long subscriptionId = subscription.getId();
            Long version = subscription.getVersion();

            R routeConnection = resolveRouteConnection(subscriptionId, version);

            if (routeConnection == null) {
                routeConnection = createRouteConnection(subscription, subscriptionId, version);
            }

            return routeConnection;
        }
    }

    private <R extends RouteConnection<?>> R createRouteConnection(Subscription subscription, Long subscriptionId, Long version)
            throws ServiceException {

        @SuppressWarnings("unchecked")
        R routeConnection = (R) this.routeConnectionFactory
                .build(MessageFormat.format("subscriptionRoute_{0}", Long.toString(subscriptionId)), subscription);

        RouteConnectionWrapper routeConnectionWrapper = new RouteConnectionWrapper(routeConnection, version);

        this.routeConnections.put(subscriptionId, routeConnectionWrapper);

        return routeConnection;
    }

    private class RouteConnectionWrapper {

        private static final int MILLIS_TO_HOUR = 1000 * 60 * 60;

        private long lastVisit;

        private final RouteConnection<?> routeConnection;
        private final Long version;

        public RouteConnectionWrapper(RouteConnection<?> routeConnection, Long version) {
            this.routeConnection = routeConnection;
            this.version = version;

            visit();
        }

        public boolean expire() {

            boolean expireDueHourNotUse = this.lastVisit > System.currentTimeMillis() - MILLIS_TO_HOUR;

            return expireDueHourNotUse;
        }

        public void visit() {

            this.lastVisit = System.currentTimeMillis();
        }

        public RouteConnection<?> getRouteConnection() {

            return this.routeConnection;
        }

        public Long getVersion() {

            return this.version;
        }
    }
}
