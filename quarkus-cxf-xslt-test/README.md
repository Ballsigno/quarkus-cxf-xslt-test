# quarkus-cxf-xslt-test Project

This is a reproducer project.

If you run `GreetingResourceTest.java`, you will see the successful result both `[GET] http://localhost:8080/test/hello1` and `[GET] http://localhost:8080/test/hello2`.

However, when it comes native image, you will see the failed result at `[GET] http://localhost:8080/test/hello1`.

This time I tested it in my local environment (Mac), so I did these commands to see the result.

```bash
# create native image
./mvnw package -Pnative -Dquarkus.native.container-build=true

# create docker image
docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/quarkus-cxf-xslt-test .

# Run!
docker run -i --rm -p 8080:8080 quarkus/quarkus-cxf-xslt-test

# check
# [GET] http://localhost:8080/test/hello1 set XSLT, failed.
# [GET] http://localhost:8080/test/hello2 just read the xsl file, success.
```
