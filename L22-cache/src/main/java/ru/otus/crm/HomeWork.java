package ru.otus.crm;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientCachedImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;

/**
 * VM options: -Xmx256m -Xms256m -Xlog:gc=debug
 */
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws InterruptedException {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        HwCache<String, Client> cache = new MyCache<>();
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String clientId, Client client, String action) {
                log.info("key:{}, value:{}, action: {}", clientId, client, action);
            }
        };
        cache.addListener(listener);

        var dbServiceClient = new DbServiceClientCachedImpl(transactionRunner, dataTemplateClient, cache);

        var client1 = dbServiceClient.saveClient(new Client("dbService" + 1));
        var client1Id = client1.getId();

        long start = System.currentTimeMillis();
        dbServiceClient.getClient(client1Id);
        log.info("selecting time from cache: {}", System.currentTimeMillis() - start);

        System.gc();
        Thread.sleep(100);
        log.info("cache is gone. client1Id: {}, client: {}", client1Id, cache.get(String.valueOf(client1Id)));

        start = System.currentTimeMillis();
        dbServiceClient.getClient(client1Id);
        log.info("selecting time from db: {}", System.currentTimeMillis() - start);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
