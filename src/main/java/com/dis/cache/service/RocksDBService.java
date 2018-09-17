package com.dis.cache.service;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RocksDBService {

    private String dbPath = "data/rocksdb";

    private RocksDB rocksDB;

    final List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

    @PostConstruct
    public void open() throws Exception {
        // a static method that loads the RocksDB C++ library.
        RocksDB.loadLibrary();

        File dir = new File(dbPath);
        if(!dir.mkdirs()){
            dir.mkdirs();
        }

        try (DBOptions options = new DBOptions()
                .setCreateIfMissing(true)
                .setCreateMissingColumnFamilies(true);
             ColumnFamilyOptions cfOpts = new ColumnFamilyOptions()
                     .optimizeLevelStyleCompaction()) {

            List<ColumnFamilyDescriptor> cfDescriptors = new ArrayList<>();
            for (byte b : "0123456789".getBytes()) {
                cfDescriptors.add(new ColumnFamilyDescriptor(new byte[]{b}, cfOpts));
            }
            cfDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts));

            rocksDB = RocksDB.open(options, dbPath, cfDescriptors, cfHandles);
            log.info("open rocksDB is success");
        } catch (Exception e) {
            log.error("open rocksDB error", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (rocksDB != null) {
            log.info("close rocksDB");
            rocksDB.close();
            rocksDB = null;
        }
    }

    public void put(byte[] key, byte[] value) {
        try {
            rocksDB.put(cfHandles.get(0), key, value);
        } catch (RocksDBException e) {
            log.error("rocksDB put data error", e);
        }
    }

    public String read(byte[] key) {
        try {
            byte[] b = rocksDB.get(cfHandles.get(0), key);
            String str = new String(b);
            return str;
        } catch (RocksDBException e) {
            log.error("rocksDB read data error", e);
        }
        return null;
    }

}
