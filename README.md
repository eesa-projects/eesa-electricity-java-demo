# EESA Electricity

## 本地配置

```bash
cp src/test/resources/application-test.properties src/test/resources/application-local.properties
```

修改 `application-local.properties` 中的配置项。

## 执行测试

```bash
./mvnw test
```

执行单个测试类：

```bash
./mvnw test -Dtest=RegionApiTest
```

