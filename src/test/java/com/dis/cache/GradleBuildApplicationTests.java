package com.dis.cache;

import com.dis.cache.service.RocksDBService;
import io.vertx.core.Vertx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rocksdb.ColumnFamilyHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GradleBuildApplicationTests {

	@Autowired
	RocksDBService rocksDBService;

	private String key = "test";

	private String value = "hello world";

	private byte[] k = key.getBytes();

	final List<ColumnFamilyHandle> cfHandles = new ArrayList<>();


	@Test
	public void put() {
		byte[] v = value.getBytes();
		rocksDBService.put(k,v);
	}

	@Test
	public void read(){
		String str = rocksDBService.read(k);
		System.out.println("read rocksDB:"+str);
	}



}
