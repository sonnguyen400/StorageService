package com.sonnguyen.storageservice.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class StatementInspectorConfig implements StatementInspector {
    @Override
    public String inspect(String sql) {
        return sql.replace("like ?", "ilike unaccent(?)");
    }
}
